package com.cfuture08.util.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;

import com.cfuture08.util.ReflectUtil;
import com.cfuture08.util.StringUtil;
import com.cfuture08.util.xml.tag.XmlTag;
import com.cfuture08.util.xml.tag.XmlTagType;

public class BeanXMLWriter implements XMLWriter {
	private File file;
	private List<?> list;

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public List<?> getList() {
		return this.list;
	}

	public BeanXMLWriter() {
	}

	public BeanXMLWriter(File file) {
		this.setFile(file);
	}

	public BeanXMLWriter(File file, List<?> list) {
		this.setFile(file);
		this.setList(list);
	}

	public BeanXMLWriter(File file, Class<?>... clazzs) {
		this.setFile(file);
		List<Class<?>> list = new ArrayList<Class<?>>();
		for (Class<?> c : clazzs) {
			list.add(c);
		}

		this.setList(list);
	}
	public <T> BeanXMLWriter(File file, T... ts){
		this.setFile(file);
		List<T> list = new ArrayList<T>();
		for (T t : ts){
			list.add(t);
		}
		
		this.setList(list);
	}
	public <T> BeanXMLWriter(File file, Class<T> clazz) {
		this.setFile(file);
		List<T> list = new ArrayList<T>();
		try {
			list.add(clazz.newInstance());
			this.setList(list);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public <T> BeanXMLWriter(File file, T t) {
		this.setFile(file);
		List<T> list = new ArrayList<T>();
		list.add(t);
		this.setList(list);
	}

	public File write() throws Exception {
		Document doc = DocumentHelper.createDocument();
		Element beans = doc.addElement(BeanXMLConstant.ROOT_ELEMENT);
		Element bean;
		for (Object t : this.list) {
			bean = beans.addElement(BeanXMLConstant.SUBROOT_ELEMENT);
			// 递归
			writeRecursion(bean, t);
		}

		// 读取文件
		FileOutputStream fos = new FileOutputStream(this.file);
		// 设置文件编码
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 创建写文件方法
		org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter(fos,
				format);
		// 写入文件
		xmlWriter.write(doc);
		// 关闭
		fos.close();
		xmlWriter.close();
		return this.file;
	}

	private <T> void writeRecursion(Element bean, T t) throws Exception {
		ReflectUtil ru = new ReflectUtil(t);
		Field[] fields = ru.getFields();
		Element property;
		for (Field f : fields) {
			String n = f.getName();
			Method m = ru.getMethod("get" + StringUtil.toUpCaseFirst(n));
			if (m != null) {
				if ("clazz".equals(n)) {
					n = "class";
				}
				Object v = m.invoke(t);
				if (v == null) {
					v = "";
				}
				Annotation[] annotation = f.getAnnotations();
				if (annotation != null && annotation.length > 0) {
					for (Annotation anno : annotation) {
						XmlTag tag = (XmlTag) anno;
						String type = tag.type();
						String value = tag.value();
						boolean canWrite = tag.canWrite();
						if (canWrite) {
							if (XmlTagType.classType.equals(type)) {
								// 属性为class，进入递归
								property = bean.addElement(n);
								writeRecursion(property, Class.forName(value)
										.cast(v));
							} else if (XmlTagType.attriType.equals(type)) {
								if (v == null || "".equals(v)) {
									v = value;
								}

								bean.addAttribute(n, String.valueOf(v));
							} else if (XmlTagType.listClassType.equals(type)) {
								List<?> list = (List<?>) v;
								for (Iterator<?> it = list.iterator(); it
										.hasNext();) {
									property = bean.addElement(n);
									writeRecursion(property, Class.forName(
											value).cast(it.next()));
								}
							} else if (XmlTagType.listElementType.equals(type)) {
								List<?> list = (List<?>) v;
								for (Iterator<?> it = list.iterator(); it
										.hasNext();) {
									Object v2 = it.next();
									if (v2 == null || "".equals(v2)) {
										v2 = value;
									}
									property = bean.addElement(n);
									property.addText(String.valueOf(v2));
								}
							} else if (XmlTagType.elementType.equals(type)) {
								property = bean.addElement(n);
								if (v == null || "".equals(v)) {
									v = value;
								}
								property.addText(String.valueOf(v));
							} else if ("".equals(type)) {
								property = bean.addElement(n);
								if (v == null || "".equals(v)) {
									v = value;
								}
								property.addText(String.valueOf(v));
							}
						}
					}
				} else {
					property = bean.addElement(n);
					// 没有注解的情况下
					property.addText(String.valueOf(v));
				}
			}
		}
	}
}
