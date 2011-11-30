package com.cfuture08.eweb4j.component.dwz.menu.exception;

public class DuplicateMenuNameException extends Exception {

	private static final long serialVersionUID = -6126039992066300740L;
	
	public DuplicateMenuNameException(){
		super("菜单名字与现有的重复");
	}

}
