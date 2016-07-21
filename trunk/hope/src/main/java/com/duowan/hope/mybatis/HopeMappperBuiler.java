package com.duowan.hope.mybatis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import com.duowan.hope.mybatis.annotation.HopeCount;
import com.duowan.hope.mybatis.annotation.HopeDelete;
import com.duowan.hope.mybatis.annotation.HopeInsert;
import com.duowan.hope.mybatis.annotation.HopeSelect;
import com.duowan.hope.mybatis.annotation.HopeUpdate;
import com.duowan.hope.mybatis.annotation.Table;
import com.duowan.hope.mybatis.database.DataBaseFieldInfo;
import com.duowan.hope.mybatis.initparams.MethodInfo;
import com.duowan.hope.mybatis.initparams.TableInfo;

public class HopeMappperBuiler {

	protected TypeAliasRegistry typeAliasRegistry;

	private static final String NAME_SPACE = "namespace";
	private static final String UTF_8 = "utf-8";
	private static final String COLUMN_NAME = "COLUMN_NAME";
	private static final String TYPE_NAME = "TYPE_NAME";
	private static final String IS_NULLABLE = "IS_NULLABLE";
	private static final String COLUMN_DEF = "TYPE_NAME";
	private static final String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";

	private static Set<String> PRIMARY_KEY = new HashSet<>();
	private String genericReturnType;

	public InputStream build(String resource, Connection connection, TypeAliasRegistry typeAliasRegistry) {
		this.typeAliasRegistry = typeAliasRegistry;

		InputStream inputStream = null;

		Document document = getDocument(resource);
		Element root = document.getRootElement();
		try {

			// 构建XML
			buildSqls(root, connection);

			// 将XML转换成inputStream,但并不写入物理文件
			inputStream = new ByteArrayInputStream(StringEscapeUtils.unescapeXml(document.asXML()).getBytes(UTF_8));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	public InputStream build(File file, Connection connection, TypeAliasRegistry typeAliasRegistry) {
		this.typeAliasRegistry = typeAliasRegistry;
		InputStream inputStream = null;

		Document document = getDocument(file);
		Element root = document.getRootElement();
		try {

			// 构建XML
			buildSqls(root, connection);

			// 将XML转换成inputStream,但并不写入物理文件
			inputStream = new ByteArrayInputStream(StringEscapeUtils.unescapeXml(document.asXML()).getBytes(UTF_8));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	private Document getDocument(File file) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 获取dom
	 * 
	 * @param resource
	 * @return
	 */
	private Document getDocument(String resource) {
		ClassPathResource cpr = new ClassPathResource(resource);
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(cpr.getFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	private Annotation getHopeAnnotation(Method method) {
		Annotation annotation = method.getAnnotation(HopeSelect.class);
		if (annotation == null) {
			annotation = method.getAnnotation(HopeCount.class);
		}

		if (annotation == null) {
			annotation = method.getAnnotation(HopeInsert.class);
		}

		if (annotation == null) {
			annotation = method.getAnnotation(HopeDelete.class);
		}

		if (annotation == null) {
			annotation = method.getAnnotation(HopeUpdate.class);
		}

		return annotation;
	}

	/**
	 * 获取泛型返回对象
	 */
	private void getGenericReturnType(Class<?> daoClz) {
		this.genericReturnType = null;
		Type[] types = daoClz.getGenericInterfaces();
		for (Type type : types) {
			Type[] params = ((ParameterizedType) type).getActualTypeArguments();
			if (null != params && params.length > 0) {
				Class<?> entityClass = (Class<?>) params[0];
				this.genericReturnType = entityClass.getName();
			}
		}

	}

	private List<Method> getMethod(Class<?> daoClz) {
		List<Method> list = null;
		if (null != daoClz) {
			// 获取接口本身的方法
			list = new ArrayList<>();
			Method[] methods = daoClz.getDeclaredMethods();
			for (Method method : methods) {
				list.add(method);
			}
			// 父接口
			Class<?>[] superInterfaces = daoClz.getInterfaces();
			for (Class<?> clz : superInterfaces) {
				list.addAll(getMethod(clz));
			}
		}
		return list;
	}

	private void buildSqls(Element root, Connection connection) throws ClassNotFoundException {
		String namespace = root.attributeValue(NAME_SPACE);
		Class<?> daoClz = Class.forName(namespace);

		getGenericReturnType(daoClz);
		List<Method> methods = getMethod(daoClz);

		TableInfo tableInfo = getTableInfo(daoClz);

		Map<String, DataBaseFieldInfo> columns = getTableColumn(connection, tableInfo.getTableNameUnderLine());

		for (Method method : methods) {
			Annotation annotation = getHopeAnnotation(method);
			MethodInfo methodInfo = new MethodInfo(method, annotation, columns, typeAliasRegistry, tableInfo, this.genericReturnType, namespace);
			if (annotation != null) {
				// build select
				if (methodInfo.getType().equals(HopeSelect.class.getSimpleName())) {
					buildSelect(methodInfo, root);
					continue;
				}

				// build count
				if (methodInfo.getType().equals(HopeCount.class.getSimpleName())) {
					buildCount(methodInfo, root);
					continue;
				}

				// build insert
				if (methodInfo.getType().equals(HopeInsert.class.getSimpleName())) {
					buildInsert(methodInfo, root);
					continue;
				}

				// build insert
				if (methodInfo.getType().equals(HopeDelete.class.getSimpleName())) {
					buildDelete(methodInfo, root);
					continue;
				}

				// build update
				if (methodInfo.getType().equals(HopeUpdate.class.getSimpleName())) {
					buildUpdate(methodInfo, root);
					continue;
				}

			}
		}

	}

	private void buildUpdate(MethodInfo methodInfo, Element root) {
		String set = methodInfo.getUpdate();
		String where = methodInfo.getWhere();
		String tableName = methodInfo.getTableName();
		String tableSuffix = methodInfo.getTableSuffix();

		String context = String.format(TagReources.SQL_UPDATE, tableName, tableSuffix, set, where);
		Element element = root.addElement(TagReources.ELEMENT_TYPE_UPDATE);
		setElementAttr(element, methodInfo.getId(), null, null, context, null, null);
	}

	private void buildDelete(MethodInfo methodInfo, Element root) {

		String where = methodInfo.getWhere();
		String tableName = methodInfo.getTableName();
		String tableSuffix = methodInfo.getTableSuffix();

		String context = String.format(TagReources.SQL_DELETE, tableName, tableSuffix, where);
		Element element = root.addElement(TagReources.ELEMENT_TYPE_DELETE);
		setElementAttr(element, methodInfo.getId(), null, null, context, null, null);
	}

	private void buildInsert(MethodInfo methodInfo, Element root) {

		String insertField = methodInfo.getInsertField();
		String insertValue = methodInfo.getInsertValue();
		String tableName = methodInfo.getTableName();
		String tableSuffix = methodInfo.getTableSuffix();
		String isReturnPrimaryKey = "";
		String primaryKey = "";

		String formatStr = TagReources.SQL_INSERT;
		if (methodInfo.isCollection()) {
			formatStr = TagReources.SQL_BATCH_INSERT_LIST;
		}

		if (methodInfo.isArray()) {
			formatStr = TagReources.SQL_BATCH_INSERT_ARRAY;
		}

		if (methodInfo.isReturnPrimaryKey()) {
			isReturnPrimaryKey = "true";
			primaryKey = methodInfo.getPrimaryKey();
		}

		String context = String.format(formatStr, tableName, tableSuffix, insertField, insertValue);
		Element element = root.addElement(TagReources.ELEMENT_TYPE_INSERT);
		setElementAttr(element, methodInfo.getId(), methodInfo.getParamterType(), null, context, isReturnPrimaryKey, primaryKey);
	}

	private void buildCount(MethodInfo methodInfo, Element root) {

		String selectFields = methodInfo.getSelectCountFields();
		String where = methodInfo.getWhere();
		String groupBy = methodInfo.getGroupBy();
		String orderBy = methodInfo.getOrderBy();
		String tableName = methodInfo.getTableName();
		String tableSuffix = methodInfo.getTableSuffix();

		String context = String.format(TagReources.SQL_SELECT, selectFields, tableName, tableSuffix, where, groupBy, orderBy);
		Element element = root.addElement(TagReources.ELEMENT_TYPE_SELECT);
		setElementAttr(element, methodInfo.getId(), null, methodInfo.getReturnType(), context, null, null);
	}

	private void buildSelect(MethodInfo methodInfo, Element root) {

		String selectFields = methodInfo.getSelectFields();
		String where = methodInfo.getWhere();
		String groupBy = methodInfo.getGroupBy();
		String orderBy = methodInfo.getOrderBy();
		String tableName = methodInfo.getTableName();
		String tableSuffix = methodInfo.getTableSuffix();

		String sql = String.format(TagReources.SQL_SELECT, selectFields, tableName, tableSuffix, where, groupBy, orderBy);
		Element element = root.addElement(TagReources.ELEMENT_TYPE_SELECT);
		setElementAttr(element, methodInfo.getId(), null, methodInfo.getReturnType(), sql, null, null);
	}

	private TableInfo getTableInfo(Class<?> daoClz) {
		TableInfo tableInfo = null;
		Table table = (Table) daoClz.getDeclaredAnnotation(Table.class);
		if (null != table) {
			tableInfo = new TableInfo(table);
		}
		return tableInfo;
	}

	private TreeMap<String, DataBaseFieldInfo> getTableColumn(Connection connection, String tableName) {
		TreeMap<String, DataBaseFieldInfo> columns = new TreeMap<>();
		String columnName = "";
		String typeName = "";
		String nullAble = "";
		String defaultValue = "";
		String autoincrement = "";
		boolean isPrimaryKey = false;

		try {
			// 获取主键
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName);
			while (primaryKeys.next()) {
				PRIMARY_KEY.add(primaryKeys.getString(COLUMN_NAME).toLowerCase());
			}

			// 获取字段信息
			ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, "%");
			while (resultSet.next()) {
				isPrimaryKey = false;
				columnName = resultSet.getString(COLUMN_NAME).toLowerCase();
				typeName = resultSet.getString(TYPE_NAME);
				nullAble = resultSet.getString(IS_NULLABLE); // 是否允许NULL
				defaultValue = resultSet.getString(COLUMN_DEF); // 字段默认值
				autoincrement = resultSet.getString(IS_AUTOINCREMENT); // 是否自增长

				// 是否为主键
				if (PRIMARY_KEY.contains(columnName)) {
					isPrimaryKey = true;
				}

				DataBaseFieldInfo dataBaseFieldInfo = new DataBaseFieldInfo(columnName, typeName, nullAble, defaultValue, autoincrement, isPrimaryKey);
				columns.put(columnName, dataBaseFieldInfo);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}

	private void setElementAttr(Element element, String mapperId, String parameterType, String resultType, String context, String useGeneratedKeys, String keyProperty) {
		if (!StringUtils.isEmpty(mapperId)) {
			element.addAttribute(TagReources.MAPPER_ID, mapperId);
		}
		if (!StringUtils.isEmpty(parameterType)) {
			element.addAttribute(TagReources.MAPPER_PARAMETER_TYPE, parameterType);
		}
		if (!StringUtils.isEmpty(resultType)) {
			element.addAttribute(TagReources.MAPPER_RESULT_TYPE, resultType);
		}
		if (!StringUtils.isEmpty(useGeneratedKeys)) {
			element.addAttribute(TagReources.USE_GENERATED_KEYS, useGeneratedKeys);
		}
		if (!StringUtils.isEmpty(keyProperty)) {
			element.addAttribute(TagReources.KEY_PROPERTY, keyProperty);
		}

		element.setText(context);
	}
}
