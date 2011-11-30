package com.cfuture08.eweb4j.ioc.config.bean;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

/**
 * IOC组件存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class Injection {
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String ref;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String name;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String type;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String value;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.ioc.config.bean.Injection")
	private String xmlBean;
	public String getXmlBean() {
		return xmlBean;
	}
	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
