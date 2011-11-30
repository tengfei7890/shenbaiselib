package com.cfuture08.eweb4j.mvc.validate;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;
import com.cfuture08.util.RegexList;

/**
 * URL验证器
 * @author cfuture.aw
 * @since v1.a.0
 *
 */
public class URLValidator implements ValidatorIF {

	@Override
	public Map<String, String> validate(ValidatorConfigBean val, Map<String, String[]> map,
			HttpServletRequest request) {
		return new ValidatorHelper(RegexList.url_regexp).validate(val, map,
				request);
	}

}
