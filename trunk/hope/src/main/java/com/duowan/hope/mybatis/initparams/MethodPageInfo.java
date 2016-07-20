package com.duowan.hope.mybatis.initparams;

import java.util.HashMap;
import java.util.Map;

public class MethodPageInfo {

	private static Map<String, MethodPage> pageMethod = new HashMap<String, MethodPage>();

	public static Map<String, MethodPage> getPageMethod() {
		return pageMethod;
	}

	public static void setPageMethod(Map<String, MethodPage> pageMethod) {
		MethodPageInfo.pageMethod = pageMethod;
	}

	public static void put(MethodPage methodPage) {
		pageMethod.put(methodPage.getId(), methodPage);
	}
	
	
	public static MethodPage get(String key) {
		MethodPage methodPage = pageMethod.get(key);
		return methodPage; 
	}
	

}
