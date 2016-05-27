package com.duowan.hope.mybatis;

public class MapperTagReources {

	// method name
	public static final String GET = "getH";

	public static final String GET_LIST = "getListH";

	public static final String INSERT = "insertH";

	public static final String INSERT_BY_TABLENAME_SUFFIXES = "insertByTnsH";

	public static final String DELETE = "deleteH";

	public static final String UPDATE = "updateH";

	public static final String COUNT = "countH";

	public static final String COUNT_DISTINCT = "countDistinctH";

	// mybatis tag
	public static final String ELEMENT_TYPE_SELECT = "select";

	public static final String ELEMENT_TYPE_INSERT = "insert";

	public static final String ELEMENT_TYPE_UPDATE = "update";

	public static final String ELEMENT_TYPE_DELETE = "delete";

	public static final String MAPPER_ID = "id";

	public static final String MAPPER_PARAMETER_TYPE = "parameterType";

	public static final String MAPPER_RESULT_TYPE = "resultType";

	public static final String MAPPER_IF = "<if test=\"%s != null and %s !='' \"> %s,</if>";

	public static final String MAPPER_IF_FOR_NUM = "<if test=\"%s != null \"> %s,</if>";

	public static final String MAPPER_IF_VALUE = "<if test=\"%s != null and %s !='' \"> #{%s},</if>";

	public static final String MAPPER_IF_VALUE_FOR_NUM = "<if test=\"%s != null \"> #{%s},</if>";

	public static final String MAPPER_IF_EQ = "<if test=\"%s != null and %s !='' \"> %s = #{%s},</if>";

	public static final String MAPPER_IF_EQ_FOR_NUM = "<if test=\"%s != null \"> %s = #{%s},</if>";

	public static final String MAPPER_WHERE_IF = "<if test=\"%s != null and %s !='' \"> AND %s ${%s} #{%s}</if>";

	public static final String MAPPER_WHERE_IF_FOR_NUM = "<if test=\"%s != null \"> AND %s ${%s} #{%s}</if>";

	public static final String MAPPER_UPDATE_IF = "<if test=\"%s != null and %s !='' \">%s = #{%s},</if>";

	public static final String MAPPER_UPDATE_IF_FOR_NUM = "<if test=\"%s != null \">%s = #{%s},</if>";

	// java data type
	public static final String INTEGER = "Integer";

	public static final String INT = "int";

	public static final String STRING = "String";

	public static final String MAP = "Map";

	// sql
	public static final String SQL_SELECT = "SELECT %s FROM %s${tableNameSuffixes} <where>%s</where>";

	public static final String SQL_SELECT_COUNT = "SELECT count(1) FROM %s${tableNameSuffixes} <where>%s</where>";

	public static final String SQL_SELECT_DISTINCT_COUNT = "SELECT count(distinct ${distinctField}) FROM %s${tableNameSuffixes} <where>%s</where>";

	public static final String SQL_INSERT = "INSERT INTO %s (<trim suffixOverrides=\",\"> %s</trim>) VALUES(<trim suffixOverrides=\",\">%s</trim>)";

	public static final String SQL_INSERT_BY_TABLENAME_SUFFIXES = "INSERT INTO %s${tableNameSuffixes} (<trim suffixOverrides=\",\"> %s</trim>) VALUES(<trim suffixOverrides=\",\">%s</trim>)";

	public static final String SQL_BATCH_INSERT = "INSERT INTO %s (<trim suffixOverrides=\",\"> %s</trim>) VALUES(<trim suffixOverrides=\",\">%s</trim>)";
	
	public static final String SQL_DELETE = "DELETE FROM %s${tableNameSuffixes} <where>%s</where>";

	public static final String SQL_UPDATE = "UPDATE %s${tableNameSuffixes} <set>%s</set> <where>%s</where>";

}
