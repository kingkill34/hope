package com.duowan.hope.mybatis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.type.Alias;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import com.duowan.hope.mybatis.annotation.Entity;
import com.duowan.hope.mybatis.database.DataBaseFieldInfo;
import com.duowan.hope.mybatis.initparams.MapperParams;
import com.duowan.hope.mybatis.initparams.SqlField;

public class HopeMappperBuiler {

	private static final String NAME_SPACE = "namespace";
	private static final String UTF_8 = "utf-8";
	private static final String COMMA = ",";
	private static final String COLUMN_NAME = "COLUMN_NAME";

	private List<DataBaseFieldInfo> getTableColumn(Connection connection, String tableName) {
		List<DataBaseFieldInfo> columns = new ArrayList<DataBaseFieldInfo>();
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, "%");
			while (resultSet.next()) {
				DataBaseFieldInfo dataBaseFieldInfo = new DataBaseFieldInfo(resultSet.getString(COLUMN_NAME), resultSet.getString("TYPE_NAME"));
				columns.add(dataBaseFieldInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}

	public InputStream build(String resource, Connection connection) {
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

	public InputStream build(File file, Connection connection) {
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

	private String getTableName(Element root) throws ClassNotFoundException {
		String tableName = "";
		String namespace = root.attributeValue(NAME_SPACE);
		Class daoClz = Class.forName(namespace);
		Entity annotation = (Entity) daoClz.getAnnotation(Entity.class);
		if (annotation != null) {
			Class entityClz = annotation.value();
			Alias AliasClz = (Alias) entityClz.getAnnotation(Alias.class);
			tableName = AliasClz.value();
		}
		return tableName;
	}

	private void buildSqls(Element root, Connection connection) throws ClassNotFoundException {

		String tableName = getTableName(root);
		if (!StringUtils.isEmpty(tableName)) {
			List<DataBaseFieldInfo> columns = getTableColumn(connection, tableName);

			MapperParams mapperParams = getMapperParams(columns, tableName);
			// 构建常用SQL
			bulidGet(root, mapperParams, tableName);
			bulidGetList(root, mapperParams, tableName);
			buildCount(root, mapperParams, tableName);
			buildCountDistinct(root, mapperParams, tableName);
			buildInsert(root, mapperParams, tableName);
			buildInsertByTns(root, mapperParams, tableName);
			buildDelete(root, mapperParams, tableName);
			buildUpdate(root, mapperParams, tableName);

		}

	}

	private void buildBatchInsert() {

	}

	private void buildBatchInsertByTns() {

	}

	private void bulidGet(Element root, MapperParams mapperParams, String tableName) {
		buildGets(root, mapperParams, tableName, MapperTagReources.GET);
	}

	private void bulidGetList(Element root, MapperParams mapperParams, String tableName) {
		buildGets(root, mapperParams, tableName, MapperTagReources.GET_LIST);
	}

	private void buildGets(Element root, MapperParams mapperParams, String tableName, String id) {
		StringBuffer where = new StringBuffer();

		List<SqlField> sqlFieldList = mapperParams.getSqlFidldList();

		for (SqlField sqlField : sqlFieldList) {
			where.append(getWhereSql(sqlField));
		}

		String context = String.format(MapperTagReources.SQL_SELECT, mapperParams.getSelectFields(), tableName, where.toString());
		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_SELECT);
		setElementAttr(element, id, MapperTagReources.MAP, tableName, context);
	}

	private void buildCount(Element root, MapperParams mapperParams, String tableName) {
		buildCounts(root, mapperParams, tableName, MapperTagReources.COUNT, MapperTagReources.SQL_SELECT_COUNT);
	}

	private void buildCountDistinct(Element root, MapperParams mapperParams, String tableName) {
		buildCounts(root, mapperParams, tableName, MapperTagReources.COUNT_DISTINCT, MapperTagReources.SQL_SELECT_DISTINCT_COUNT);
	}

	private void buildCounts(Element root, MapperParams mapperParams, String tableName, String id, String tag) {
		StringBuffer where = new StringBuffer();

		List<SqlField> sqlFieldList = mapperParams.getSqlFidldList();

		for (SqlField sqlField : sqlFieldList) {
			where.append(getWhereSql(sqlField));
		}

		String context = String.format(tag, tableName, where.toString());
		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_SELECT);
		setElementAttr(element, id, MapperTagReources.MAP, MapperTagReources.INTEGER, context);
	}

	private void buildInsert(Element root, MapperParams mapperParams, String tableName) {
		StringBuffer values = new StringBuffer();
		StringBuffer fields = new StringBuffer();

		List<SqlField> sqlFieldList = mapperParams.getSqlFidldList();
		for (SqlField sqlField : sqlFieldList) {
			values.append(getInsertValueSql(sqlField));
			fields.append(getInsertFieldSql(sqlField));
		}

		String context = String.format(MapperTagReources.SQL_INSERT, tableName, fields.toString(), values.toString());
		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_INSERT);
		setElementAttr(element, MapperTagReources.INSERT, tableName, null, context);

	}

	// buildInsertByTableNameSuffixes
	private void buildInsertByTns(Element root, MapperParams mapperParams, String tableName) {
		StringBuffer values = new StringBuffer();
		StringBuffer fields = new StringBuffer();

		List<SqlField> sqlFieldList = mapperParams.getSqlFidldList();
		for (SqlField sqlField : sqlFieldList) {
			values.append(getInsertValueSql(sqlField));
			fields.append(getInsertFieldSql(sqlField));
		}
		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_INSERT);
		String context = String.format(MapperTagReources.SQL_INSERT_BY_TABLENAME_SUFFIXES, tableName, fields.toString(), values.toString());
		setElementAttr(element, MapperTagReources.INSERT_BY_TABLENAME_SUFFIXES, MapperTagReources.MAP, null, context);

	}

	private void buildDelete(Element root, MapperParams mapperParams, String tableName) {
		StringBuffer sb = new StringBuffer();

		List<SqlField> sqlFieldList = mapperParams.getSqlFidldList();
		for (SqlField sqlField : sqlFieldList) {
			sb.append(getWhereSql(sqlField));
		}

		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_DELETE);
		String context = String.format(MapperTagReources.SQL_DELETE, tableName, sb.toString());
		setElementAttr(element, MapperTagReources.DELETE, MapperTagReources.MAP, null, context);
	}

	private void buildUpdate(Element root, MapperParams mapperParams, String tableName) {
		StringBuffer selectFields = new StringBuffer();
		StringBuffer where = new StringBuffer();

		List<SqlField> sqlFieldList = mapperParams.getSqlFidldList();
		for (SqlField sqlField : sqlFieldList) {
			selectFields.append(getUpdateSql(sqlField));
			where.append(getWhereSql(sqlField));
		}

		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_UPDATE);
		String context = String.format(MapperTagReources.SQL_UPDATE, tableName, selectFields.toString(), where.toString());
		setElementAttr(element, MapperTagReources.UPDATE, MapperTagReources.MAP, null, context);
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

	private MapperParams getMapperParams(List<DataBaseFieldInfo> columns, String tableName) {
		List<SqlField> SqlFieldList = null;
		StringBuffer selectFields = null;
		if (null != columns && columns.size() > 0) {
			selectFields = new StringBuffer();
			SqlFieldList = new ArrayList<SqlField>();
			int fieldsLength = columns.size();
			for (int i = 0; i < fieldsLength; i++) {
				DataBaseFieldInfo dataBaseFieldInfo = columns.get(i);
				String fieldName = dataBaseFieldInfo.getFieldName();

				String comma = getComma(fieldsLength, i);
				SqlField sqlField = new SqlField(fieldName, comma, tableName, dataBaseFieldInfo.getFieldType());
				SqlFieldList.add(sqlField);

				selectFields.append(fieldName + comma);
			}
		}

		return new MapperParams(selectFields.toString(), SqlFieldList);
	}

	private String getComma(Integer length, Integer i) {
		String comma = "";
		if (i != length - 1) {
			comma = COMMA;
		}
		return comma;
	}

	private String getInsertFieldSql(SqlField sqlField) {
		String fieleName = sqlField.getFieldName();
		return String.format(MapperTagReources.MAPPER_IF, fieleName, fieleName, sqlField.getFieldNameSource());
	}

	private String getInsertValueSql(SqlField sqlField) {
		String fieleName = sqlField.getFieldName();
		if (sqlField.isNum()) {
			return String.format(MapperTagReources.MAPPER_IF_VALUE, fieleName, fieleName, fieleName);
		} else {
			return String.format(MapperTagReources.MAPPER_IF_VALUE_FOR_NUM, fieleName, fieleName);
		}
	}

	private String getWhereSql(SqlField sqlField) {
		String whereFieldName = sqlField.getWhereFieldName();
		if (sqlField.isNum()) {
			return String.format(MapperTagReources.MAPPER_WHERE_IF_FOR_NUM, whereFieldName, sqlField.getFieldNameSource(), sqlField.getWhereOperation(), whereFieldName);
		} else {
			return String.format(MapperTagReources.MAPPER_WHERE_IF, whereFieldName, whereFieldName, sqlField.getFieldNameSource(), sqlField.getWhereOperation(), whereFieldName);
		}
	}

	private String getUpdateSql(SqlField sqlField) {
		String fieldName = sqlField.getFieldName();
		if (sqlField.isNum()) {
			return String.format(MapperTagReources.MAPPER_UPDATE_IF_FOR_NUM, fieldName, sqlField.getFieldNameSource(), fieldName);
		} else {
			return String.format(MapperTagReources.MAPPER_UPDATE_IF, fieldName, fieldName, sqlField.getFieldNameSource(), fieldName);
		}
	}

}
