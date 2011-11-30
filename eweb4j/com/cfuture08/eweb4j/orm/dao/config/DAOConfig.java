package com.cfuture08.eweb4j.orm.dao.config;

import java.io.File;

import javax.sql.DataSource;

import com.cfuture08.eweb4j.cache.DBInfoConfigBeanCache;
import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.CheckConfigBean;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.bean.DBInfoXmlFiles;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.orm.dao.config.bean.DBInfoConfigBean;
import com.cfuture08.eweb4j.orm.jdbc.DataSourceCache;
import com.cfuture08.eweb4j.orm.jdbc.DataSourceCreator;
import com.cfuture08.eweb4j.orm.jdbc.DataSourceWrap;
import com.cfuture08.util.BeanXMLUtil;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.StringUtil;

public class DAOConfig {
	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		if (cb == null)
			return null;

		DBInfoXmlFiles dbInfoXmlFiles = cb.getOrm().getDbInfoXmlFiles();
		if (dbInfoXmlFiles == null)
			return ConfigInfoCons.CANNOT_READ_CONFIG_FILE;

		for (String filePath : dbInfoXmlFiles.getPath()) {
			if (filePath == null || filePath.length() == 0)
				continue;

			File configFile = new File(StringUtil.uriDecoding(FileUtil
					.getRootDir(DAOConfig.class) + filePath));
			try {
				DBInfoConfigBean dcb = (DBInfoConfigBean) BeanXMLUtil
						.getBeanXMLReader(configFile).readOne();
				if (dcb == null)
					error = ConfigInfoCons.REPAIR_FILE_INFO;
				else {
					String error1 = CheckConfigBean.checkORMDBInfo(dcb,
							filePath);
					if (error1 == null) {
						DBInfoConfigBeanCache.add(dcb.getDsName(), dcb);
						DataSource ds = DataSourceCreator.getDataSource(dcb);
						DataSourceWrap dsw = new DataSourceWrap(
								dcb.getDsName(), ds);

						String error2 = dsw.getConnection() == null ? ConfigInfoCons.CANNOT_GET_DB_CON
								: null;

						if (error2 != null)
							if (error == null)
								error = error2;
							else
								error += error2;

						else {
							String info = dcb.getDsName()
									+ ConfigInfoCons.READ_CONFIG_INFO_SUCCESS;
							LogFactory.getORMLogger("INFO").write(info);
							// ------log-------
							// 将数据源放入缓存，它可是个重量级对象
							// 此步也是为了共存多个数据源
							DataSourceCache.put(dcb.getDsName(), dsw);
						}
					} else if (error == null)
						error = error1;
					else
						error += error1;

				}
			} catch (Exception e) {
				e.printStackTrace();

				try {
					// 保存为备份文件
					File tf = new File(configFile.getAbsolutePath() + ".back"
							+ StringUtil.getNowTime("_MMddHHmmss"));
					FileUtil.copy(configFile, tf);
					BeanXMLUtil.getBeanXMLWriter(configFile,
							DAOConfigBeanCreator.getDAOBean()).write();

					StringBuilder tsb = new StringBuilder(
							ConfigInfoCons.REPAIR_CONFIG_FILE);
					tsb.append(StringUtil.getExceptionString(e));
					error = tsb.toString();

					LogFactory.getORMLogger("ERROR").write(error);
				} catch (Exception e1) {
					e1.printStackTrace();

					StringBuilder sb2 = new StringBuilder(
							ConfigInfoCons.CANNOT_REPAIR_FILE);
					sb2.append(StringUtil.getExceptionString(e1));
					error = sb2.toString();
					
					LogFactory.getORMLogger("ERROR").write(error);
				}
			}
		}

		if (error != null)
			DBInfoConfigBeanCache.clear();

		return error;
	}
}
