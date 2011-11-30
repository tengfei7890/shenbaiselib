package com.cfuture08.eweb4j.orm.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.cache.ORMConfigBeanCache;
import com.cfuture08.eweb4j.orm.config.annotation.Column;
import com.cfuture08.eweb4j.orm.config.annotation.Id;
import com.cfuture08.eweb4j.orm.config.annotation.Ignore;
import com.cfuture08.eweb4j.orm.config.annotation.Many;
import com.cfuture08.eweb4j.orm.config.annotation.ManyMany;
import com.cfuture08.eweb4j.orm.config.annotation.One;
import com.cfuture08.eweb4j.orm.config.annotation.Pk;
import com.cfuture08.eweb4j.orm.config.annotation.Table;
import com.cfuture08.eweb4j.orm.config.bean.ORMConfigBean;
import com.cfuture08.eweb4j.orm.config.bean.Property;
import com.cfuture08.util.ClassUtil;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.ReflectUtil;

/**
 * Persistent obj annotation read to cache
 * 
 * @author weiwei
 * 
 */
public class PojoAnnotationConfig {
	/**
	 * 读取注解，并存入配置缓存中
	 * 
	 * @param scanPackage
	 * @return
	 */
	public static String readAnnotation(List<String> scanPackages) {
		String error = null;
		try {
			if (scanPackages != null) {
				for (String scanPackage : scanPackages) {
					if (scanPackage == null || scanPackage.length() == 0)
						continue;

					String classDir = FileUtil
							.getRootDir(PojoAnnotationConfig.class) + "classes";
					File dir = null;

					if (".".equals(scanPackage)) {
						scanPackage = "";
						dir = new File(classDir);
					} else
						dir = new File(classDir + File.separator
								+ scanPackage.replace(".", File.separator));

					if (dir.isDirectory())
						scanPackage(dir, scanPackage);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder(e.toString());
			for (StackTraceElement ste : e.getStackTrace()) {
				sb.append("\n").append(ste.toString());
			}

			error = sb.toString();
		}
		return error;
	}

	/**
	 * 扫描包目录下的所有文件
	 * 
	 * @param dir
	 * @param actionPackage
	 * @throws Exception
	 */
	private static void scanPackage(File dir, String actionPackage)
			throws Exception {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					// 递归
					if (file.isDirectory()) {
						if (actionPackage.length() == 0)
							scanPackage(file, file.getName());
						else
							scanPackage(file,
									actionPackage + "." + file.getName());
					} else if (file.isFile()) {
						String fname = file.getName();
						if (!fname.endsWith(".class"))
							continue;

						String clsName = actionPackage + "."
								+ fname.substring(0, fname.lastIndexOf("."));
						if (clsName == null || "".equals(clsName))
							continue;

						Class<?> clazz = Class.forName(clsName);
						if (clazz != null) {

							Table tableAnn = clazz.getAnnotation(Table.class);
							if (tableAnn == null && !clsName.endsWith("PO")
									&& !clsName.endsWith("POJO")
									&& !clsName.endsWith("Entity")
									&& !clsName.endsWith("Model")) {
								continue;
							}

							String table = tableAnn == null ? "" : tableAnn
									.value();
							table = "".equals(table.trim()) ? clazz
									.getSimpleName().replace("PO", "")
									.replace("POJO", "").replace("Entity", "")
									.replace("Model", "") : table;
							ORMConfigBean ormBean = new ORMConfigBean();
							ormBean.setClazz(clazz.getName());
							ormBean.setId(clazz.getSimpleName());
							ormBean.setTable(table);
							ReflectUtil ru;

							ru = new ReflectUtil(clazz);
							List<Property> pList = new ArrayList<Property>();
							for (Field f : ru.getFields()) {

								Column colAnn = f.getAnnotation(Column.class);
								Many manyAnn = f.getAnnotation(Many.class);
								One oneAnn = f.getAnnotation(One.class);
								ManyMany mmAnn = f
										.getAnnotation(ManyMany.class);
								if (colAnn == null
										&& (manyAnn != null || oneAnn != null || mmAnn != null))
									continue;

								if (ClassUtil.isPojo(f.getType()))
									continue;

								Ignore igAnn = f.getAnnotation(Ignore.class);
								if (igAnn != null)
									continue;

								Id idAnn = f.getAnnotation(Id.class);
								Pk pkAnn = f.getAnnotation(Pk.class);
								String name = f.getName();
								Property p = new Property();
								if (idAnn != null
										|| "id".equalsIgnoreCase(name)) {
									p.setAutoIncrement("1");
									p.setPk("1");
								}
								if (pkAnn != null) {
									p.setPk("1");
								}

								String column = colAnn == null ? "" : colAnn
										.value();
								column = "".equals(column.trim()) ? name
										: column;
								p.setName(name);
								p.setColumn(column);
								p.setType(f.getType().getName());
								pList.add(p);
							}
							ormBean.setProperty(pList);
							ORMConfigBeanCache.add(clazz, ormBean);
						}
					}
				}
			}
		}
	}
}
