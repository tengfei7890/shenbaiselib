package com.cfuture08.eweb4j.cache;

import java.util.HashMap;

import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.orm.config.bean.ORMConfigBean;

/**
 * ORM组件缓存
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ORMConfigBeanCache {
	private static final HashMap<Object, ORMConfigBean> ht = new HashMap<Object, ORMConfigBean>();

	public static boolean containsKey(String beanID) {
		return ht.containsKey(beanID);
	}

	public static boolean containsKey(Class<?> clazz) {
		return ht.containsKey(clazz);
	}

	public static void add(String beanID, ORMConfigBean o) {
		if (beanID != null && o != null) {
			ConfigBean cb = (ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID);
			String debug = (cb == null ? "true" : cb.getOrm().getDebug());
			String info = null;
			if (!ht.containsKey(beanID)) {
				ht.put(beanID, o);
				info = "ORMConfigBeanCache:add...finished..." + beanID;
				if ("1".equals(debug) || "true".equals(debug)) {
					System.out.println(info);
				}
				LogFactory.getIOCLogger("INFO").write(info);
			}
		}
	}

	public static void add(Class<?> clazz, ORMConfigBean o) {
		if (clazz != null && o != null) {
			ConfigBean cb = (ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID);
			String debug = (cb == null ? "true" : cb.getOrm().getDebug());
			String info = null;
			if (!ht.containsKey(clazz)) {
				ht.put(clazz, o);
				info = "ORMConfigBeanCache:add...finished..." + clazz;
				if ("1".equals(debug) || "true".equals(debug)) {
					System.out.println(info);
				}
				LogFactory.getIOCLogger("INFO").write(info);
			}
		}
	}

	public static ORMConfigBean get(String beanID) {
		ORMConfigBean o = null;
		if (ht.containsKey(beanID)) {
			o = ht.get(beanID);
		}

		return o;
	}

	public static ORMConfigBean get(Class<?> clazz) {
		ORMConfigBean o = null;
		if (ht.containsKey(clazz)) {
			o = ht.get(clazz);
		}

		return o;
	}

	public static void clear() {
		if (!ht.isEmpty()) {
			ht.clear();
		}
	}
}
