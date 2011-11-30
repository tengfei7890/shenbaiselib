package com.cfuture08.eweb4j.config.bean;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

public class ScanActionPackage {
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.listElementType, value = "")
	private List<String> path = new ArrayList<String>();
	@XmlTag(canRead = true, canWrite = true, type = XmlTagType.attriType, value = "com.cfuture08.eweb4j.config.bean.ScanActionPackage")
	private String xmlBean;
	
	public List<String> getPath() {
		return path;
	}
	public void setPath(List<String> path) {
		this.path = path;
	}
	public String getXmlBean() {
		return xmlBean;
	}
	public void setXmlBean(String xmlBean) {
		this.xmlBean = xmlBean;
	}
}
