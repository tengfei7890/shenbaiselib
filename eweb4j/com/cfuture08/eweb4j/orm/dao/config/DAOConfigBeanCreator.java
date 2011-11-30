package com.cfuture08.eweb4j.orm.dao.config;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.orm.dao.config.bean.DBInfoConfigBean;
import com.cfuture08.eweb4j.orm.dao.config.bean.Property;
/**
 * DAO组件配置bean的创建
 * @author cfuture.aw
 * @since v1.a.0
 */
public class DAOConfigBeanCreator {
	public static DBInfoConfigBean getDAOBean(){
		DBInfoConfigBean dcb = new DBInfoConfigBean();
		List<Property> properties = new ArrayList<Property>();
		Property p = new Property();
		p.setKey("");
		p.setValue("");
		properties.add(p);
		dcb.setProperties(properties);
		return dcb;
	}
}
