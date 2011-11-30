package com.cfuture08.eweb4j.orm.config.bean;

import java.util.List;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;
/**
 * ORM组件用来存取配置信息的bean
 * @author cfuture.aw
 * @since v1.a.0
 */
public class ORMConfigBean {
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = true, canRead = true)
	private String id;
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = true, canRead = true)
	private String table;
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = true, canRead = true)
	private String clazz;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.orm.config.bean.ORMConfigBean")
	private String xmlBean;
	@XmlTag(type = XmlTagType.listClassType, value = "com.cfuture08.eweb4j.orm.config.bean.Property", canWrite = true, canRead = true)
	private List<Property> property;

	public String getXmlBean() {
		return xmlBean;
	}

	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public List<Property> getProperty() {
		return property;
	}

	public void setProperty(List<Property> property) {
		this.property = property;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
