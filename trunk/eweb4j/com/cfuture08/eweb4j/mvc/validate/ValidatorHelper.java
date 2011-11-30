package com.cfuture08.eweb4j.mvc.validate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;

/**
 * 验证器帮助类
 * 
 * @author cfuture.aw
 * 
 */
public class ValidatorHelper implements ValidatorIF {
	private String regex;

	public ValidatorHelper(String regex) {
		this.regex = regex;
	}

	@Override
	public Map<String, String> validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String key = f.getName();
			String[] value = map.get(key);

			if (value == null) {
				if (!"".matches(this.regex)) {
					valError.put(key, f.getMessage());
				}
			} else {
				for (String v : value) {
					if (!v.matches(this.regex)) {
						valError.put(key, f.getMessage());
					}
					if (v != null) {
						request.setAttribute(key, v);
					}
				}
			}
		}

		return valError.isEmpty() ? null : valError;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
}
