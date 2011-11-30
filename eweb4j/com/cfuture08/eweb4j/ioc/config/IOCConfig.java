package com.cfuture08.eweb4j.ioc.config;

import java.io.File;
import java.util.List;

import com.cfuture08.eweb4j.cache.IOCConfigBeanCache;
import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.CheckConfigBean;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.ioc.config.bean.IOCConfigBean;
import com.cfuture08.util.BeanXMLUtil;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.StringUtil;

/**
 * IOC独立组件的启动配置
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class IOCConfig {
	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		if (cb == null)
			return null;

		List<String> iocXmlFilePaths = cb.getIoc().getIocXmlFiles().getPath();
		for (String filePath : iocXmlFilePaths) {
			if (filePath == null || filePath.length() == 0)
				continue;

			File configFile = new File(StringUtil.uriDecoding(FileUtil
					.getRootDir(IOCConfig.class) + filePath));
			try {
				List<IOCConfigBean> iocList = BeanXMLUtil.getBeanXMLReader(
						configFile).read();
				if (iocList == null || iocList.isEmpty())
					error = ConfigInfoCons.CANNOT_READ_CONFIG_FILE;
				else {
					for (IOCConfigBean ioc : iocList) {
						String error1 = CheckConfigBean.checkIOC(ioc, filePath);
						if (error1 != null)
							if (error == null)
								error = error1;
							else
								error += error1;

						String error2 = CheckConfigBean
								.checkIOCJnject(ioc.getInject(), iocList,
										ioc.getId(), filePath);
						if (error2 != null)
							if (error == null)
								error = error2;
							else
								error += error2;

					}

					if (error == null) {
						for (IOCConfigBean ioc : iocList)
							if (!"".equals(ioc.getClazz())) {
								String clazz = ioc.getClazz();
								IOCConfigBeanCache.add(Class.forName(clazz),
										ioc);
								if (!"".equals(ioc.getId()))
									IOCConfigBeanCache.add(ioc.getId(), ioc);

							}

						// ------log-------
						LogFactory.getIOCLogger("INFO").write(
								ConfigInfoCons.READ_CONFIG_FILE_SUCCESS);
						// ------log-------
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

				try {
					// 保存为备份文件
					File tf = new File(configFile.getAbsolutePath() + ".back"
							+ StringUtil.getNowTime("_MMddHHmmss"));
					FileUtil.copy(configFile, tf);
					BeanXMLUtil.getBeanXMLWriter(configFile,
							IOCConfigBeanCreator.getIOCBean()).write();
					StringBuilder sb = new StringBuilder(
							ConfigInfoCons.REPAIR_FILE);
					sb.append(StringUtil.getExceptionString(e));
					error = sb.toString();

					LogFactory.getIOCLogger("ERROR").write(error);
				} catch (Exception e1) {
					e1.printStackTrace();
					StringBuilder sb2 = new StringBuilder(
							ConfigInfoCons.CANNOT_REPAIR_FILE);

					sb2.append(StringUtil.getExceptionString(e1));
					error = sb2.toString();

					LogFactory.getIOCLogger("ERROR").write(error);
				}
			}
		}

		if (error != null)
			IOCConfigBeanCache.clear();

		return error;
	}
}
