package com.duowan.hope.mybatis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import com.duowan.hope.mybatis.annotation.HopeCount;
import com.duowan.hope.mybatis.annotation.HopeInsert;
import com.duowan.hope.mybatis.annotation.HopeSelect;
import com.duowan.hope.mybatis.annotation.Table;
import com.duowan.hope.mybatis.database.DataBaseFieldInfo;
import com.duowan.hope.mybatis.initparams.MethodInfo;
import com.duowan.hope.mybatis.initparams.TableInfo;

public class HopeMappperBuiler2 {

	protected TypeAliasRegistry typeAliasRegistry;

	private static final String NAME_SPACE = "namespace";
	private static final String UTF_8 = "utf-8";
	private static final String COLUMN_NAME = "COLUMN_NAME";
	public static final Set<String> CONSTANT = new HashSet<String>();

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
		return annotation;
	}

	private void buildSqls(Element root, Connection connection) throws ClassNotFoundException {
		String namespace = root.attributeValue(NAME_SPACE);
		Class<?> daoClz = Class.forName(namespace);
		Method[] methods = daoClz.getDeclaredMethods();
		TableInfo tableInfo = getTableInfo(daoClz);

		Map<String, DataBaseFieldInfo> columns = getTableColumn(connection, tableInfo.getTableName());

		for (Method method : methods) {
			Annotation annotation = getHopeAnnotation(method);
			MethodInfo methodInfo = new MethodInfo(method, annotation, columns, typeAliasRegistry, tableInfo);
			if (annotation != null) {
				// build select
				if (methodInfo.getType().equals(HopeSelect.class.getSimpleName())) {
					buildSelect(methodInfo, root);
					break;
				}

				// build count
				if (methodInfo.getType().equals(HopeCount.class.getSimpleName())) {
					buildCount(methodInfo, root);
					break;
				}
				
				
				// build insert
				if (methodInfo.getType().equals(HopeInsert.class.getSimpleName())) {
					buildInsert(methodInfo, root);
					break;
				}

			}
		}

	}
	
	
	private void buildInsert(MethodInfo methodInfo, Element root) {

		String insertField = methodInfo.getInsertValue();
		String insertValue = methodInfo.getInsertValue();
		String tableName = methodInfo.getTableName();
		String tableSuffix = methodInfo.getTableSuffix();

		String context = String.format(MapperTagReources.SQL_INSERT, tableName,tableSuffix, insertField,insertValue);
		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_INSERT);
		setElementAttr(element, methodInfo.getId(), null, methodInfo.getReturnType(), context);
	}

	

	private void buildCount(MethodInfo methodInfo, Element root) {

		String selectFields = methodInfo.getSelectCountFields();
		String where = methodInfo.getWhere();
		String groupBy = methodInfo.getGroupBy();
		String orderBy = methodInfo.getOrderBy();
		String tableName = methodInfo.getTableName();
		String tableSuffix = methodInfo.getTableSuffix();

		String context = String.format(MapperTagReources.SQL_SELECT, selectFields, tableName,tableSuffix, where, groupBy, orderBy);
		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_SELECT);
		setElementAttr(element, methodInfo.getId(), null, methodInfo.getReturnType(), context);
	}

	private void buildSelect(MethodInfo methodInfo, Element root) {

		String selectFields = methodInfo.getSelectFields();
		String where = methodInfo.getWhere();
		String groupBy = methodInfo.getGroupBy();
		String orderBy = methodInfo.getOrderBy();
		String tableName = methodInfo.getTableName();
		String tableSuffix = methodInfo.getTableSuffix();

		String sql = String.format(MapperTagReources.SQL_SELECT, selectFields, tableName,tableSuffix, where, groupBy, orderBy);
		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_SELECT);
		setElementAttr(element, methodInfo.getId(), null, methodInfo.getReturnType(), sql);
	}

	private TableInfo getTableInfo(Class<?> daoClz) {
		TableInfo tableInfo = null;
		Table table = (Table) daoClz.getDeclaredAnnotation(Table.class);
		if (null != table) {
			tableInfo = new TableInfo(table);
		}
		return tableInfo;
	}

	private Map<String, DataBaseFieldInfo> getTableColumn(Connection connection, String tableName) {
		Map<String, DataBaseFieldInfo> columns = new HashMap<>();
		String columnName = "";
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, "%");
			while (resultSet.next()) {
				columnName = resultSet.getString(COLUMN_NAME).toLowerCase();
				DataBaseFieldInfo dataBaseFieldInfo = new DataBaseFieldInfo(columnName, resultSet.getString("TYPE_NAME"));
				columns.put(columnName, dataBaseFieldInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}

	private void setElementAttr(Element element, String mapperId, String parameterType, String resultType, String context) {
		if (!StringUtils.isEmpty(mapperId)) {
			element.addAttribute(MapperTagReources.MAPPER_ID, mapperId);
		}
		if (!StringUtils.isEmpty(parameterType)) {
			element.addAttribute(MapperTagReources.MAPPER_PARAMETER_TYPE, parameterType);
		}
		if (!StringUtils.isEmpty(resultType)) {
			element.addAttribute(MapperTagReources.MAPPER_RESULT_TYPE, resultType);
		}
		element.setText(context);
	}
}
