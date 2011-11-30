package com.cfuture08.eweb4j.component.dwz.menu.exception;

public class CannotBeParentMenuException extends Exception{

	private static final long serialVersionUID = 6108173169852635705L;

	public CannotBeParentMenuException(){
		super("自己不能作为自己的父菜单");
	}
}
