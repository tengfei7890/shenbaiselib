package com.cfuture08.eweb4j.mvc.validate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;

public class RegexValidator implements ValidatorIF {

	@Override
	public Map<String, String> validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {

		Map<String, String> error = null;

		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());
			String temp = (value == null ? "" : value[0]);
			for (ParamConfigBean p : f.getParam()) {
				if (ValidatorConstant.REGEX_PARAM.equals(p.getName())) {
					if (!temp.matches(p.getValue())) {
						valError.put(f.getName(), f.getMessage());
					}
				}
			}
			request.setAttribute(f.getName(), temp);

		}
		error = valError.isEmpty() ? null : valError;

		return error;
	}
}
