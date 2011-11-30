package com.cfuture08.eweb4j.component.dwz.menu.exception;

public class CannotFindParentMenuException extends Exception {

	private static final long serialVersionUID = 6632546923538443784L;

	public CannotFindParentMenuException(){
		super("找不到父菜单");
	}
}
