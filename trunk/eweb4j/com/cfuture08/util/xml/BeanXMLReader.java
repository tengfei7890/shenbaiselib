package com.cfuture08.util.xml;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cfuture08.util.ReflectUtil;
import com.cfuture08.util.StringUtil;
import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

public class BeanXMLReader implements XMLReader {
	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public BeanXMLReader(File file) {
		this.setFile(file);
	}

	public BeanXMLReader() {
	}

	public <T> List<T> read() throws Exception {
		List<T> tList = new ArrayList<T>();
		T t = null;
		SAXReader reader = new SAXReader();
		Document doc = reader.read(this.file);
		// 列出beans下的所有bean元素节点
		List<?> list = doc.selectNodes("//" + BeanXMLConstant.ROOT_ELEMENT
				+ "/" + BeanXMLConstant.SUBROOT_ELEMENT);
		for (Iterator<?> it = list.iterator(); it.hasNext();) {
			Element bean = (Element) it.next();
			// 进入递归
			t = this.readRecursion(bean);
			tList.add(t);
		}

		return tList;
	}

	public <T> T readOne() throws Exception {
		T t = null;
		List<T> list = this.read();
		if (list != null) {
			t = list.get(0);
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	private <T> T readRecursion(Element bean) throws Exception {
		Attribute xmlBean = bean.attribute(BeanXMLConstant.CLASS_PROPERTY);
		Class<T> clazz = null;
		if (xmlBean != null) {
			clazz = (Class<T>) Class.forName(xmlBean.getValue());
		} else {
			clazz = (Class<T>) Class.forName(bean.element(
					BeanXMLConstant.CLASS_PROPERTY).getText());
		}
		T o = clazz.newInstance();
		ReflectUtil ru = new ReflectUtil(o);
		Field[] fields = ru.getFields();
		for (Field f : fields) {
			String n = f.getName();
			Method m = ru.getMethod("set" + StringUtil.toUpCaseFirst(n));
			if (m != null) {
				Annotation[] annotation = f.getAnnotations();
				if (annotation != null && annotation.length > 0) {
					for (Annotation anno : annotation) {
						XmlTag tag = (XmlTag) anno;
						String type = tag.type();
						String value = tag.value();
						boolean canRead = tag.canRead();
						if (canRead) {
							if (XmlTagType.attriType.equals(type)) {
								if ("clazz".equals(n)) {
									n = "class";
								}
								Attribute a = bean.attribute(n);
								if (a != null) {
									m.invoke(o, a.getData());
								}
							} else if (XmlTagType.classType.equals(type)) {
								Element el = bean.element(n);
								if (el != null) {
									if ("".equals(value)) {
										value = el.attribute(
												BeanXMLConstant.CLASS_PROPERTY)
												.getValue();
									}
									Object object = Class.forName(value)
											.newInstance();
									// 递归
									object = readRecursion(el);
									m.invoke(o, object);
								}
							} else if (XmlTagType.listClassType.equals(type)) {
								List<?> eList = bean.elements(n);
								if (eList != null) {
									List<Object> list = new ArrayList<Object>();
									for (Iterator<?> it = eList.iterator(); it
											.hasNext();) {
										Element e = (Element) it.next();
										if ("".equals(value)) {
											value = e
													.attribute(
															BeanXMLConstant.CLASS_PROPERTY)
													.getValue();
										}
										Object object = Class.forName(value)
												.newInstance();
										// 递归
										object = readRecursion(e);
										list.add(object);
									}
									m.invoke(o, list);
								}
							} else if (XmlTagType.listElementType.equals(type)) {
								List<?> eList = bean.elements(n);
								if (eList != null) {
									List<String> list = new ArrayList<String>();
									for (Iterator<?> it = eList.iterator(); it
											.hasNext();) {
										Element e = (Element) it.next();
										list.add(e.getText());
									}
									m.invoke(o, list);
								}
							} else if (XmlTagType.elementType.equals(type)) {
								Element a = bean.element(n);
								if (a != null) {
									if ("clazz".equals(n)) {
										n = "class";
									}
									if (a != null) {
										m.invoke(o, a.getData());
									}
								}
							} else if ("".equals(type)) {
								Element a = bean.element(n);
								if (a != null) {
									if ("clazz".equals(n)) {
										n = "class";
									}
									if (a != null) {
										m.invoke(o, a.getData());
									}
								}
							}
						}
					}
				} else {
					m.invoke(o, bean.element(n).getData());
				}
			}
		}

		return o;
	}
}
