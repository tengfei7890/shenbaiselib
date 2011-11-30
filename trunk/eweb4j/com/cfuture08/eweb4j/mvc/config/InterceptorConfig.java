package com.cfuture08.eweb4j.mvc.config;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.cfuture08.eweb4j.cache.InterConfigBeanCache;
import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.CheckConfigBean;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.mvc.config.bean.InterConfigBean;
import com.cfuture08.util.BeanXMLUtil;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.StringUtil;

public class InterceptorConfig {
	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);

		if (cb != null) {
			for (String filePath : cb.getMvc().getInterXmlFiles().getPath()) {
				if (filePath != null && filePath.length() > 0) {
					File configFile = new File(StringUtil.uriDecoding(FileUtil
							.getRootDir(InterceptorConfig.class) + filePath));
					try {
						List<InterConfigBean> interList = BeanXMLUtil
								.getBeanXMLReader(configFile).read();
						if (interList == null || interList.isEmpty()) {
							error = "InterConfig.class : 读取不了任何配置文件信息！已经重写了配置文件，请重新填写完整，然后启动框架。";
						} else {
							for (Iterator<InterConfigBean> it = interList
									.iterator(); it.hasNext();) {
								InterConfigBean inter = it.next();
								String error1 = CheckConfigBean.checkMVCInterceptor(inter,
										filePath);
								if (error1 != null) {
									if (error != null) {
										error += error1;
									} else {
										error = error1;
									}
								}

							}

							if (error == null) {
								for (Iterator<InterConfigBean> it = interList
										.iterator(); it.hasNext();) {
									InterConfigBean inter = it.next();
									if (!"".equals(inter.getClazz())) {
										InterConfigBeanCache.add(inter);
									}
								}

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						StringBuilder sb = new StringBuilder(e.toString());
						for (StackTraceElement ste : e.getStackTrace()) {
							sb.append("\n").append(ste.toString());
						}
						try {
							// 保存为备份文件
							File tf = new File(configFile.getAbsolutePath()
									+ ".back"
									+ StringUtil.getNowTime("_MMddHHmmss"));
							FileUtil.copy(configFile, tf);
							BeanXMLUtil.getBeanXMLWriter(configFile,
									MVCConfigBeanCreator.getInterBean())
									.write();

							StringBuilder tsb = new StringBuilder(
									"InterConfig.class : 配置文件读取错误，已重写。异常捕获：");
							tsb.append(sb.toString());
							LogFactory.getMVCLogger("ERROR").write(
									tsb.toString());
							error = tsb.toString();
						} catch (Exception e1) {
							e1.printStackTrace();
							StringBuilder sb2 = new StringBuilder(
									"InterConfig.class : 无法重写配置文件！异常捕获:"
											+ e1.toString());
							for (StackTraceElement ste : e1.getStackTrace()) {
								sb2.append("\n").append(ste.toString());
							}

							LogFactory.getMVCLogger("ERROR").write(
									sb2.toString());
							error = sb2.toString();
						}
					}
				}
			}
			if (error != null) {
				InterConfigBeanCache.clear();
			}
		}
		return error;
	}
}
