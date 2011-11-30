package com.cfuture08.eweb4j.ioc.config.bean;

import java.util.List;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

/**
 * IOC组件存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class IOCConfigBean {
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String id;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String scope;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "")
	private String clazz;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.listClassType, value = "com.cfuture08.eweb4j.ioc.config.bean.Injection")
	private List<Injection> inject;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.ioc.config.bean.IOCConfigBean")
	private String xmlBean;

	public String getXmlBean() {
		return xmlBean;
	}

	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<Injection> getInject() {
		return inject;
	}

	public void setInject(List<Injection> inject) {
		this.inject = inject;
	}
}
