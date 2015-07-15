package com.inng.hope.exception;

public class BaseRuntimeException extends RuntimeException {

	private String tips;

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

}
