package com.cfuture08.eweb4j.mvc.validate;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;

public interface ValidatorIF {
	Map<String, String> validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request);
}
