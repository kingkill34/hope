package com.inng.hope.result;

import com.inng.hope.util.Page;

public class ResultObject {

	/**
	 * 操作成功
	 */
	public static final Integer SUCCESS = 0;
	
	/**
	 * 操作失败
	 */
	public static final Integer FAIL = 1;
	
	/**
	 * 操作异常
	 */
	public static final Integer EXCEPTION = 2;
	
	/**
	 * 操作是否成功标识
	 */
	private Integer flag;
	
	/**
	 * 提示内容
	 */
	private String prompt;
	
	/**
	 * 数据
	 */
	private Object data;
	
	/**
	 * 异常内容
	 */
	private String exceptionMessage;
	
	/**
	 * 分页类
	 */
	private Page page;
	
	
	public ResultObject(){
		
	}
	
	
	public ResultObject(Integer flag,Object data){
		this.flag = flag;
		this.data = data;
	}
	
	public ResultObject(Integer flag,Object data,Page page){
		this.flag = flag;
		this.data = data;
		this.page = page;
	}
	
	public ResultObject(Integer flag,String prompt){
		this.flag = flag;
		this.prompt = prompt;
	}
	
	
	public ResultObject(Integer flag,Exception e){
		this.flag = flag;
		this.exceptionMessage = e.getMessage();
	}
	
	public ResultObject(Integer flag,String prompt,String exceptionMessage){
		this.flag = flag;
		this.prompt = prompt;
		this.exceptionMessage = exceptionMessage;
	}
	
	
	
	
	
	
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}


	public Page getPage() {
		return page;
	}


	public void setPage(Page page) {
		this.page = page;
	}
	
	
	
}
