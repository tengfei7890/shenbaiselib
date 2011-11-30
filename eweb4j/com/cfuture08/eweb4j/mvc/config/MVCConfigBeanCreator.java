package com.cfuture08.eweb4j.mvc.config;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.InterConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ResultConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.Url;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;
/**
 * 创建MVC存取配置信息bean实例
 * @author cfuture.aw
 * @since v1.a.0
 *
 */
public class MVCConfigBeanCreator {
	public static ActionConfigBean getActionBean() {
		ActionConfigBean mvcBean = null;
		mvcBean = new ActionConfigBean();
		List<ResultConfigBean> rlist = new ArrayList<ResultConfigBean>();
		ResultConfigBean result = new ResultConfigBean();
		rlist.add(result);
		mvcBean.setResult(rlist);
		
		List<ValidatorConfigBean> vlist = new ArrayList<ValidatorConfigBean>();
		ValidatorConfigBean validator = new ValidatorConfigBean();
		List<FieldConfigBean> fieldList = new ArrayList<FieldConfigBean>();
		FieldConfigBean field = new FieldConfigBean();
		List<ParamConfigBean> paramList = new ArrayList<ParamConfigBean>();
		ParamConfigBean param = new ParamConfigBean();
		paramList.add(param);
		field.setParam(paramList);
		fieldList.add(field);
		validator.setField(fieldList);
		
		
		
		vlist.add(validator);
		mvcBean.setValidator(vlist);
		mvcBean.setParam(paramList);
		
		return mvcBean;
	}
	
	public static InterConfigBean getInterBean(){
		InterConfigBean icb = new InterConfigBean();
		List<Url> urls = new ArrayList<Url>();
		Url url = new Url();
		urls.add(url);
		icb.setUrl(urls);
		List<String> excepts = new ArrayList<String>();
		excepts.add("");
		icb.setExcept(excepts);
		return icb;
	}
}
