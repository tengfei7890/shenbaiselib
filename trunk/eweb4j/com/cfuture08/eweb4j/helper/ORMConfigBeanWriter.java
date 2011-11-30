package com.cfuture08.eweb4j.helper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.orm.config.annotation.Column;
import com.cfuture08.eweb4j.orm.config.annotation.Id;
import com.cfuture08.eweb4j.orm.config.annotation.Pk;
import com.cfuture08.eweb4j.orm.config.annotation.Table;
import com.cfuture08.eweb4j.orm.config.bean.ORMConfigBean;
import com.cfuture08.eweb4j.orm.config.bean.Property;
import com.cfuture08.util.BeanXMLUtil;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.ReflectUtil;
import com.cfuture08.util.StringUtil;

/**
 * 将ORM配置信息写入xml文件
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class ORMConfigBeanWriter {
	/**
	 * 将Class集合写入到目标xml文件中
	 * 
	 * @param filePath
	 *            文件相对路径
	 * @param list
	 * @return
	 */
	public static String write(String filePath, List<Class<?>> list) {
		String error = null;
		if (filePath != null && list != null && !list.isEmpty()) {
			Class<?>[] clss = new Class<?>[list.size()];
			for (int i = 0; i < list.size(); ++i) {
				clss[i] = list.get(i);
			}

			error = write(filePath, clss);
		}

		return error;
	}

	/**
	 * 将某个Class写入到目标xml文件中
	 * 
	 * @param filePath
	 *            文件相对路径
	 * @param clazzs
	 * @return
	 */
	public static String write(String filePath, Class<?>... clazzs) {
		String error = null;
		File file = new File(StringUtil.uriDecoding(FileUtil
				.getRootDir(ORMConfigBeanWriter.class) + filePath));

		if (filePath != null && clazzs != null && clazzs.length > 0) {
			List<ORMConfigBean> ormList = new ArrayList<ORMConfigBean>();
			try {
				for (int i = 0; i < clazzs.length; ++i) {
					Class<?> clazz = clazzs[i];
					Table tableAnn = clazz.getAnnotation(Table.class);
					if (tableAnn == null) {
						continue;
					}

					String table = tableAnn.value();
					table = "".equals(table.trim()) ? clazz.getSimpleName()
							: table;
					ORMConfigBean ormBean = new ORMConfigBean();
					ormBean.setClazz(clazz.getName());
					ormBean.setId(clazz.getSimpleName());
					ormBean.setTable(table);
					ReflectUtil ru;

					ru = new ReflectUtil(clazz);
					List<Property> pList = new ArrayList<Property>();
					for (Field f : ru.getFields()) {
						Column colAnn = f.getAnnotation(Column.class);
						if (colAnn == null) {
							continue;
						}

						Id idAnn = f.getAnnotation(Id.class);
						Pk pkAnn = f.getAnnotation(Pk.class);
						String name = f.getName();
						Property p = new Property();
						if (idAnn != null) {
							p.setAutoIncrement("1");
							p.setPk("1");
						}
						if (pkAnn != null) {
							p.setPk("1");
						}
						String column = colAnn.value();
						column = "".equals(column.trim()) ? name : column;
						p.setName(name);
						p.setColumn(column);
						p.setType(f.getType().getName());
						pList.add(p);
					}
					ormBean.setProperty(pList);
					ormList.add(ormBean);
				}

				BeanXMLUtil.getBeanXMLWriter(file, ormList).write();
			} catch (Exception e) {
				error = e.getMessage();
				e.printStackTrace();
			}

		} else {
			error = "Class参数不能为空";
		}

		return error;
	}

	/**
	 * 将某个包下的所有Class类写入到目标xml文件中
	 * 
	 * @param filePath
	 *            文件相对路径
	 * @param packageName
	 *            包名
	 * @return
	 */
	public static String write(String filePath, String packageName) {
		String error = null;
		String result;
		List<Class<?>> list = new ArrayList<Class<?>>();
		StringBuilder sb = new StringBuilder(
				FileUtil.getRootDir(ORMConfigBeanWriter.class));
		sb.append(File.separator).append("classes");
		for (String t : packageName.split("\\.")) {
			sb.append(new StringBuilder(File.separator).append(t).toString());
		}
		File file = new File(sb.toString());
		File listFile[] = file.listFiles();
		for (File f : listFile) {
			if (f.getName().endsWith(".class")) {
				result = new StringBuilder(packageName)
						.append(".")
						.append(f.getName())
						.toString()
						.substring(
								0,
								new StringBuilder(packageName).append(".")
										.append(f.getName()).toString()
										.lastIndexOf("."));
				try {
					list.add(Class.forName(result));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		error = write(filePath, list);
		return error;
	}
}
