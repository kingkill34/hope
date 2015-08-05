package com.inng.hope.mybatis;

public class MapperTagReources {

	//method name
	public static final String GET = "get";
	
	public static final String INSERT = "insert";
	
	public static final String DELETE = "delete";
	
	public static final String UPDATE = "update";
	
	
	//mybatis tag
	public static final String ELEMENT_TYPE_SELECT = "select";
	
	public static final String ELEMENT_TYPE_INSERT = "insert";
	
	public static final String ELEMENT_TYPE_UPDATE = "update";
	
	public static final String ELEMENT_TYPE_DELETE = "delete";
	
	
	
	
	public static final String MAPPER_ID = "id";
	
	public static final String MAPPER_PARAMETER_TYPE = "parameterType";
	
	public static final String MAPPER_RESULT_TYPE= "resultType";
	
	public static final String MAPPER_IF= "<if test=\"%s != null and %s !='' \"> %s = #{%s}%s</if>";
	
	
	//java data type
	public static final String INTEGER = "Integer";

	public static final String INT = "int";
	
	public static final String STRING = "String";
	
	
	//sql
	public static final String SQL_SELECT = "SELECT %s FROM %s WHERE id=#{id}";
	
	public static final String SQL_INSERT = "INSERT INTO %s <set>%s</set>";
	
	public static final String SQL_UPDATE = "";

	
}
