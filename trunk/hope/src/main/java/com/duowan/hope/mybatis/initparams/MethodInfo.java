package com.duowan.hope.mybatis.initparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
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

import com.duowan.hope.mybatis.TagReources;
import com.duowan.hope.mybatis.annotation.HopeSelect;
import com.duowan.hope.mybatis.annotation.HopeUpdate;
import com.duowan.hope.mybatis.annotation.OP;
import com.duowan.hope.mybatis.database.DataBaseFieldInfo;
import com.duowan.hope.mybatis.page.HopePage;
import com.duowan.hope.mybatis.util.FieldUtil;

public class MethodInfo {

	private TypeAliasRegistry typeAliasRegistry; // mybatis 返回实体类注册
	public static final Set<String> BASE_TYPE = new HashSet<String>(); // 基础类型

	public static final Map<String, String> COLLECTION_TYPE = new HashMap<String, String>(); // 基础类型

	private Map<String, DataBaseFieldInfo> prefixFiels;
	private Map<String, DataBaseFieldInfo> suffixFiels;

	private String genericReturnType;

	private String id; // 对应mybatis 方法的ID

	private String value; // 个性化查询字段或者条件

	private String returnType; // 方法返回类型

	private String type; // 注解类型

	private Class<?> returnClass;

	private String orderByASC;

	private String orderByDESC;

	private String groupBy;

	private String paramterType;

	private boolean distinct;

	private boolean isVoOrPo;

	private boolean isCollection;

	private boolean isArray;

	// 是否返回主键
	private boolean returnPrimaryKey;

	// 主键名称
	private String primaryKey;

	// 表名相关信息
	private TableInfo tableInfo;

	static {
		BASE_TYPE.add(Integer.class.getName());
		BASE_TYPE.add(Double.class.getName());
		BASE_TYPE.add(String.class.getName());
		BASE_TYPE.add(HashMap.class.getName());
		BASE_TYPE.add(Boolean.class.getName());
		BASE_TYPE.add(Date.class.getName());
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
	public MethodInfo(Method method, Annotation annotation, Map<String, DataBaseFieldInfo> columns, TypeAliasRegistry typeAliasRegistry, TableInfo tableInfo, String genericReturnType, String namespace) {
		this.typeAliasRegistry = typeAliasRegistry; // mybatis 注册对象
		this.genericReturnType = genericReturnType; // 泛型类型
		this.tableInfo = tableInfo;
		this.id = method.getName();
		setAnnotationInfo(annotation);
		setType(annotation);
		setPrimaryKey(columns);

		this.returnType = getReturnType(method);
		setPageMethodInfo(method, namespace);
		if (!StringUtils.isEmpty(this.type)) {
			setPrefixFiels(method, columns);
			setSuffixFiels(method, columns);
		}

	}

	/**
	 * 设置分页方法信息
	 * 
	 * @param method
	 */
	private void setPageMethodInfo(Method method, String namespace) {
		if (this.returnType.equals(HopePage.class.getName())) {
			this.returnType = this.genericReturnType;
			MethodPage methodPage = new MethodPage();
			methodPage.setId(namespace + "." + method.getName());
			Parameter[] parameters = method.getParameters();

			int count = 1;
			for (Parameter parameter : parameters) {
				if (parameter.getName().equals(TagReources.PAGE_SIZE)) {
					methodPage.setPageSize(TagReources.PARAM + count);
				}

				if (parameter.getName().equals(TagReources.PAGE_NO)) {
					methodPage.setPageNo(TagReources.PARAM + count);
				}
				count++;
			}
			MethodPageInfo.put(methodPage);
		}
	}

	/**
	 * 获取主键
	 * 
	 * @param columns
	 */
	private void setPrimaryKey(Map<String, DataBaseFieldInfo> columns) {
		for (Map.Entry<String, DataBaseFieldInfo> entry : columns.entrySet()) {
			DataBaseFieldInfo dataBaseFieldInfo = entry.getValue();
			if (dataBaseFieldInfo.isAutoincrement() && dataBaseFieldInfo.isPrimaryKey()) {
				this.primaryKey = dataBaseFieldInfo.getFieldNameCamelCase();
			}
		}
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
	 * 设置前置字段 包含查询、修改、更新需要的字段
	 * 
	 * @param method
	 * @param columns
	 */
	private void setPrefixFiels(Method method, Map<String, DataBaseFieldInfo> columns) {
		prefixFiels = new TreeMap<String, DataBaseFieldInfo>();
		Parameter[] parameters = method.getParameters();
		// 如果类型是HopeSelect并且配置了value,查询字段变成value
		if (this.type.equals(HopeSelect.class.getSimpleName())) {
			if (!StringUtils.isEmpty(value)) {
				String[] values = this.value.split(",");
				for (String v : values) {
					prefixFiels.put(v, columns.get(FieldUtil.toUnderlineName(v)));
				}
			} else {
				prefixFiels = columns;
			}

		} else if (this.type.equals(HopeUpdate.class.getSimpleName())) { // 查询条件不需要更新
			String[] values = new String[] {};
			if (!StringUtils.isEmpty(value)) {
				values = this.value.split(",");
			}

			for (int i = 1; i <= parameters.length; i++) {

				Parameter parameter = parameters[i - 1];

				String fieldName = FieldUtil.toUnderlineName(parameter.getName());

				// 获取参数类型
				String typeName = parameter.getParameterizedType().getTypeName();
				// 设置传入方法类型
				RegisterAliasInfo registerAliasInfo = setParamterType(typeName);

				if (null == registerAliasInfo) {
					OP op = parameter.getAnnotation(OP.class);
					if (null != op && op.isUpdate() == true) {
						DataBaseFieldInfo dataBaseFieldInfo = columns.get(fieldName);
						dataBaseFieldInfo.setEntityParam(false);
						dataBaseFieldInfo.setFieldIndex(i);
						prefixFiels.put(fieldName, dataBaseFieldInfo);
					} else {
						prefixFiels.remove(fieldName);
					}
				} else {

					Field[] fields = registerAliasInfo.getClz().getDeclaredFields();

					// 更新排除WHERE字段
					for (Map.Entry<String, DataBaseFieldInfo> entry : columns.entrySet()) {

						// 查看是否为条件字段
						for (String v : values) {
							if (FieldUtil.toUnderlineName(v).equals(entry.getKey())) {
								continue;
							}
						}

						// 需要更新的字段
						for (Field field : fields) {
							fieldName = FieldUtil.toUnderlineName(field.getName());
							if (fieldName.equals(entry.getKey())) {
								DataBaseFieldInfo dataBaseFieldInfo = columns.get(fieldName);
								dataBaseFieldInfo.setFieldIndex(i);
								dataBaseFieldInfo.setEntityParam(true);
								if (null != parameters && parameters.length == 1) {
									dataBaseFieldInfo.setSingleParam(true);
								} else {
									dataBaseFieldInfo.setSingleParam(false);
								}
								prefixFiels.put(fieldName, dataBaseFieldInfo);
							}
						}

					}
				}
			}

		} else {
			for (Parameter parameter : parameters) {
				String fieldName = FieldUtil.toUnderlineName(parameter.getName());
				// 获取参数类型
				String typeName = parameter.getParameterizedType().getTypeName();
				// 设置传入方法类型
				RegisterAliasInfo registerAliasInfo = setParamterType(typeName);
				if (null == registerAliasInfo) {
					prefixFiels.put(fieldName, columns.get(fieldName));
				} else {
					Field[] fields = registerAliasInfo.getClz().getDeclaredFields();
					for (Field field : fields) {
						fieldName = FieldUtil.toUnderlineName(field.getName());
						prefixFiels.put(fieldName, columns.get(fieldName));
					}
				}

			}

		}

	}

	/**
	 * 设置Where条件字段
	 * 
	 * @param method
	 * @param columns
	 */
	private void setSuffixFiels(Method method, Map<String, DataBaseFieldInfo> columns) {
		suffixFiels = new TreeMap<String, DataBaseFieldInfo>();
		Parameter[] parameters = method.getParameters();

		// 如果类型是HopeUpdate
		if (this.type.equals(HopeUpdate.class.getSimpleName())) {
			setUpdateSuffixFiels(parameters, columns);
		} else {
			int count = 1;
			for (Parameter parameter : parameters) {
				// 获取参数类型
				String typeName = parameter.getParameterizedType().getTypeName();
				// 设置传入方法类型
				RegisterAliasInfo registerAliasInfo = setParamterType(typeName);

				String fieldName = FieldUtil.toUnderlineName(parameter.getName());
				// 如果参数类型为基础类型
				if (null == registerAliasInfo) {
					OP op = parameter.getAnnotation(OP.class);
					if (columns.containsKey(fieldName)) {
						DataBaseFieldInfo dataBaseFieldInfo = columns.get(fieldName);
						dataBaseFieldInfo.setEntityParam(false);
						dataBaseFieldInfo.setListOrArray(isArray);
						dataBaseFieldInfo.setOp(op);
						dataBaseFieldInfo.setFieldIndex(count);
						suffixFiels.put(fieldName, dataBaseFieldInfo);
						count++;
					}
				} else { // 如果参数是对象
					Field[] fields = registerAliasInfo.getClz().getDeclaredFields();
					for (Field field : fields) {
						fieldName = FieldUtil.toUnderlineName(field.getName());
						if (columns.containsKey(fieldName)) {
							DataBaseFieldInfo dataBaseFieldInfo = columns.get(fieldName);
							dataBaseFieldInfo.setEntityParam(true);
							suffixFiels.put(fieldName, dataBaseFieldInfo);
						}
					}
				}

			}
		}
	}

	private void setUpdateSuffixFiels(Parameter[] parameters, Map<String, DataBaseFieldInfo> columns) {
		if (!StringUtils.isEmpty(value)) {
			String[] values = this.value.split(",");
			for (String v : values) {
				DataBaseFieldInfo dataBaseFieldInfo = columns.get(FieldUtil.toUnderlineName(v));

				if (null != parameters && parameters.length == 1) {
					dataBaseFieldInfo.setSingleParam(true);
					dataBaseFieldInfo.setEntityParam(true);
				}
				suffixFiels.put(v, dataBaseFieldInfo);
			}
		} else {
			int count = 1;
			for (Parameter parameter : parameters) {
				// 获取参数类型
				String typeName = parameter.getParameterizedType().getTypeName();
				// 设置传入方法类型
				RegisterAliasInfo registerAliasInfo = setParamterType(typeName);

				// 如果参数类型为基础类型
				if (null == registerAliasInfo) {
					String fieldName = FieldUtil.toUnderlineName(parameter.getName());
					OP op = parameter.getAnnotation(OP.class);

					// 如果字段是标记更新字段，跳过
					if (null != op && op.isUpdate() == true) {
						count++;
						continue;
					}

					if (columns.containsKey(fieldName)) {
						DataBaseFieldInfo dataBaseFieldInfo = columns.get(fieldName);
						dataBaseFieldInfo.setEntityParam(false);
						dataBaseFieldInfo.setOp(op);
						dataBaseFieldInfo.setFieldIndex(count);
						suffixFiels.put(fieldName, dataBaseFieldInfo);

					}
				}
				count++;

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
		int fieldsLength = prefixFiels.size();

		int count = 0;
		for (Map.Entry<String, DataBaseFieldInfo> entry : prefixFiels.entrySet()) {
			DataBaseFieldInfo dataBaseFieldInfo = entry.getValue();
			selectFields.append(dataBaseFieldInfo.getSelectField(fieldsLength, count));
			count++;
		}
		setGroupBy(selectFields);
		return selectFields.toString();
	}

	/**
	 * 获取WHERE条件
	 */
	public String getWhere() {
		StringBuffer where = new StringBuffer();
		int i = 0;
		for (Map.Entry<String, DataBaseFieldInfo> entry : suffixFiels.entrySet()) {
			DataBaseFieldInfo dataBaseFieldInfo = entry.getValue();
			setTableInfoIndex(dataBaseFieldInfo.getFieldNameCamelCase(), i); // 找到分表字段下标，进行标记
			where.append(dataBaseFieldInfo.getWhereValue());
			i++;
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
				String prefix = getVoOrPoPrefix();
				indexStr = "#{" + prefix + this.tableInfo.getTableSuffix() + "}";
				tableSuffix = this.tableInfo.getTableSeparator() + "${" + prefix + this.tableInfo.getTableSuffix() + "}";
			} else {
				indexStr = "#{param" + this.tableInfo.getIndex() + "}";
				tableSuffix = this.tableInfo.getTableSeparator() + "${param" + this.tableInfo.getIndex() + "}";
			}

			tableSuffix = String.format(TagReources.MAPPER_TABLE_SUFFIX, indexStr, tableSuffix);
		}
		return tableSuffix;
	}

	private String getVoOrPoPrefix() {
		String resultStr = "";
		if (paramterType.equals(List.class.getName())) {
			resultStr = "list[0].";
		} else if (!BASE_TYPE.contains(paramterType)) {
			resultStr = "param" + this.tableInfo.getIndex() + ".";
		}
		return resultStr;
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
			selectFields.append(fieldFormat);
		} else {

			// 是否去重
			String distinctStr = "";
			if (distinct) {
				distinctStr = TagReources.DISTINCT;
			}
			String[] values = this.value.split(",");
			for (String v : values) {
				fieldFormat = String.format(TagReources.COUNT, distinctStr + FieldUtil.toUnderlineName(v), v);
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
		int fieldsLength = prefixFiels.size();
		int count = 0;
		for (Map.Entry<String, DataBaseFieldInfo> entry : prefixFiels.entrySet()) {
			DataBaseFieldInfo dataBaseFieldInfo = entry.getValue();
			insertFields.append(dataBaseFieldInfo.getInsertField(fieldsLength, count));
			count++;
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
		int fieldsLength = suffixFiels.size();
		int count = 0;

		if (fieldsLength > 0) {
			insertValue.append("(");

			for (Map.Entry<String, DataBaseFieldInfo> entry : prefixFiels.entrySet()) {
				DataBaseFieldInfo dataBaseFieldInfo = entry.getValue();
				setTableInfoIndex(dataBaseFieldInfo.getFieldNameCamelCase(), count);

				// 如果是集合，VALUE的填写方式不一样
				if (isCollection || isArray) {
					insertValue.append(dataBaseFieldInfo.getBatchInsertValue(fieldsLength, count, isVoOrPo));
				} else {
					insertValue.append(dataBaseFieldInfo.getInsertValue(fieldsLength, count, isVoOrPo));

				}

				count++;
			}
			insertValue.append(")");
		}
		return insertValue.toString();

	}

	/**
	 * 获取更新字段
	 * 
	 * @return
	 */
	public String getUpdate() {
		StringBuffer update = new StringBuffer();
		int fieldsLength = prefixFiels.size();
		int count = 1;
		for (Map.Entry<String, DataBaseFieldInfo> entry : prefixFiels.entrySet()) {
			DataBaseFieldInfo dataBaseFieldInfo = entry.getValue();
			setTableInfoIndex(dataBaseFieldInfo.getFieldNameCamelCase(), count);
			update.append(dataBaseFieldInfo.getUpdateValue(fieldsLength, count - 1));
		}
		return update.toString();
	}

	private void setGroupBy(StringBuffer selectFields) {
		if (selectFields.length() > 0 && !StringUtils.isEmpty(groupBy)) {
			selectFields.append(",");
			selectFields.append(FieldUtil.toUnderlineName(groupBy) + TagReources.AS + groupBy);
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

	@SuppressWarnings("all")
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
					case "returnPrimaryKey":
						this.returnPrimaryKey = (boolean) method.invoke(annotation, null);
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

	public String getOrderBy() {
		String orderBy = getOrderBy(this.orderByASC, TagReources.ASC);
		if (StringUtils.isEmpty(orderBy)) {
			orderBy = getOrderBy(this.orderByDESC, TagReources.DESC);
		}
		return orderBy;
	}

	private String getOrderBy(String orderByFields, String sort) {
		String orderBy = "";
		if (!StringUtils.isEmpty(orderByFields)) {
			orderBy = TagReources.ORDER_BY + FieldUtil.toUnderlineName(orderByFields) + " " + sort;
		}
		return orderBy;
	}

	public String getGroupBy() {
		if (!StringUtils.isEmpty(groupBy)) {
			groupBy = TagReources.GROUP_BY + FieldUtil.toUnderlineName(groupBy);
		}
		return groupBy;
	}

	public String getId() {
		return id;
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
			String registerAliasName = FieldUtil.toLowerCamelCase(clz.getSimpleName());
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

		// 如果是OBJECT类型，证明是泛型.因为框架决定不能够返回OBJECT类型
		// OBJECT类型是由JAVA自动转换得来的结果
		if (resultType.equals(Object.class.getName())) {
			resultType = this.genericReturnType;
		}

		// 如果是List 获取泛型类型
		// 如果是MAP,去掉泛型
		if (resultType.equals(List.class.getName())) {
			String genericity = method.getGenericReturnType().getTypeName();
			resultType = getGenericityName(genericity);
			if (resultType.startsWith(Map.class.getName())) {
				resultType = Map.class.getName();
			}

			// 如果返回结果没有带. 是泛型 T
			int pointIndex = resultType.indexOf(".");
			if (pointIndex == -1) {
				resultType = this.genericReturnType;
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

		startIndex = genericity.indexOf("[");
		endIndex = genericity.indexOf("]");
		if (-1 != startIndex && -1 != endIndex) {
			genericityName = genericity.substring(0, startIndex);
			isArray = true;
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

	public String getReturnType() {
		return returnType;
	}

	public Class<?> getReturnClass() {
		return returnClass;
	}

	public String getTableName() {
		return this.tableInfo.getTableNameUnderLine();
	}

	public String getParamterType() {
		return paramterType;
	}

	public boolean isCollection() {
		return isCollection;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	public boolean isReturnPrimaryKey() {
		return returnPrimaryKey;
	}

	public void setReturnPrimaryKey(boolean returnPrimaryKey) {
		this.returnPrimaryKey = returnPrimaryKey;
	}

	public String getPrimaryKey() {
		return primaryKey;
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
