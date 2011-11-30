package com.cfuture08.eweb4j.component.dwz.menu.exception;

public class MenuDataAccessException extends Exception{

	private static final long serialVersionUID = 1660812307237920664L;
	public MenuDataAccessException(String cause){
		super("菜单数据访问异常,原因："+cause);
	}
}
