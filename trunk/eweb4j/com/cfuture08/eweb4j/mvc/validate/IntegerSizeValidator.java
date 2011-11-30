package com.cfuture08.eweb4j.mvc.validate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;
import com.cfuture08.util.RegexList;

/**
 * 对数字大小的验证
 * 
 * @author cfuture.aw
 * 
 */
public class IntegerSizeValidator implements ValidatorIF {

	@Override
	public Map<String, String> validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {

		Map<String, String> error = null;

		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());
			String temp = (value == null ? "" : value[0]);
			// 先保证temp是数字，才能进行验证大小
			int v = 0;
			if (temp.trim().matches(RegexList.integer_regexp)) {
				v = Integer.parseInt(temp);
				for (ParamConfigBean p : f.getParam()) {
					if (ValidatorConstant.MIN_SIZE_PARAM.equals(p.getName())) {
						if (v < Integer.parseInt(p.getValue())) {
							valError.put(f.getName(), f.getMessage());
						}
					} else if (ValidatorConstant.MAX_SIZE_PARAM.equals(p
							.getName())) {
						if (v > Integer.parseInt(p.getValue())) {
							valError.put(f.getName(), f.getMessage());
						}
					}
				}
			}else{
				valError.put(f.getName(), f.getMessage());
			}
			
			request.setAttribute(f.getName(), v);

		}

		error = valError.isEmpty() ? null : valError;

		return error;
	}
}
