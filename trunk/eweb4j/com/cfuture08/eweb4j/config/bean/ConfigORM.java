package com.cfuture08.eweb4j.config.bean;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

/**
 * EWeb4J用来存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ConfigORM {
	@XmlTag(canRead = true, canWrite = true, type = "", value = "false")
	private String open;
	@XmlTag(canRead = true, canWrite = true, type = "", value = "true")
	private String debug;
	@XmlTag(canRead = true, canWrite = true, type = "", value = "logs/orm.log")
	private String logFile;
	@XmlTag(canRead = true, canWrite = true, type = "", value = "5")
	private String logMaxSize;
	@XmlTag(canRead = true, canWrite = true, type = "", value = "com.mchange.v2.c3p0.ComboPooledDataSource")
	private String dataSource;//数据源类完整路径，默认c3p0
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.classType, value = "com.cfuture08.eweb4j.config.bean.ScanPojoPackage")
	private ScanPojoPackage scanPojoPackage;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.classType, value = "com.cfuture08.eweb4j.config.bean.ORMXmlFiles")
	private ORMXmlFiles ormXmlFiles;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.classType, value = "com.cfuture08.eweb4j.config.bean.DBInfoXmlFiles")
	private DBInfoXmlFiles dbInfoXmlFiles;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.config.bean.ConfigORM")
	private String xmlBean;

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public String getLogFile() {
		return logFile;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public ScanPojoPackage getScanPojoPackage() {
		return scanPojoPackage;
	}

	public void setScanPojoPackage(ScanPojoPackage scanPojoPackage) {
		this.scanPojoPackage = scanPojoPackage;
	}

	public ORMXmlFiles getOrmXmlFiles() {
		return ormXmlFiles;
	}

	public void setOrmXmlFiles(ORMXmlFiles ormXmlFiles) {
		this.ormXmlFiles = ormXmlFiles;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	public DBInfoXmlFiles getDbInfoXmlFiles() {
		return dbInfoXmlFiles;
	}

	public void setDbInfoXmlFiles(DBInfoXmlFiles dbInfoXmlFiles) {
		this.dbInfoXmlFiles = dbInfoXmlFiles;
	}

	public String getXmlBean() {
		return xmlBean;
	}

	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}

	public String getLogMaxSize() {
		return logMaxSize;
	}

	public void setLogMaxSize(String logMaxSize) {
		this.logMaxSize = logMaxSize;
	}
}
