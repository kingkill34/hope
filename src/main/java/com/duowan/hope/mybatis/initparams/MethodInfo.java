package com.duowan.hope.mybatis.initparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.ibatis.type.TypeAliasRegistry;
import org.springframework.util.StringUtils;

import com.duowan.hope.mybatis.MapperTagReources;
import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.mybatis.database.DataBaseFieldInfo;
import com.duowan.hope.mybatis.util.FieldUtil;

public class MethodInfo {

	private TypeAliasRegistry typeAliasRegistry; // mybatis 返回实体类注册
	public static final Set<String> BASE_TYPE = new HashSet<String>(); // 基础类型

	public static final Map<String, String> COLLECTION_TYPE = new HashMap<String, String>(); // 基础类型

	private List<DataBaseFieldInfo> dataBaseFieldInfoList; // 组装SQL需要用到的字段

	private String id; // 对应mybatis 方法的ID

	private String value;

	private String returnType; // 方法返回类型

	private String type; // 注解类型

	private Class<?> returnClass;

	private String orderByASC;

	private String orderByDESC;

	private String groupBy;

	private String paramterType;

	private boolean distinct;

	private boolean isVoOrPo;

	boolean isCollection;

	// 表名相关信息
	private TableInfo tableInfo;

	static {
		BASE_TYPE.add(Integer.class.getName());
		BASE_TYPE.add(Double.class.getName());
		BASE_TYPE.add(String.class.getName());
		BASE_TYPE.add(HashMap.class.getName());
		BASE_TYPE.add(Boolean.class.getName());
		BASE_TYPE.add(double.class.getName());
		BASE_TYPE.add(int.class.getName());
		BASE_TYPE.add(boolean.class.getName());

		COLLECTION_TYPE.put(List.class.getName(), List.class.getName());
		COLLECTION_TYPE.put(ArrayList.class.getName(), List.class.getName());
		COLLECTION_TYPE.put(LinkedList.class.getName(), List.class.getName());
		COLLECTION_TYPE.put(Map.class.getName(), Map.class.getName());
		COLLECTION_TYPE.put(HashMap.class.getName(), Map.class.getName());
		COLLECTION_TYPE.put(LinkedHashMap.class.getName(), Map.class.getName());
		COLLECTION_TYPE.put(TreeMap.class.getName(), Map.class.getName());

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
		this.tableInfo = tableInfo;
		this.id = method.getName();
		setAnnotationInfo(annotation);
		setType(annotation);
		this.returnType = getReturnType(method);
		setDataBaseFieldInfoList(method, columns);
	}

	private RegisterAliasInfo setParamterType(String typeName) {

		RegisterAliasInfo RegisterAliasInfo = null;

		String paramPrefix = getParamterType(typeName);
		// 如果是集合
		if (COLLECTION_TYPE.containsKey(paramPrefix)) {
			isCollection = true;
			this.paramterType = COLLECTION_TYPE.get(paramPrefix);
		}

		// 如果不是基础类型,那么进行注册，并获取参数类型
		String paramSuffix = getGenericityName(typeName);
		if (!BASE_TYPE.contains(paramSuffix)) {
			isVoOrPo = true;
			RegisterAliasInfo = registerAlias(paramSuffix);
			if (!isCollection) {
				this.paramterType = RegisterAliasInfo.getName();
			}
		}
		return RegisterAliasInfo;

	}

	/**
	 * 过滤参数，必须要跟表的字段对应上
	 * 
	 * @param parameters
	 */
	private void setDataBaseFieldInfoList(Method method, Map<String, DataBaseFieldInfo> columns) {
		dataBaseFieldInfoList = new ArrayList<>();
		Parameter[] parameters = method.getParameters();
		Integer parametersLength = parameters == null ? 0 : parameters.length;

		// 如果参数只有一个,考虑不是基础类型
		if (parametersLength == 1) {
			// 获取参数类型
			String typeName = parameters[0].getParameterizedType().getTypeName();

			// 设置传入方法类型
			RegisterAliasInfo registerAliasInfo = setParamterType(typeName);

			// 如果是VO或者PO，把字段取出来当参数,
			// 一般来说会用VO PO做参数的，只会是插入数据或者更新
			// 取出对象的字段，和数据库中的字段进行对比，匹配的拿来做SQL的参数
			if (isVoOrPo) {
				Field[] fields = registerAliasInfo.getClz().getDeclaredFields();
				for (Field field : fields) {
					String underLineFieldName = FieldUtil.toUnderlineName(field.getName());
					if (columns.containsKey(underLineFieldName)) {
						DataBaseFieldInfo dataBaseFieldInfo = columns.get(underLineFieldName);
						dataBaseFieldInfoList.add(dataBaseFieldInfo);
					}
				}
			}
		}

		// 如果不是实体类,并且parameters ==0 or parameters.size>1
		if (!isVoOrPo) {
			int count = 0;
			for (Parameter parameter : parameters) {
				String fieldName = FieldUtil.toUnderlineName(parameter.getName());
				OP op = parameter.getAnnotation(OP.class);
				if (columns.containsKey(fieldName)) {
					DataBaseFieldInfo dataBaseFieldInfo = columns.get(fieldName);
					dataBaseFieldInfo.setOp(op);
					dataBaseFieldInfo.setFieldIndex(count);
					dataBaseFieldInfoList.add(dataBaseFieldInfo);
					count++;
				}
			}

		}
	}

	/**
	 * 获取查询字段
	 * 
	 * @return
	 */
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

	/**
	 * 获取WHERE条件
	 */
	public String getWhere() {
		StringBuffer where = new StringBuffer();
		int fieldsLength = dataBaseFieldInfoList.size();
		for (int i = 0; i < fieldsLength; i++) {
			DataBaseFieldInfo dataBaseFieldInfo = dataBaseFieldInfoList.get(i);
			setTableInfoIndex(dataBaseFieldInfo.getFieldNameCamelCase(), i); // 找到分表字段下标，进行标记
			where.append(dataBaseFieldInfo.getWhereValue(fieldsLength, i));
		}
		return where.toString();
	}

	/**
	 * 获取分表后缀
	 * 
	 * @return
	 */
	public String getTableSuffix() {
		String tableSuffix = "";
		Integer index = this.tableInfo.getIndex();
		if (null != index && index > -1) {
			String indexStr = "";
			if (isVoOrPo) {
				String listStr = "";
				if (paramterType.equals(List.class.getName())) {
					listStr = "list[0].";
				}

				indexStr = "#{" + listStr + this.tableInfo.getTableSuffix() + "}";
				tableSuffix = this.tableInfo.getTableSeparator() + "${" + listStr + this.tableInfo.getTableSuffix() + "}";
			} else {
				indexStr = "#{" + this.tableInfo.getIndex() + "}";
				tableSuffix = this.tableInfo.getTableSeparator() + indexStr;
			}

			tableSuffix = String.format(MapperTagReources.MAPPER_TABLE_SUFFIX, indexStr, tableSuffix);
		}
		return tableSuffix;
	}

	/**
	 * 获取count字段
	 * 
	 * @return
	 */
	public String getSelectCountFields() {
		StringBuffer selectFields = new StringBuffer();
		String fieldFormat = "";

		// 如果VALUE为空，默认count(1)
		if (StringUtils.isEmpty(value)) {
			fieldFormat = "count(1) as `count`";
		} else {

			// 是否去重
			String distinctStr = "";
			if (distinct) {
				distinctStr = "distinct";
			}
			String[] values = this.value.split(",");
			for (String v : values) {
				fieldFormat = "count(" + distinctStr + " " + FieldUtil.toUnderlineName(v) + ")" + " as " + v;
				selectFields.append(fieldFormat);
			}
		}
		setGroupBy(selectFields);
		return selectFields.toString();
	}

	/**
	 * 获取插入字段
	 * 
	 * @return
	 */
	public String getInsertField() {
		StringBuffer insertFields = new StringBuffer();
		int fieldsLength = dataBaseFieldInfoList.size();
		for (int i = 0; i < fieldsLength; i++) {
			DataBaseFieldInfo dataBaseFieldInfo = dataBaseFieldInfoList.get(i);
			insertFields.append(dataBaseFieldInfo.getInsertField(fieldsLength, i));
		}
		return insertFields.toString();
	}

	/**
	 * 获取插入值
	 * 
	 * @return
	 */
	public String getInsertValue() {
		StringBuffer insertValue = new StringBuffer();
		int fieldsLength = dataBaseFieldInfoList.size();
		if (fieldsLength > 0) {
			insertValue.append("(");
			for (int i = 0; i < fieldsLength; i++) {
				DataBaseFieldInfo dataBaseFieldInfo = dataBaseFieldInfoList.get(i);
				setTableInfoIndex(dataBaseFieldInfo.getFieldNameCamelCase(), i);
				// 如果是集合，VALUE的填写方式不一样
				if (isCollection) {
					insertValue.append(dataBaseFieldInfo.getBatchInsertValue(fieldsLength, i, isVoOrPo));
				} else {
					insertValue.append(dataBaseFieldInfo.getInsertValue(fieldsLength, i, isVoOrPo));

				}

			}
			insertValue.append(")");
		}
		return insertValue.toString();

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
						this.distinct = (boolean) method.invoke(annotation, null);
						break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
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

	/**
	 * 向mybatis注册返回对象
	 * 
	 * @param className
	 * @return
	 */
	private RegisterAliasInfo registerAlias(String className) {
		RegisterAliasInfo registerAliasInfo = null;
		try {
			Class<?> clz = Class.forName(className);
			String registerAliasName = FieldUtil.toCamelCase(clz.getSimpleName());
			registerAliasInfo = new RegisterAliasInfo(registerAliasName, clz);
			this.typeAliasRegistry.registerAlias(registerAliasName, clz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return registerAliasInfo;
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

		// 如果不是基础类型，认为是VO或者是PO。进行注册
		if (!BASE_TYPE.contains(resultType)) {
			registerAlias(resultType);
		}
		return resultType;
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

	public static String getParamterType(String genericity) {
		String genericityName = genericity;
		int startIndex = genericity.indexOf("<");
		if (-1 != startIndex) {
			genericityName = genericity.substring(0, startIndex);
		}
		return genericityName;
	}

	public String getTableName() {
		return this.tableInfo.getTableName();
	}

	public String getParamterType() {
		return paramterType;
	}

	public boolean isCollection() {
		return isCollection;
	}

	class RegisterAliasInfo {
		private String name;
		private Class<?> clz;

		public RegisterAliasInfo(String name, Class<?> clz) {
			this.name = name;
			this.clz = clz;
		}

		public String getName() {
			return name;
		}

		public Class<?> getClz() {
			return clz;
		}

	}

}
