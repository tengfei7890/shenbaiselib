package com.cfuture08.eweb4j.orm.config;

import java.io.File;
import java.util.List;

import com.cfuture08.eweb4j.cache.ORMConfigBeanCache;
import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.CheckConfigBean;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.orm.config.bean.ORMConfigBean;
import com.cfuture08.util.BeanXMLUtil;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.StringUtil;

/**
 * ORM configuration
 * 
 * @author weiwei
 * 
 */
public class ORMConfig {
	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		if (cb == null)
			return null;

		List<String> ormXmlFilePaths = cb.getOrm().getOrmXmlFiles().getPath();
		for (String filePath : ormXmlFilePaths) {
			if (filePath == null || filePath.length() == 0)
				continue;

			File configFile = new File(StringUtil.uriDecoding(FileUtil
					.getRootDir(ORMConfig.class) + filePath));
			try {
				List<ORMConfigBean> ormList = BeanXMLUtil.getBeanXMLReader(
						configFile).read();
				if (ormList == null || ormList.isEmpty())
					error = ConfigInfoCons.CANNOT_READ_ANY_CONFIG_INFO;
				else {
					for (ORMConfigBean orm : ormList) {
						String error1 = CheckConfigBean.checkORM(orm, filePath);
						if (error1 != null)
							if (error == null)
								error = error1;
							else
								error += error1;

						String error2 = CheckConfigBean.checkORMProperty(
								orm.getProperty(), ormList, orm.getId(),
								filePath);
						if (error2 != null)
							if (error == null)
								error = error2;
							else
								error += error2;
					}

					if (error == null) {
						for (ORMConfigBean orm : ormList) {
							if (!"".equals(orm.getClazz()))
								ORMConfigBeanCache.add(
										Class.forName(orm.getClazz()), orm);

						}
						// ------log-------
						LogFactory.getORMLogger("INFO").write(
								ConfigInfoCons.READ_CONFIG_INFO_SUCCESS);
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
							ORMConfigBeanCreator.getORMBean()).write();
					StringBuilder sb = new StringBuilder(
							ConfigInfoCons.REPAIR_INFO);
					sb.append(StringUtil.getExceptionString(e));
					error = sb.toString();

					LogFactory.getORMLogger("ERROR").write(error);
				} catch (Exception e1) {
					e1.printStackTrace();
					StringBuilder sb3 = new StringBuilder(
							StringUtil.getExceptionString(e1));
					sb3.append(ConfigInfoCons.CANNOT_REPAIR_CONFIG_FILE);
					error = sb3.toString();
					LogFactory.getORMLogger("ERROR").write(error);
				}
			}
		}

		if (error != null)
			ORMConfigBeanCache.clear();

		return error;
	}
}
