package com.cfuture08.eweb4j.config.bean;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;
/**
 * EWeb4J用来存取配置信息的bean
 * @author cfuture.aw
 * @since v1.a.0
 *
 */
public class ConfigBean {
	@XmlTag(canRead = true, canWrite = true, type = "", value = "0")
	private String debug ;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.classType, value = "com.cfuture08.eweb4j.config.bean.ConfigIOC")
	private ConfigIOC ioc;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.classType, value = "com.cfuture08.eweb4j.config.bean.ConfigORM")
	private ConfigORM orm;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.classType, value = "com.cfuture08.eweb4j.config.bean.ConfigMVC")
	private ConfigMVC mvc;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.config.bean.ConfigBean")
	private String xmlBean ;
	
	public String getDebug() {
		return debug;
	}
	public void setDebug(String debug) {
		this.debug = debug;
	}
	public ConfigIOC getIoc() {
		return ioc;
	}
	public void setIoc(ConfigIOC ioc) {
		this.ioc = ioc;
	}
	public ConfigORM getOrm() {
		return orm;
	}
	public void setOrm(ConfigORM orm) {
		this.orm = orm;
	}
	public ConfigMVC getMvc() {
		return mvc;
	}
	public void setMvc(ConfigMVC mvc) {
		this.mvc = mvc;
	}
	public String getXmlBean() {
		return xmlBean;
	}
	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}
}
