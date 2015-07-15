package com.inng.hope.exception.user;

import com.inng.hope.exception.BaseRuntimeException;
import com.inng.hope.exception.tips.UserTips;

public class UserNotExits extends BaseRuntimeException {

	public UserNotExits() {
		super.setTips(UserTips.USER_NOT_EXITS);
	}

}
