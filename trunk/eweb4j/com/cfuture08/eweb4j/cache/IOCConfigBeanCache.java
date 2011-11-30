package com.cfuture08.eweb4j.cache;

import java.util.Hashtable;

import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.ioc.config.bean.IOCConfigBean;

/**
 * IOC组件缓存
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class IOCConfigBeanCache {
	private static final Hashtable<Object, IOCConfigBean> ht = new Hashtable<Object, IOCConfigBean>();

	public static boolean containsKey(String beanID) {
		return ht.containsKey(beanID);
	}

	public static boolean containsKey(Class<?> clazz) {
		return ht.containsKey(clazz);
	}

	public static void add(String beanID, IOCConfigBean o) {
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		String debug = (cb == null ? "true" : cb.getIoc().getDebug());
		String info = null;
		if (!ht.containsKey(beanID)) {
			ht.put(beanID, o);
			info = "IOCConfigBeanCache:add...finished..." + beanID;
			if ("1".equals(debug) || "true".equals(debug)) {
				System.out.println(info);
			}
			LogFactory.getIOCLogger("INFO").write(info);
		}
	}

	public static void add(Class<?> clazz, IOCConfigBean o) {
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		String debug = (cb == null ? "true" : cb.getIoc().getDebug());
		String info = null;
		if (!ht.containsKey(clazz)) {
			ht.put(clazz, o);
			info = "IOCConfigBeanCache:add...finished..." + clazz;
			if ("1".equals(debug) || "true".equals(debug)) {
				System.out.println(info);
			}
			LogFactory.getIOCLogger("INFO").write(info);
		}
	}

	public static IOCConfigBean get(String beanID) {
		IOCConfigBean o = null;
		if (ht.containsKey(beanID)) {
			o = ht.get(beanID);
		}

		return o;
	}

	public static IOCConfigBean get(Class<?> clazz) {
		IOCConfigBean o = null;
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
