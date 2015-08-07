package com.inng.hope.framework.mybatis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

import com.inng.hope.framework.mybatis.annotation.Entity;

public class HopeMappperBuiler {

	public InputStream build(String resource, TypeAliasRegistry typeAliasRegistry) {
		InputStream inputStream = null;
		try {
			// 加载*mapper.xml
			String xmlPath = this.getClass().getClassLoader().getResource("").getPath() + resource;
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(xmlPath));
			Element root = document.getRootElement();

			// 读取mapper namespace
			String namespace = root.attributeValue("namespace");
			Class daoClz = Class.forName(namespace);
			Entity annotation = (Entity) daoClz.getAnnotation(Entity.class);
			if (annotation != null) {
				// 获取Dao
				String entityClzName = annotation.value();
				Class entityClz = typeAliasRegistry.resolveAlias(entityClzName);

				bulidGet(root, entityClz.getDeclaredFields(), entityClzName);
				bulidGetList(root, entityClz.getDeclaredFields(), entityClzName);
				buildInsert(root, entityClz.getDeclaredFields(),entityClzName);
				buildDelete(root, entityClz.getDeclaredFields(), entityClzName);
			}

			inputStream = new ByteArrayInputStream(StringEscapeUtils.unescapeXml(document.asXML()).getBytes("utf-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	private void bulidGet(Element root, Field[] fields, String tableName) {
		buildGets(root, fields, tableName, MapperTagReources.GET);
	}

	private void bulidGetList(Element root, Field[] fields, String tableName) {
		buildGets(root, fields, tableName, MapperTagReources.GET_LIST);
	}

	private void buildGets(Element root, Field[] fields, String tableName, String id) {
		StringBuffer selectFields = new StringBuffer();
		StringBuffer where = new StringBuffer();

		int fieldsLength = fields.length;
		for (int i = 0; i < fieldsLength; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String comma = "";
			if (i != fieldsLength - 1) {
				comma = ",";
			}
			selectFields.append(fieldName + comma);

			String operation = "operation" + fieldName;
			where.append(String.format(MapperTagReources.MAPPER_WHERE_IF, fieldName, fieldName, fieldName, operation, fieldName));
		}

		String context = String.format(MapperTagReources.SQL_SELECT, selectFields.toString(), tableName, where.toString());

		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_SELECT);
		setElementAttr(element, id, MapperTagReources.MAP, tableName, context);
	}

	private void buildInsert(Element root, Field[] fields, String tableName) {
		StringBuffer sb = new StringBuffer();

		int fieldsLength = fields.length;
		for (int i = 0; i < fieldsLength; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String comma = "";
			if (i != fieldsLength - 1) {
				comma = ",";
			}
			sb.append(String.format(MapperTagReources.MAPPER_IF, fieldName, fieldName, fieldName, fieldName, comma));

		}

		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_INSERT);
		String context = String.format(MapperTagReources.SQL_INSERT, tableName, sb.toString());
		setElementAttr(element, MapperTagReources.INSERT, tableName, null, context);

	}

	private void buildDelete(Element root, Field[] fields, String tableName) {
		StringBuffer sb = new StringBuffer();

		int fieldsLength = fields.length;
		for (int i = 0; i < fieldsLength; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String operation = "operation" + fieldName;
			sb.append(String.format(MapperTagReources.MAPPER_WHERE_IF, fieldName, fieldName, fieldName, operation, fieldName));
		}
		
		Element element = root.addElement(MapperTagReources.ELEMENT_TYPE_DELETE);
		String context = String.format(MapperTagReources.SQL_DELETE, tableName, sb.toString());
		setElementAttr(element, MapperTagReources.DELETE, MapperTagReources.MAP, null, context);
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
