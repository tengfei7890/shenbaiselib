package com.cfuture08.eweb4j.mvc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cfuture08.eweb4j.mvc.config.MVCConfigConstant;
import com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean;
/**
 * MVC组件Action接口
 * @author cfuture.aw
 * @since v1.a.0
 */
public interface IAction {
	public static String SUCCESS = MVCConfigConstant.SUCCESS_RESULT;
	public static String ERROR = MVCConfigConstant.ERROR_RESULT;
	public static String INPUT = MVCConfigConstant.INPUT_RESULT;
	public static String AJAX = MVCConfigConstant.AJAX_TYPE;
	public static String JSON = MVCConfigConstant.JSON_TYPE;
	public static String AJAX_OUT = MVCConfigConstant.AJAX_OUT;
	public static String JSON_OUT = MVCConfigConstant.JSON_OUT;
	
	
	IAction init(HttpServletRequest request, HttpServletResponse response, ActionConfigBean contextBean) throws Exception;
	String execute();
}
