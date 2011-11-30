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
public class ConfigMVC {
	@XmlTag(canRead = true, canWrite = true, type = "", value = "true")
	private String open;
	@XmlTag(canRead = true, canWrite = true, type = "", value = "true")
	private String debug;
	@XmlTag(canRead = true, canWrite = true, type = "", value = "logs/mvc.log")
	private String logFile;
	@XmlTag(canRead = true, canWrite = true, type = "", value = "5")
	private String logMaxSize;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.classType, value = "com.cfuture08.eweb4j.config.bean.ScanActionPackage")
	private ScanActionPackage scanActionPackage;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.classType, value = "com.cfuture08.eweb4j.config.bean.ActionXmlFile")
	private ActionXmlFile actionXmlFiles;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.classType, value = "com.cfuture08.eweb4j.config.bean.InterXmlFile")
	private InterXmlFile interXmlFiles;
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.config.bean.ConfigMVC")
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

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	public ActionXmlFile getActionXmlFiles() {
		return actionXmlFiles;
	}

	public void setActionXmlFiles(ActionXmlFile actionXmlFiles) {
		this.actionXmlFiles = actionXmlFiles;
	}

	public InterXmlFile getInterXmlFiles() {
		return interXmlFiles;
	}

	public void setInterXmlFiles(InterXmlFile interXmlFiles) {
		this.interXmlFiles = interXmlFiles;
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

	public ScanActionPackage getScanActionPackage() {
		return scanActionPackage;
	}

	public void setScanActionPackage(ScanActionPackage scanActionPackage) {
		this.scanActionPackage = scanActionPackage;
	}
}
