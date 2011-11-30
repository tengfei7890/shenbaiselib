package com.cfuture08.eweb4j.mvc.config.bean;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

public class Url {
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.elementType, value = "")
	private String value;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.elementType, value = "")
	private String type;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.mvc.config.bean.Url")
	private String xmlBean;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getXmlBean() {
		return xmlBean;
	}
	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}
}
