package com.cfuture08.eweb4j.cache;

import java.util.HashMap;

import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.orm.dao.config.DAOConfigConstant;
import com.cfuture08.eweb4j.orm.dao.config.bean.DBInfoConfigBean;

/**
 * 全局单件对象缓存
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class DBInfoConfigBeanCache {
	private static final HashMap<String, DBInfoConfigBean> ht = new HashMap<String, DBInfoConfigBean>();

	public static boolean containsKey(String beanID) {
		return ht.containsKey(beanID);
	}

	public static void add(String beanID, DBInfoConfigBean o) {
		if (beanID != null && o != null) {
			ConfigBean cb = (ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID);
			String debug = (cb == null ? "true" : cb.getDebug());
			String info = null;
			if (!ht.containsKey(beanID)) {
				ht.put(beanID, o);
				info = "DBInfoConfigBeanCache:add...finished..." + beanID;
				if ("1".equals(debug) || "true".equals(debug)) {
					System.out.println(info);
				}
			}
			LogFactory.getORMLogger("INFO").write(info);
		}
	}

	public static DBInfoConfigBean get(String beanID) {
		return ht.get(beanID);
	}

	public static DBInfoConfigBean get() {
		return ht.get(DAOConfigConstant.MYDBINFO);
	}

	public static void clear() {
		if (!ht.isEmpty()) {
			ht.clear();
		}
	}
}
