package com.inng.hope.mybatis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Field;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.inng.hope.mybatis.annotation.Entity;

public class HopeMappperBuiler {
	
	
	

	public InputStream build(String resource,TypeAliasRegistry typeAliasRegistry){
		InputStream inputStream = null;
		try {
			//加载*mapper.xml
			String xmlPath = this.getClass().getClassLoader().getResource("").getPath()+resource;
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(xmlPath));
			Element root = document.getRootElement();
			
			
			
			//读取mapper namespace
			String namespace = root.attributeValue("namespace");
			Class daoClz = Class.forName(namespace);
			Entity annotation= (Entity)daoClz.getAnnotation(Entity.class);
			if(annotation!= null){
				//获取Dao
				String entityClzName = annotation.value();
				Class entityClz = typeAliasRegistry.resolveAlias(entityClzName);
				
				
				bulidSelect(root,entityClz.getDeclaredFields(),entityClzName);
				buildInsert(root, entityClz.getDeclaredFields(), entityClzName);
			}
			
			
			inputStream = new ByteArrayInputStream(StringEscapeUtils.unescapeXml(document.asXML()).getBytes("utf-8"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return inputStream;
	}
	
	
	private void bulidSelect(Element root,Field[] fields,String tableName){
		Element selectElement = root.addElement(MapperTagReources.ELEMENT_TYPE_SELECT);
		
		selectElement.addAttribute(MapperTagReources.MAPPER_ID, MapperTagReources.GET);
		selectElement.addAttribute(MapperTagReources.MAPPER_PARAMETER_TYPE, MapperTagReources.INTEGER);
		selectElement.addAttribute(MapperTagReources.MAPPER_RESULT_TYPE, tableName);
		
		StringBuffer sb = new StringBuffer();
		
		int fieldsLength = fields.length;
		for(int i = 0;i<fieldsLength;i++){
			Field field = fields[i];
			sb.append(field.getName());
			if(i!=fieldsLength-1){
				sb.append(",");
			}
		}
		
		selectElement.setText(String.format(MapperTagReources.SQL_SELECT, sb.toString(),tableName));
	}
	
	
	
	private void buildInsert(Element root,Field[] fields,String tableName){
		Element selectElement = root.addElement(MapperTagReources.ELEMENT_TYPE_INSERT);
		selectElement.addAttribute(MapperTagReources.MAPPER_ID, MapperTagReources.INSERT);
		selectElement.addAttribute(MapperTagReources.MAPPER_PARAMETER_TYPE, tableName);
		
		StringBuffer sb = new StringBuffer();
		
		int fieldsLength = fields.length;
		for(int i = 0;i<fieldsLength;i++){
			Field field = fields[i];
			String fieldName =field.getName();
			String  comma = "";
			if(i!=fieldsLength-1){
				comma = ",";
			}
			sb.append(String.format(MapperTagReources.MAPPER_IF, fieldName,fieldName,fieldName,fieldName,comma));
			
		}
		selectElement.setText(String.format(MapperTagReources.SQL_INSERT, tableName,sb.toString()));
	}
	
	
	private void buildDelete(Element root,Field[] fields,String tableName){
		Element selectElement = root.addElement(MapperTagReources.ELEMENT_TYPE_DELETE);
		selectElement.addAttribute(MapperTagReources.MAPPER_ID, MapperTagReources.UPDATE);
		selectElement.addAttribute(MapperTagReources.MAPPER_PARAMETER_TYPE, tableName);
		
		StringBuffer sb = new StringBuffer();
		
		int fieldsLength = fields.length;
		for(int i = 0;i<fieldsLength;i++){
			Field field = fields[i];
			String fieldName =field.getName();
			String  comma = "";
			if(i!=fieldsLength-1){
				comma = ",";
			}
			sb.append(String.format(MapperTagReources.MAPPER_IF, fieldName,fieldName,fieldName,fieldName,comma));
			
		}
		selectElement.setText(String.format(MapperTagReources.SQL_INSERT, tableName,sb.toString()));
	}
	
	
	
}
