package com.cfuture08.eweb4j.mvc.validate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;

public class EnumValidator implements ValidatorIF{

	@Override
	public Map<String, String> validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());
			String v = (value == null ? "" : value[0].trim());
			for (ParamConfigBean p : f.getParam()) {
				if (ValidatorConstant.ENUM_WORD_PARAM.equalsIgnoreCase(p.getName())) {
					String[] enumWord = p.getValue().split("#");
					boolean flag = false;
					for (String word : enumWord){
						if (v.equals(word)){
							flag = true;
							break;
						}else{
							flag = false;
						}
					}
					
					if (!flag){
						valError.put(f.getName(), f.getMessage());
					}
				} 
			}

			request.setAttribute(f.getName(), v);
		}
		return valError.isEmpty() ? null : valError;
	}

}
