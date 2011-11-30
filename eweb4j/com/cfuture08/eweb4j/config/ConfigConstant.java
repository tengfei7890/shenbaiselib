package com.cfuture08.eweb4j.config;

import com.cfuture08.util.FileUtil;
import com.cfuture08.util.StringUtil;

/**
 * EWeb4J配置常量
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public interface ConfigConstant {
	static String XMLFILE_PATH = StringUtil.uriDecoding(FileUtil
			.getRootDir(EWeb4JConfig.class) + "eweb4j-start-config.xml");
	static String CONFIGBEAN_ID = "ConfigBeanID";
	static String SUCCESS_START = "successStart";
}
