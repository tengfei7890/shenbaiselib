package com.cfuture08.eweb4j.mvc.config.bean;

import java.util.List;

import com.cfuture08.eweb4j.mvc.action.RequestMethodType;
import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

/**
 * MVC组件用来存取配置信息的Bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class ActionConfigBean {
	@XmlTag(canRead = false, canWrite = false, type = XmlTagType.attriType, value = "")
	private String reqMethod = RequestMethodType.GET;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String name;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String method;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String clazz;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String showValErrorType;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.listClassType, value = "com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean")
	private List<ParamConfigBean> param;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.listClassType, value = "com.cfuture08.eweb4j.mvc.config.bean.ResultConfigBean")
	private List<ResultConfigBean> result;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.listClassType, value = "com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean")
	private List<ValidatorConfigBean> validator;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean")
	private String xmlBean;


	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ParamConfigBean> getParam() {
		return param;
	}

	public void setParam(List<ParamConfigBean> param) {
		this.param = param;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getShowValErrorType() {
		return showValErrorType;
	}

	public void setShowValErrorType(String showValErrorType) {
		this.showValErrorType = showValErrorType;
	}

	public List<ValidatorConfigBean> getValidator() {
		return validator;
	}

	public void setValidator(List<ValidatorConfigBean> validator) {
		this.validator = validator;
	}

	public List<ResultConfigBean> getResult() {
		return result;
	}

	public void setResult(List<ResultConfigBean> result) {
		this.result = result;
	}

	public String getXmlBean() {
		return xmlBean;
	}

	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}
}
