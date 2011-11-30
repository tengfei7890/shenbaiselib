package com.cfuture08.eweb4j.config;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.config.bean.ActionXmlFile;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.bean.ConfigIOC;
import com.cfuture08.eweb4j.config.bean.ConfigMVC;
import com.cfuture08.eweb4j.config.bean.ConfigORM;
import com.cfuture08.eweb4j.config.bean.DBInfoXmlFiles;
import com.cfuture08.eweb4j.config.bean.IOCXmlFiles;
import com.cfuture08.eweb4j.config.bean.InterXmlFile;
import com.cfuture08.eweb4j.config.bean.ORMXmlFiles;
import com.cfuture08.eweb4j.config.bean.ScanActionPackage;
import com.cfuture08.eweb4j.config.bean.ScanPojoPackage;

/**
 * EWeb4J存取配置信息的bean的创建
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ConfigBeanCreator {
	public static ConfigBean getConfigBean() {
		ConfigBean configBean = new ConfigBean();
		ConfigIOC ioc = new ConfigIOC();
		IOCXmlFiles iocXmlFiles = new IOCXmlFiles();
		List<String> paths1 = new ArrayList<String>();
		paths1.add("");
		iocXmlFiles.setPath(paths1);
		ioc.setIocXmlFiles(iocXmlFiles);
		configBean.setIoc(ioc);

		ConfigORM orm = new ConfigORM();
		ORMXmlFiles ormXmlFiles = new ORMXmlFiles();
		ormXmlFiles.setPath(paths1);
		orm.setOrmXmlFiles(ormXmlFiles);
		ScanPojoPackage spp = new ScanPojoPackage();
		List<String> paths2 = new ArrayList<String>();
		paths2.add(".");
		spp.setPath(paths2);
		orm.setScanPojoPackage(spp);

		DBInfoXmlFiles dbInfoXmlFiles = new DBInfoXmlFiles();
		dbInfoXmlFiles.setPath(paths1);
		orm.setDbInfoXmlFiles(dbInfoXmlFiles);

		configBean.setOrm(orm);

		ConfigMVC mvc = new ConfigMVC();
		ActionXmlFile actionFiles = new ActionXmlFile();
		actionFiles.setPath(paths1);
		mvc.setActionXmlFiles(actionFiles);
		
		InterXmlFile intFiles = new InterXmlFile();
		intFiles.setPath(paths1);
		mvc.setInterXmlFiles(intFiles);
		
		ScanActionPackage sap = new ScanActionPackage();
		sap.setPath(paths2);
		mvc.setScanActionPackage(sap);
		
		configBean.setMvc(mvc);

		return configBean;
	}
}
