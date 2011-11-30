package com.cfuture08.eweb4j.cache;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.mvc.config.bean.InterConfigBean;
/**
 * 拦截器缓存
 * @author CFurure.aw
 *
 */
public class InterConfigBeanCache {
	private static final List<InterConfigBean> ht = new ArrayList<InterConfigBean>();

	public static List<InterConfigBean> getList() {
		return ht;
	}

	public static void add(InterConfigBean o) {
		if (o != null) {
			ConfigBean cb = (ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID);
			String debug = (cb == null ? "true" : cb.getMvc().getDebug());
			String info = null;
			if (!ht.contains(o)) {
				ht.add(o);
				info = "InterConfigBean:add...finished..." + o.getClazz();
				if ("1".equals(debug) || "true".equals(debug)) {
					System.out.println(info);
				}
				LogFactory.getIOCLogger("INFO").write(info);
			}
		}
	}

	public static void clear() {
		if (!ht.isEmpty()) {
			ht.clear();
		}
	}
}
