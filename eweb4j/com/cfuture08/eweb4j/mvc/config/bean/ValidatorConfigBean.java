package com.cfuture08.eweb4j.mvc.config.bean;

import java.util.List;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

public class ValidatorConfigBean {
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String name;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String clazz;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.listClassType, value = "com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean")
	private List<FieldConfigBean> field;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean")
	private String xmlBean;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getXmlBean() {
		return xmlBean;
	}

	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}

	public List<FieldConfigBean> getField() {
		return field;
	}

	public void setField(List<FieldConfigBean> field) {
		this.field = field;
	}
}
