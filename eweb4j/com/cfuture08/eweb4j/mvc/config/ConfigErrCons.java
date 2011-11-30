package com.cfuture08.eweb4j.mvc.config;

public interface ConfigErrCons {
	public static final String CANNOT_READ_CONFIG_INFO = "ActionConfig.class : 读取不了任何配置文件信息！已经重写了配置文件，请重新填写完整，然后启动框架。";
	public static final String CONFIG_INFO_READ_ERROR = "ActionConfig.class : 配置文件读取错误，已重写。异常捕获：";
	public static final String CANNOT_REPAIR_CONFIG_FILE = "ActionConfig.class : 无法重写配置文件！异常捕获:";
}
