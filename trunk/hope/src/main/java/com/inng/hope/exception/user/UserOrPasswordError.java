package com.inng.hope.exception.user;

import com.inng.hope.exception.BaseRuntimeException;
import com.inng.hope.exception.tips.UserTips;

public class UserOrPasswordError extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserOrPasswordError() {
		super.setTips(UserTips.USER_OR_PASSWORD_ERROR);
	}
}
