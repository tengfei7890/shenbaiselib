package com.cfuture08.eweb4j.mvc.validate;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;
import com.cfuture08.util.RegexList;
/**
 * 对QQ号码的验证
 * @author cfuture.aw
 *
 */
public class QQValidator implements ValidatorIF{

	@Override
	public Map<String, String> validate(ValidatorConfigBean val, Map<String, String[]> map,
			HttpServletRequest request) {
		return new ValidatorHelper(RegexList.qq_regexp).validate(val, map, request);
	}

}
