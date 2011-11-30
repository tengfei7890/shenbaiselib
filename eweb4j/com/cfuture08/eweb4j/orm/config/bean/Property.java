package com.cfuture08.eweb4j.orm.config.bean;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;
/**
 * ORM组件用来存取配置信息的bean
 * @author cfuture.aw
 * @since v1.a.0
 */
public class Property {
	/**
	 * POJO属性名字
	 */
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = true, canRead = true)
	private String name;
	/**
	 * 数据类型
	 */
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = true, canRead = true)
	private String type;
	/**
	 * 数据表字段名
	 */
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = true, canRead = true)
	private String column;
	
	/**
	 * 关联类型(一对一，一对多，多对多)
	 */
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = false, canRead = true)
	private String relType;

	/**
	 * 关联哪个POJO
	 */
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = false, canRead = true)
	private String relBean;

	/**
	 * 关联POJO的哪个属性
	 */
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = false, canRead = true)
	private String relProperty;

	/**
	 * 关联哪个表（多对多用）
	 */
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = false, canRead = true)
	private String relTable;

	/**
	 * 从哪个字段（多对多用）
	 */
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = false, canRead = true)
	private String relFrom;

	/**
	 * 往哪个字段（多对多用）
	 */
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = false, canRead = true)
	private String relTo;
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = true, canRead = true)
	private String pk;
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = false, canRead = true)
	private String notNull;
	@XmlTag(type = XmlTagType.attriType, value = "", canWrite = true, canRead = true)
	private String autoIncrement;

	@XmlTag(type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.orm.config.bean.Property", canWrite = true, canRead = true)
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

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getRelType() {
		return relType;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	}

	public String getRelBean() {
		return relBean;
	}

	public void setRelBean(String relBean) {
		this.relBean = relBean;
	}

	public String getRelProperty() {
		return relProperty;
	}

	public void setRelProperty(String relProperty) {
		this.relProperty = relProperty;
	}

	public String getRelTable() {
		return relTable;
	}

	public void setRelTable(String relTable) {
		this.relTable = relTable;
	}

	public String getRelFrom() {
		return relFrom;
	}

	public void setRelFrom(String relFrom) {
		this.relFrom = relFrom;
	}

	public String getRelTo() {
		return relTo;
	}

	public void setRelTo(String relTo) {
		this.relTo = relTo;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getNotNull() {
		return notNull;
	}

	public void setNotNull(String notNull) {
		this.notNull = notNull;
	}

	public String getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(String autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String getXmlBean() {
		return xmlBean;
	}

	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}
}
