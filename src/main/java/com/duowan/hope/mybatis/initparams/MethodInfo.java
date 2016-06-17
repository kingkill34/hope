package com.duowan.hope.mybatis.initparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.type.TypeAliasRegistry;
import org.springframework.util.StringUtils;

import com.duowan.hope.mybatis.MapperTagReources;
import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.mybatis.database.DataBaseFieldInfo;
import com.duowan.hope.mybatis.util.FieldUtil;

public class MethodInfo {

	private TypeAliasRegistry typeAliasRegistry;
	public static final Set<String> CONSTANT = new HashSet<String>();
	private static final Set<String> CONSTANT_SET = new HashSet<String>();

	private List<DataBaseFieldInfo> dataBaseFieldInfoList;

	private Map<String, DataBaseFieldInfo> columns;

	private String id;

	private String value;

	private String returnType;

	private String type;

	private Class<?> returnClass;

	private String orderByASC;

	private String orderByDESC;

	private String groupBy;

	private boolean distinct;

	// 表名相关信息
	private TableInfo tableInfo;

	private Parameter[] parameters;

	static {

		CONSTANT.add(Integer.class.getName());
		CONSTANT.add(Double.class.getName());
		CONSTANT.add(String.class.getName());
		CONSTANT.add(HashMap.class.getName());
		CONSTANT.add(Boolean.class.getName());
		CONSTANT.add(double.class.getName());
		CONSTANT.add(int.class.getName());
		CONSTANT.add(boolean.class.getName());

		CONSTANT_SET.add(List.class.getName());
		CONSTANT_SET.add(Map.class.getName());
	}

	public MethodInfo(Method method, Annotation annotation, Map<String, DataBaseFieldInfo> columns, TypeAliasRegistry typeAliasRegistry, TableInfo tableInfo) {
		this.typeAliasRegistry = typeAliasRegistry;
		this.columns = columns;
		setAnnotationInfo(annotation);
		setType(annotation);
		this.id = method.getName();
		this.parameters = method.getParameters();
		this.returnType = getReturnType(method);

		// table info
		this.tableInfo = tableInfo;
	}

	public String getSelectFields() {
		StringBuffer selectFields = new StringBuffer();
		if (null != dataBaseFieldInfoList && dataBaseFieldInfoList.size() > 0) {
			int fieldsLength = dataBaseFieldInfoList.size();
			for (int i = 0; i < fieldsLength; i++) {
				DataBaseFieldInfo dataBaseFieldInfo = dataBaseFieldInfoList.get(i);
				String fieldName = dataBaseFieldInfo.getFieldName();
				String comma = getComma(fieldsLength, i);
				selectFields.append(fieldName + " as " + FieldUtil.toCamelCase(fieldName) + comma);
			}
		}
		setGroupBy(selectFields);
		return selectFields.toString();
	}

	public String getSelectCountFields() {
		StringBuffer selectFields = new StringBuffer();
		String distinctStr = "";
		if (this.distinct) {
			distinctStr = "distinct";
		}
		String fieldFormat = "count(" + distinctStr + " " + FieldUtil.toUnderlineName(value) + ")" + " as " + value;
		selectFields.append(fieldFormat);

		setGroupBy(selectFields);

		return selectFields.toString();
	}

	public String getInsertField() {
		StringBuffer selectFields = new StringBuffer();
		if (null != dataBaseFieldInfoList && dataBaseFieldInfoList.size() > 0) {
			int fieldsLength = dataBaseFieldInfoList.size();
			for (int i = 0; i < fieldsLength; i++) {
				DataBaseFieldInfo dataBaseFieldInfo = dataBaseFieldInfoList.get(i);
				String fieldName = dataBaseFieldInfo.getFieldName();
				String comma = getComma(fieldsLength, i);
				if (!dataBaseFieldInfo.isPrimaryKey() && !dataBaseFieldInfo.isAutoincrement()) {
					selectFields.append(fieldName + comma);
				}
			}
		}
		return selectFields.toString();
	}
	
	

	public String getInsertValue() {
		String ifNull = "<if test=\"#{%s} = null \"> %s</if>";
		String ifNotNull = "<if test=\"#{%s} != null \"> %s</if>";
		StringBuffer insertValue = new StringBuffer();

		String fieldName = "";
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			fieldName = parameter.getName();

			setTableInfoIndex(fieldName, i);

			fieldName = FieldUtil.toUnderlineName(fieldName).toLowerCase();
			if (columns.containsKey(fieldName)) {

				DataBaseFieldInfo dataBaseFieldInfo = columns.get(fieldName);

				if (dataBaseFieldInfo.isInsert()) {
					String comma = getComma(parameters.length, i);
					boolean isNullAble = dataBaseFieldInfo.isNullAble();
					String defaultValue = dataBaseFieldInfo.getDefaultValue();
					String insertField = getValue(parameter, i);

					if (!isNullAble && !StringUtils.isEmpty(defaultValue)) {// 不允许空，有默认值
						insertValue.append(String.format(ifNull, i, insertField + comma));
						insertValue.append(String.format(ifNotNull, i, insertField + comma));
					} else {// 插入字段值允许空 // 插入字段值不允许空，也没有默认值 //两种情况
						insertValue.append(insertField + comma);
					}

				}
			}
		}
		return insertValue.toString();

	}

	private void setGroupBy(StringBuffer selectFields) {
		if (selectFields.length() > 0 && !StringUtils.isEmpty(groupBy)) {
			selectFields.append(",");
			selectFields.append(FieldUtil.toUnderlineName(groupBy) + " as " + groupBy);
		}
	}

	private String getComma(Integer length, Integer i) {
		String comma = "";
		if (i != length - 1) {
			comma = ",";
		}
		return comma;
	}

	public String getType() {
		return type;
	}

	private void setType(Annotation annotation) {
		if (null != annotation) {
			this.type = annotation.annotationType().getSimpleName();
		}
	}

	private void setAnnotationInfo(Annotation annotation) {
		if (null != annotation) {
			Method[] methods = annotation.annotationType().getMethods();
			for (Method method : methods) {
				try {
					switch (method.getName()) {
					case "value":
						this.value = method.invoke(annotation, null).toString();
						break;
					case "groupBy":
						this.groupBy = method.invoke(annotation, null).toString();
						break;
					case "orderByASC":
						this.orderByASC = method.invoke(annotation, null).toString();
						break;
					case "orderByDESC":
						this.orderByDESC = method.invoke(annotation, null).toString();
						break;
					case "distinct":
						this.distinct = (Boolean) method.invoke(annotation, null);
						break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	}

	public String getWhere() {
		StringBuffer where = new StringBuffer();

		String fieldName = "";
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			fieldName = parameter.getName();

			setTableInfoIndex(fieldName, i);

			fieldName = FieldUtil.toUnderlineName(fieldName).toLowerCase();
			if (columns.containsKey(fieldName)) {
				String whereValue = getValue(parameter, i);
				String appendSql = "AND " + FieldUtil.toUnderlineName(fieldName) + whereValue;
				where.append(appendSql);
			}
		}
		return where.toString();
	}

	public String getTableSuffix() {
		String tableSuffix = "";
		Integer index = this.tableInfo.getIndex();
		if (null != index && index > -1) {
			String indexStr = "#{" + this.tableInfo.getIndex() + "}";
			tableSuffix = this.tableInfo.getTableSeparator() + indexStr;
			tableSuffix = String.format(MapperTagReources.MAPPER_TABLE_SUFFIX, indexStr, tableSuffix);
		}
		return tableSuffix;
	}

	/**
	 * 获取分表的字段属性下标
	 * 
	 * @param fieldName
	 * @param index
	 */
	private void setTableInfoIndex(String fieldName, int index) {
		String tableSuffix = this.tableInfo.getTableSuffix();
		if (fieldName.equals(tableSuffix) && !StringUtils.isEmpty(tableSuffix)) {
			this.tableInfo.setIndex(index);
		}
	}

	/**
	 * 获取操作符
	 * 
	 * @param parameter
	 * @return
	 */
	private String getValue(Parameter parameter, int index) {
		String value = "#{" + index + "}";
		String whereValue = "=" + value;
		OP op = parameter.getAnnotation(OP.class);
		if (op != null) {
			if (!op.value().equals("")) {
				whereValue = op.value() + value;
			}

			if (op.isNull()) {
				whereValue = " is null";
			}

			if (op.isNotNull()) {
				whereValue = " is not null";
			}

		}
		return whereValue;
	}

	public String getReturnType() {
		return returnType;
	}

	public Class<?> getReturnClass() {
		return returnClass;
	}

	public String getOrderBy() {
		String orderBy = getOrderBy(this.orderByASC, "ASC");
		if (StringUtils.isEmpty(orderBy)) {
			orderBy = getOrderBy(this.orderByDESC, "DESC");
		}
		return orderBy;
	}

	private String getOrderBy(String orderByFields, String sort) {
		String orderBy = "";
		if (!StringUtils.isEmpty(orderByFields)) {
			orderBy = "order by " + FieldUtil.toUnderlineName(orderByFields) + " " + sort;
		}
		return orderBy;
	}

	public String getGroupBy() {
		if (!StringUtils.isEmpty(groupBy)) {
			groupBy = "group by " + FieldUtil.toUnderlineName(groupBy);
		}
		return groupBy;
	}

	public String getId() {
		return id;
	}

	public List<DataBaseFieldInfo> getDataBaseFieldInfoList() {
		return dataBaseFieldInfoList;
	}

	private Class<?> registerAlias(String className) {
		Class<?> clz = null;
		try {
			clz = Class.forName(className);
			this.typeAliasRegistry.registerAlias(clz.getSimpleName(), clz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clz;
	}

	private String getReturnType(Method method) {
		String resultType = method.getReturnType().getName();

		// 如果是List 获取泛型类型
		// 如果是MAP,去掉泛型
		if (resultType.equals(List.class.getName())) {
			String genericity = method.getGenericReturnType().getTypeName();
			resultType = getGenericityName(genericity);
			if (resultType.startsWith(Map.class.getName())) {
				resultType = Map.class.getName();
			}

		}

		// 如果是MAP转换成hashmap
		if (resultType.equals(Map.class.getName())) {
			resultType = HashMap.class.getName();

			dataBaseFieldInfoList = new ArrayList<DataBaseFieldInfo>();
			// 如果返回值为MAP,并且没有查找字段,默认返回这个表的所有字段
			if (StringUtils.isEmpty(value)) {
				for (Map.Entry<String, DataBaseFieldInfo> entry : columns.entrySet()) {
					dataBaseFieldInfoList.add(entry.getValue());
				}
			} else {
				String[] v = value.split(",");
				for (String fieldName : v) {
					fieldName = FieldUtil.toUnderlineName(fieldName);
					dataBaseFieldInfoList.add(columns.get(fieldName));
				}

			}
		}else{
			
		}

		if (!CONSTANT.contains(resultType)) {
			Class<?> clz = registerAlias(resultType);
			dataBaseFieldInfoList = getColumns(clz);
		}

		return resultType;
	}

	private List<DataBaseFieldInfo> getColumns(Class<?> clz) {
		String fieldName = "";
		List<DataBaseFieldInfo> list = null;
		Field[] fields = clz.getDeclaredFields();
		if (null != fields && fields.length > 0) {
			list = new ArrayList<DataBaseFieldInfo>();
			for (Field field : fields) {
				fieldName = FieldUtil.toUnderlineName(field.getName()).toLowerCase();
				if (columns.containsKey(fieldName)) {
					list.add(columns.get(fieldName));
				}
			}
		}
		return list;
	}

	private String getGenericityName(String genericity) {
		String genericityName = genericity;
		int startIndex = genericity.indexOf("<");
		int endIndex = genericity.lastIndexOf(">");
		if (-1 != startIndex && -1 != endIndex) {
			genericityName = genericity.substring(startIndex + 1, endIndex);
		}
		return genericityName;
	}

	public String getTableName() {
		return this.tableInfo.getTableName();
	}

}
