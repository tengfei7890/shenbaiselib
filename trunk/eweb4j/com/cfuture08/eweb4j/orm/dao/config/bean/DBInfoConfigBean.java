package com.cfuture08.eweb4j.orm.dao.config.bean;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.orm.dao.config.DAOConfigConstant;
import com.cfuture08.eweb4j.orm.sql.constant.DBType;
import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

/**
 * DAO组件用来存取配置信息 的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class DBInfoConfigBean {

	@XmlTag(type = XmlTagType.attriType, value = DAOConfigConstant.MYDBINFO, canWrite = true, canRead = true)
	private String dsName;
	@XmlTag(value = DBType.MYSQL_DB)
	private String dataBaseType;
	@XmlTag(type = XmlTagType.listClassType, value = "com.cfuture08.eweb4j.orm.dao.config.bean.Property")
	private List<Property> properties = new ArrayList<Property>();
	@XmlTag(type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.orm.dao.config.bean.DBInfoConfigBean", canWrite = true, canRead = true)
	private String xmlBean;

	public String getDsName() {
		return dsName;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public String getXmlBean() {
		return xmlBean;
	}

	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}

	public String getDataBaseType() {
		return dataBaseType;
	}

	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}
}
