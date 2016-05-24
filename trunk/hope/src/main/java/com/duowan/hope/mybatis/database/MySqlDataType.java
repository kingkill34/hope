package com.duowan.hope.mybatis.database;

import java.util.HashSet;
import java.util.Set;

public class MySqlDataType {

	public static Set<String> dataType = new HashSet<String>();

	static {
		// mysql 数字类型
		dataType.add("INT");
		dataType.add("TINYINT");
		dataType.add("SMALLINT");
		dataType.add("MEDIUMINT");
		dataType.add("BIGINT");
	}
}
