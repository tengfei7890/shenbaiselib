package com.cfuture08.eweb4j.mvc.config.bean;

import java.util.List;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

public class InterConfigBean {
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.listClassType, value = "com.cfuture08.eweb4j.mvc.config.bean.Url")
	private List<Url> url;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.listElementType, value = "")
	private List<String> except;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String showErrorType;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String name;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "before")
	private String type;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String clazz;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.mvc.config.bean.InterConfigBean")
	private String xmlBean;
	
	public List<Url> getUrl() {
		return url;
	}
	public void setUrl(List<Url> url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getExcept() {
		return except;
	}
	public void setExcept(List<String> except) {
		this.except = except;
	}
	public String getShowErrorType() {
		return showErrorType;
	}
	public void setShowErrorType(String showErrorType) {
		this.showErrorType = showErrorType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
}
