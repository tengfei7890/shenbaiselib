package com.cfuture08.eweb4j.mvc.config.bean;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

public class ParamConfigBean {
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String name;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String value;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean")
	private String xmlBean;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
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
}
