package com.cfuture08.eweb4j.component.dwz.menu.exception;

public class MenuNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	public MenuNotFoundException(){
		super("指定的菜单记录不存在或已被删除");
	}

}
