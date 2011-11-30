package com.cfuture08.eweb4j.orm.dao;

import com.cfuture08.eweb4j.orm.jdbc.JdbcUtilException;

public class DAOException extends JdbcUtilException {
	private static final long serialVersionUID = 7567279073825068097L;
	public DAOException(String info){
		super(info);
	}
}
