package com.inng.hope.exception.user;

import com.inng.hope.exception.BaseRuntimeException;
import com.inng.hope.exception.tips.UserTips;

public class UserNotExits extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotExits() {
		super.setTips(UserTips.USER_NOT_EXITS);
	}

}
