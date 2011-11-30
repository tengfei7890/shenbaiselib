package com.cfuture08.eweb4j.orm.config;

public interface ConfigInfoCons {
	public static final String CANNOT_READ_ANY_CONFIG_INFO = "ORMConfig.class : 读取不了任何配置文件信息！已经重写了配置文件，请重新填写完整，然后启动框架。";
	public static final String READ_CONFIG_INFO_SUCCESS = "ORM组件启动配置文件验证通过，已将读取到的配置信息存入缓存;";
	public static final String REPAIR_INFO = "ORMConfig.class : 配置文件读取错误，已重写。异常捕获：";
	public static final String CANNOT_REPAIR_CONFIG_FILE = "ORMConfig.class : 无法重写配置文件！异常捕获: ";
}
