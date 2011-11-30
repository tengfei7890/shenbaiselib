package com.cfuture08.util;

import java.io.File;
import java.util.List;

import com.cfuture08.util.xml.BeanXMLReader;
import com.cfuture08.util.xml.BeanXMLWriter;
import com.cfuture08.util.xml.XMLReader;
import com.cfuture08.util.xml.XMLWriter;
/**
 * 
 * @author weiwei
 *
 */
public class BeanXMLUtil {
	public static XMLReader getBeanXMLReader(File file) {
		return new BeanXMLReader(file);
	}

	public static XMLWriter getBeanXMLWriter(File file, List<?> list) {
		return new BeanXMLWriter(file, list);
	}

	public static XMLWriter getBeanXMLWriter(File file, Class<?> clazz) {
		return new BeanXMLWriter(file, clazz);
	}

	public static XMLWriter getBeanXMLWriter(File file, Object obj) {
		return new BeanXMLWriter(file, obj);
	}
	
	public static XMLWriter getBeanXMLWriter(File file, Object... objs) {
		return new BeanXMLWriter(file, objs);
	}
	
	public static XMLWriter getBeanXMLWriter(File file, Class<?>... clazzs) {
		return new BeanXMLWriter(file, clazzs);
	}
}
