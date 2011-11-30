package com.cfuture08.eweb4j.mvc.config.bean;

import java.util.List;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

public class FieldConfigBean {
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String name;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String message;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.listClassType, value = "com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean")
	private List<ParamConfigBean> param;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean")
	private String xmlBean;

	public String getXmlBean() {
		return xmlBean;
	}

	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ParamConfigBean> getParam() {
		return param;
	}

	public void setParam(List<ParamConfigBean> param) {
		this.param = param;
	}
}
