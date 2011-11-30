package com.cfuture08.eweb4j.mvc.config;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.mvc.annotation.Result;
import com.cfuture08.eweb4j.mvc.config.bean.ResultConfigBean;
/**
 * MVC.Result注解 工具类
 * @author weiwei
 *
 */
public class ResultAnnUtil {
	/**
	 * 读@Result注解的信息到配置Cache中
	 * @param resultAnn
	 * @return
	 */
	public static List<ResultConfigBean> readResultAnn(Result resultAnn) {
		List<ResultConfigBean> rList = new ArrayList<ResultConfigBean>();
		String[] name = resultAnn.name();
		String[] type = resultAnn.type();
		String[] location = resultAnn.value();
		for (int a = 0; a < name.length; ++a) {
			ResultConfigBean r = new ResultConfigBean();
			r.setName(name[a]);
			r.setType(type[a]);
			r.setLocation(location[a]);
			rList.add(r);
		}
		
		return rList;
	}
}
