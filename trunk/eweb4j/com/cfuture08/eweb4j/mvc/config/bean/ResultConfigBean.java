package com.cfuture08.eweb4j.mvc.config.bean;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;
/**
 * MVC组件用来存取配置信息的Bean
 * @author cfuture.aw
 * @since v1.a.0
 */
public class ResultConfigBean {
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String name;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String type;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String location;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.mvc.config.bean.ResultConfigBean")
	private String xmlBean;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String value) {
		this.location = value;
	}
	public String getXmlBean() {
		return xmlBean;
	}
	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}
}
