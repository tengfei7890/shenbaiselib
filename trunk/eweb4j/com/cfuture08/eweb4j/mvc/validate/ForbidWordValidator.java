package com.cfuture08.eweb4j.mvc.validate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;
/**
 * 禁止发表敏感文字
 * @author weiwei
 *
 */
public class ForbidWordValidator implements ValidatorIF {

	@Override
	public Map<String, String> validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());
			String v = (value == null ? "" : value[0].trim());
			for (ParamConfigBean p : f.getParam()) {
				if (ValidatorConstant.FORBID_WORD_PARAM.equals(p.getName())) {
					String[] forbidWord = p.getValue().split("#");
					StringBuilder sb = new StringBuilder();
					for (String word : forbidWord){
						if (v.indexOf(word.trim()) != -1){
							sb.append(word).append("|");
						}
					}
					
					if (sb.length() > 0){
						valError.put(f.getName(), f.getMessage()+sb.toString());
					}
				} 
			}

			request.setAttribute(f.getName(), v);
		}
		return valError.isEmpty() ? null : valError;
	}
}
