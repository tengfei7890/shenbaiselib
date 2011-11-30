package com.cfuture08.eweb4j.config.log;

import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.ioc.config.log.IOCLogger;
import com.cfuture08.eweb4j.mvc.config.log.MVCLogger;
import com.cfuture08.eweb4j.orm.config.log.ORMLogger;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.StringUtil;

public class LogFactory {
	public static Logger getIOCLogger(String level) {
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		String rootDir = StringUtil.uriDecoding(FileUtil
				.getRootDir(LogFactory.class));
		String filePath = cb == null ? "" : rootDir + cb.getIoc().getLogFile();
		String maxSize = cb == null ? "5" : cb.getIoc().getLogMaxSize();
		return new IOCLogger(level, filePath, Integer.parseInt(maxSize));
	}

	public static Logger getMVCLogger(String level) {
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		String rootDir = StringUtil.uriDecoding(FileUtil
				.getRootDir(LogFactory.class));
		String filePath = cb == null ? "" : rootDir + cb.getMvc().getLogFile();
		String maxSize = cb == null ? "5" : cb.getMvc().getLogMaxSize();
		return new MVCLogger(level, filePath, Integer.parseInt(maxSize));
	}

	public static Logger getORMLogger(String level) {
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		String rootDir = StringUtil.uriDecoding(FileUtil
				.getRootDir(LogFactory.class));
		String filePath = cb == null ? "" : rootDir + cb.getOrm().getLogFile();
		String maxSize = cb == null ? "5" : cb.getOrm().getLogMaxSize();
		return new ORMLogger(level, filePath, Integer.parseInt(maxSize));
	}
}
