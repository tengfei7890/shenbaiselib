package com.cfuture08.eweb4j.orm.dao.config.bean;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

public class Property {
	@XmlTag(type = XmlTagType.attriType)
	private String key;
	@XmlTag(type = XmlTagType.attriType)
	private String value;
	@XmlTag(type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.orm.dao.config.bean.Property", canWrite = true, canRead = true)
	private String xmlBean;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

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

}
