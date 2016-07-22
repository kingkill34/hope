package com.duowan.hope.mybatis.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import com.duowan.hope.mybatis.initparams.MethodPage;
import com.duowan.hope.mybatis.initparams.MethodPageInfo;
import com.duowan.hope.mybatis.util.MetaObjectUtil;

/**
 * 分页拦截器
 * 
 * @author frankie
 * @date 2016年7月22日 上午10:09:23
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }),
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class PageInterceptor implements Interceptor {

	public static final ThreadLocal<HopePage<?>> localHopePage = new ThreadLocal<HopePage<?>>();

	private static final String DELEGATE = "delegate";

	private static final String MAPPEDSTATEMENT = "mappedStatement";

	private static final String DELEGATE_BOUNDSQL_SQL = "delegate.boundSql.sql";

	private static final String COUNT_SQL = "select count(0) from (%s) as countTable";

	private static final String LIMIT = " limit %s,%s";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();

		if (target instanceof StatementHandler) {
			RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();

			// 获取 StatementHandler
			MetaObject metaStatementHandlerMo = MetaObjectUtil.forObject(handler);
			StatementHandler statementHandler = (StatementHandler) metaStatementHandlerMo.getValue(DELEGATE);

			// 获取MappedStatement
			MetaObject statementHandlerMo = MetaObjectUtil.forObject(statementHandler);
			MappedStatement mappedStatement = (MappedStatement) statementHandlerMo.getValue(MAPPEDSTATEMENT);

			String methodId = mappedStatement.getId();

			MethodPage methodPage = MethodPageInfo.get(methodId);
			if (null != methodPage) {
				BoundSql boundSql = handler.getBoundSql();
				Object obj = boundSql.getParameterObject();
				if (obj == null) {
					return invocation.proceed();
				}

				// 以MAP的形式传递分页参数
				Map<String, Object> params = (Map<String, Object>) obj;
				// 设置分页信息
				HopePage<?> hopePage = localHopePage.get();
				if (null == hopePage) {
					hopePage = new HopePage<>();
					hopePage.setPageNo(getIntValue(params, methodPage.getPageNo()));
					hopePage.setPageSize(getIntValue(params, methodPage.getPageSize()));
					localHopePage.set(hopePage);
				}

				Connection connection = (Connection) invocation.getArgs()[0];
				setTotalCount(connection, mappedStatement, boundSql, hopePage);

				String sql = buildSelectSql(boundSql.getSql(), hopePage);
				metaStatementHandlerMo.setValue(DELEGATE_BOUNDSQL_SQL, sql);
			}
		} else if (invocation.getTarget() instanceof ResultSetHandler) {
			// 将结果塞进PAGE对象
			Object result = invocation.proceed();
			HopePage<?> page = localHopePage.get();
			if (null != page) {
				page.setData((List) result);
				result = page;
			}
			return result;
		}
		// 将执行权交给下一个拦截器
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler || target instanceof ResultSetHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}

	private String buildSelectSql(String sql, HopePage<?> hopePage) {
		int start = (hopePage.getPageNo() - 1) * hopePage.getPageSize();
		int end = hopePage.getPageSize();
		sql += String.format(LIMIT, start, end);
		return sql;
	}

	/**
	 * 获取总记录数
	 * 
	 * @param sql
	 * @param connection
	 * @param mappedStatement
	 * @param boundSql
	 * @param page
	 */
	private void setTotalCount(Connection connection, MappedStatement mappedStatement, BoundSql boundSql, HopePage<?> hopePage) {
		// 记录总记录数
		String countSql = String.format(COUNT_SQL, boundSql.getSql());
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());

			// 设置参数
			ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBS);
			parameterHandler.setParameters(countStmt);

			rs = countStmt.executeQuery();
			int totalCount = 0;
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
			hopePage.setTotalCount(totalCount);
			int totalPage = totalCount / hopePage.getPageSize() + ((totalCount % hopePage.getPageSize() == 0) ? 0 : 1);
			hopePage.setTotalPage(totalPage);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(countStmt, rs);
		}
	}

	private void close(PreparedStatement countStmt, ResultSet rs) {
		try {
			countStmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取MAP值，转换成INTEGER
	 * 
	 * @param params
	 * @param key
	 * @return
	 */
	private Integer getIntValue(Map params, String key) {
		Object value = params.get(key);
		return Integer.valueOf(value.toString());
	}

}
