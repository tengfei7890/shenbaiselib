package com.cfuture08.eweb4j.config;

import java.io.File;

import com.cfuture08.eweb4j.cache.ActionConfigBeanCache;
import com.cfuture08.eweb4j.cache.IOCConfigBeanCache;
import com.cfuture08.eweb4j.cache.ORMConfigBeanCache;
import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.ioc.config.IOCConfig;
import com.cfuture08.eweb4j.mvc.config.ActionAnnotationConfig;
import com.cfuture08.eweb4j.mvc.config.ActionConfig;
import com.cfuture08.eweb4j.mvc.config.InterceptorConfig;
import com.cfuture08.eweb4j.orm.config.ORMConfig;
import com.cfuture08.eweb4j.orm.config.PojoAnnotationConfig;
import com.cfuture08.eweb4j.orm.dao.config.DAOConfig;
import com.cfuture08.util.BeanXMLUtil;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.StringUtil;

/**
 * EWeb4J配置
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class EWeb4JConfig {
	public synchronized static String start() {
		String error = null;
		boolean readXml = true;
		if (ConfigConstant.SUCCESS_START.equals(String.valueOf(SingleBeanCache
				.get(ConfigConstant.SUCCESS_START)))) {
			ConfigBean cb = (ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID);
			String debug = (cb == null ? "true" : cb.getDebug());
			if ("true".equals(debug) || "1".equals(debug)) {
				// 如果开了DEBUG,清空缓存，重新读取配置文件
				SingleBeanCache.clear();
				ORMConfigBeanCache.clear();
				IOCConfigBeanCache.clear();
				ActionConfigBeanCache.clear();
				System.out.println("EWeb4J清空缓存");
				readXml = true;
			} else {
				// 否则，不需要读取配置文件
				readXml = false;
			}
		}

		if (readXml) {
			// 1.读取配置文件
			try {
				ConfigBean cb = (ConfigBean) BeanXMLUtil.getBeanXMLReader(
						new File(ConfigConstant.XMLFILE_PATH)).readOne();
				if (cb == null) {
					error = "EWeb4JConfig.class : 读取不了任何配置文件信息！已经重写了配置文件，请重新填写完整，然后启动框架。";
				} else {
					// 检查配置信息格式是否填写正确
					String error1 = CheckConfigBean.checkEWeb4JConfigBean(cb);
					if (error1 != null)
						error = error1;

					String error2 = CheckConfigBean.checkEWeb4JIOCPart(cb
							.getIoc());
					if (error2 != null)
						if (error == null)
							error = error2;
						else
							error += error2;

					String error3 = CheckConfigBean.checkIOCXml(cb.getIoc()
							.getIocXmlFiles());
					if (error3 != null)
						if (error == null)
							error = error3;
						else
							error += error3;

					String error4 = CheckConfigBean.checkEWeb4JORMPart(cb
							.getOrm());
					if (error4 != null)
						if (error == null)
							error = error4;
						else
							error += error4;

					String error5 = CheckConfigBean.checkORMXml(cb.getOrm()
							.getOrmXmlFiles());
					if (error5 != null)
						if (error == null)
							error = error5;
						else
							error += error5;

					String error6 = CheckConfigBean.checkEWeb4JMVCPart(cb
							.getMvc());
					if (error6 != null)
						if (error == null)
							error = error6;
						else
							error += error6;

					String error7 = CheckConfigBean.checkMVCActionXmlFile(cb
							.getMvc().getActionXmlFiles());
					if (error7 != null)
						if (error == null)
							error = error7;
						else
							error += error7;

					String error8 = CheckConfigBean.checkInter(cb.getMvc()
							.getInterXmlFiles());
					if (error8 != null)
						if (error == null)
							error = error8;
						else
							error += error8;

					if (error == null) {
						// 验证通过，将读取到的信息放入缓存池中
						SingleBeanCache.add(ConfigConstant.CONFIGBEAN_ID, cb);
						SingleBeanCache.add(cb.getClass(), cb);
						// ------log-------
						String debug = ((ConfigBean) SingleBeanCache
								.get(ConfigConstant.CONFIGBEAN_ID)).getDebug();
						if ("true".equals(debug) || "1".equals(debug)) {
							String info = "EWeb4J框架启动配置文件验证通过，已将读取到的配置信息存入缓存;";
							System.out.println(info);
						}
						// ------log-------
						// 继续验证其他组件配置信息
						if ("true".equals(cb.getOrm().getOpen())
								|| "1".equals(cb.getOrm().getOpen())) {
							error = DAOConfig.check();
							String error10 = PojoAnnotationConfig
									.readAnnotation(cb.getOrm()
											.getScanPojoPackage().getPath());
							if (error10 != null)
								if (error == null)
									error = error10;
								else
									error += error10;

							String error11 = ORMConfig.check();
							if (error11 != null)
								if (error == null)
									error = error11;
								else
									error += error11;

						}
						if ("true".equals(cb.getIoc().getOpen())
								|| "1".equals(cb.getIoc().getOpen())) {
							String error10 = IOCConfig.check();
							if (error10 != null)
								if (error == null)
									error = error10;
								else
									error += error10;

						}
						if ("true".equals(cb.getMvc().getOpen())
								|| "1".equals(cb.getMvc().getOpen())) {
							String error20 = ActionAnnotationConfig
									.readAnnotation(cb.getMvc()
											.getScanActionPackage().getPath());
							if (error20 != null)
								if (error == null)
									error = error20;
								else
									error += error20;

							String error11 = ActionConfig.check();
							if (error11 != null)
								if (error == null)
									error = error11;
								else
									error += error11;

							String error12 = InterceptorConfig.check();
							if (error12 != null)
								if (error == null)
									error = error12;
								else
									error += error12;

						}
					}
				}

			} catch (Exception e) {
				// 重写配置文件
				try {
					// 保存为备份文件
					File file = new File(ConfigConstant.XMLFILE_PATH);
					FileUtil.copy(
							file,
							new File(ConfigConstant.XMLFILE_PATH + ".back"
									+ "_" + StringUtil.getNowTime("MMddHHmmss")));
					BeanXMLUtil.getBeanXMLWriter(file,
							ConfigBeanCreator.getConfigBean()).write();
					error = StringUtil.getNowTime()
							+ "EWeb4JConfig : 配置文件读取错误，已重写。异常捕获："
							+ StringUtil.getExceptionString(e);
					System.out.println(error);
					e.printStackTrace();
				} catch (Exception e1) {
					error = StringUtil.getNowTime()
							+ "EWeb4JConfig : 无法重写配置文件！异常捕获："
							+ StringUtil.getExceptionString(e1);
					System.out.println(error);
					e1.printStackTrace();
				}
			}

			if (error != null) {
				SingleBeanCache.clear();
				ORMConfigBeanCache.clear();
				IOCConfigBeanCache.clear();
				ActionConfigBeanCache.clear();
			} else {
				SingleBeanCache.add(ConfigConstant.SUCCESS_START,
						ConfigConstant.SUCCESS_START);
			}
		}

		return error;
	}

	public static boolean isIocDebug() {
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		String debug = (cb == null ? "true" : cb.getIoc().getDebug());

		if ("1".equals(debug) || "true".equals(debug))
			return true;
		else
			return false;

	}
}
