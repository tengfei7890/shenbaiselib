package com.cfuture08.eweb4j.mvc.config.cache;

import java.util.HashMap;

import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;

public class ActionClassCache {
	private static final HashMap<String, Class<?>> ht = new HashMap<String, Class<?>>();

	public static boolean containsKey(String beanID) {
		return ht.containsKey(beanID);
	}

	public static boolean containsValue(Class<?> clazz) {
		return ht.containsValue(clazz);
	}

	public static void add(String beanID, Class<?> o) {
		if (beanID != null && o != null) {
			ConfigBean cb = (ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID);
			String debug = (cb == null ? "true" : cb.getMvc().getDebug());
			String info = null;
			if (!ht.containsKey(beanID)) {
				ht.put(beanID, o);
				info = "ActionClassCache:add...finished..." + beanID;
				if ("1".equals(debug) || "true".equals(debug)) {
					System.out.println(info);
				}
				LogFactory.getMVCLogger("INFO").write(info);
			}
		}
	}

	public static Class<?> get(String beanID) {
		return ht.get(beanID);
	}

	public static void clear() {
		if (!ht.isEmpty()) {
			ht.clear();
		}
	}
}
