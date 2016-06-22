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

	private TypeAliasRegistry typeAliasRegistry; // mybatis 返回实体类注册
	public static final Set<String> CONSTANT = new HashSet<String>(); // 基础类型

	private List<DataBaseFieldInfo> dataBaseFieldInfoList; // 组装SQL需要用到的字段

	private String id; // 对应mybatis 方法的ID

	private String value;

	private String returnType; // 方法返回类型

	private String type; // 注解类型

	private Class<?> returnClass;

	private String orderByASC;

	private String orderByDESC;

	private String groupBy;

	// 表名相关信息
	private TableInfo tableInfo;

	// private List<Parameter> parameters;

	static {
		CONSTANT.add(Integer.class.getName());
		CONSTANT.add(Double.class.getName());
		CONSTANT.add(String.class.getName());
		CONSTANT.add(HashMap.class.getName());
		CONSTANT.add(Boolean.class.getName());
		CONSTANT.add(double.class.getName());
		CONSTANT.add(int.class.getName());
		CONSTANT.add(boolean.class.getName());
	}

	/**
	 * 组装拼装SQL所需要的元素
	 * 
	 * @param method
	 * @param annotation
	 * @param columns
	 * @param typeAliasRegistry
	 * @param tableInfo
	 */
	public MethodInfo(Method method, Annotation annotation, Map<String, DataBaseFieldInfo> columns, TypeAliasRegistry typeAliasRegistry, TableInfo tableInfo) {
		this.typeAliasRegistry = typeAliasRegistry;
		setAnnotationInfo(annotation);
		setType(annotation);
		setDataBaseFieldInfoList(method, columns);
		this.id = method.getName();
		this.returnType = getReturnType(method);
		// table info
		this.tableInfo = tableInfo;
	}

	/**
	 * 过滤参数，必须要跟表的字段对应上
	 * 
	 * @param parameters
	 */
	private void setDataBaseFieldInfoList(Method method, Map<String, DataBaseFieldInfo> columns) {
		boolean isEntity = false;
		dataBaseFieldInfoList = new ArrayList<>();
		Parameter[] parameters = method.getParameters();
		Integer parametersLength = parameters == null ? 0 : parameters.length;

		// 如果参数只有一个,考虑不是基础类型
		if (parametersLength == 1) {
			// 获取参数类型
			String typeName = parameters[0].getParameterizedType().getTypeName();
			// 判断是否是LIST
			if (typeName.startsWith(List.class.getName())) {
				// 如果是list 获取泛型里面的参数类型
				typeName = getGenericityName(typeName);
			}

			if (this.tableInfo.getTypeName().equals(typeName)) {
				isEntity = true;
				for (Map.Entry<String, DataBaseFieldInfo> entry : columns.entrySet()) {
					dataBaseFieldInfoList.add(entry.getValue());
				}
			}
		}

		// 如果不是实体类
		if (isEntity == false) {
			for (Parameter parameter : parameters) {
				String fieldName = parameter.getName();
				OP op = parameter.getAnnotation(OP.class);
				if (columns.containsKey(fieldName)) {
					DataBaseFieldInfo dataBaseFieldInfo = columns.get(fieldName);
					dataBaseFieldInfo.setOp(op);
					dataBaseFieldInfoList.add(dataBaseFieldInfo);
				}
			}

		}
	}

	public String getSelectFields() {
		StringBuffer selectFields = new StringBuffer();
		int fieldsLength = dataBaseFieldInfoList.size();
		for (int i = 0; i < fieldsLength; i++) {
			DataBaseFieldInfo dataBaseFieldInfo = dataBaseFieldInfoList.get(i);
			selectFields.append(dataBaseFieldInfo.getSelectField(fieldsLength, i));
		}
		setGroupBy(selectFields);
		return selectFields.toString();
	}

	public String getSelectCountFields() {
		StringBuffer selectFields = new StringBuffer();
		String distinctStr = "";
		String fieldFormat = "count(" + distinctStr + " " + FieldUtil.toUnderlineName(value) + ")" + " as " + value;
		selectFields.append(fieldFormat);

		setGroupBy(selectFields);

		return selectFields.toString();
	}

	public String getInsertField() {
		StringBuffer insertFields = new StringBuffer();
		if (null != dataBaseFieldInfoList && dataBaseFieldInfoList.size() > 0) {
			int fieldsLength = dataBaseFieldInfoList.size();
			for (int i = 0; i < fieldsLength; i++) {
				DataBaseFieldInfo dataBaseFieldInfo = dataBaseFieldInfoList.get(i);
				if (dataBaseFieldInfo.isPrimaryKey() == false && dataBaseFieldInfo.isAutoincrement() == false) {
					String fieldName = dataBaseFieldInfo.getFieldName();
					String comma = getComma(fieldsLength, i);
					insertFields.append(fieldName + comma);
				}
			}
		}
		return insertFields.toString();
	}

	public String getInsertValue() {
		// String ifNull = "<if test=\"#{%s} = null \"> %s</if>";
		// String ifNotNull = "<if test=\"#{%s} != null \"> %s</if>";
		// StringBuffer insertValue = new StringBuffer();
		//
		// String fieldName = "";
		//
		// int parametersLength = parameters.size();
		//
		// if (parametersLength == 1 && isSameClzType(parameters.get(0))) {
		// int fieldsLength = dataBaseFieldInfoList.size();
		// for (int i = 0; i < fieldsLength; i++) {
		// DataBaseFieldInfo dataBaseFieldInfo = dataBaseFieldInfoList.get(i);
		// if (dataBaseFieldInfo.isPrimaryKey() == false &&
		// dataBaseFieldInfo.isAutoincrement() == false) {
		// fieldName = dataBaseFieldInfo.getFieldName();
		// String comma = getComma(fieldsLength, i);
		// }
		// }
		// }
		//
		// for (int i = 0; i < parametersLength; i++) {
		// Parameter parameter = parameters.get(i);
		// fieldName = parameter.getName();
		//
		// setTableInfoIndex(fieldName, i);
		//
		// fieldName = FieldUtil.toUnderlineName(fieldName).toLowerCase();
		// if (columns.containsKey(fieldName)) {
		//
		// DataBaseFieldInfo dataBaseFieldInfo = columns.get(fieldName);
		//
		// if (dataBaseFieldInfo.isInsert()) {
		//
		// boolean isNullAble = dataBaseFieldInfo.isNullAble();
		// String defaultValue = dataBaseFieldInfo.getDefaultValue();
		// String insertField = getWhereValue(parameter, i, parametersLength,
		// i);
		//
		// if (!isNullAble && !StringUtils.isEmpty(defaultValue)) {// 不允许空，有默认值
		// insertValue.append(String.format(ifNull, i, insertField));
		// insertValue.append(String.format(ifNotNull, i, insertField));
		// } else {// 插入字段值允许空 // 插入字段值不允许空，也没有默认值 //两种情况
		// insertValue.append(insertField);
		// }
		//
		// }
		// }
		// }
		// return insertValue.toString();
		return "";

	}

	private void setGroupBy(StringBuffer selectFields) {
		if (selectFields.length() > 0 && !StringUtils.isEmpty(groupBy)) {
			selectFields.append(",");
			selectFields.append(FieldUtil.toUnderlineName(groupBy) + " as " + groupBy);
		}
	}

	public String getType() {
		return type;
	}

	private void setType(Annotation annotation) {
		if (null != annotation) {
			this.type = annotation.annotationType().getSimpleName();
		}
	}

	/**
	 * 获取操作符
	 * 
	 * @param parameter
	 * @return
	 */
	private String getWhereValue(Parameter parameter, Object field, Integer fieldLength, Integer i) {
		String comma = getComma(fieldLength, i);

		String value = "#{" + field + "}" + comma;
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
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			fieldName = parameter.getName();

			setTableInfoIndex(fieldName, i);

			fieldName = FieldUtil.toUnderlineName(fieldName).toLowerCase();
			if (columns.containsKey(fieldName)) {
				String whereValue = getWhereValue(parameter, i, null, null);
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

	/**
	 * 获取方法返回类型
	 * 
	 * @param method
	 * @return
	 */
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
