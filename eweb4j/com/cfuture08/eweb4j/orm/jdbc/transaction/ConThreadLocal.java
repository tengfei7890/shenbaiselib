package com.cfuture08.eweb4j.orm.jdbc.transaction;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.cfuture08.eweb4j.orm.dao.config.DAOConfigConstant;

public class ConThreadLocal {
	// 标记当前线程是否是事务操作
	private final static ThreadLocal<Boolean> transLock = new ThreadLocal<Boolean>();
	private final static ThreadLocal<HashMap<String, Connection>> cons = new ThreadLocal<HashMap<String, Connection>>();

	/**
	 * 给当前进行的事务上锁
	 * @param value
	 */
	public static void lock(boolean value) {
		transLock.set(value);
	}

	/**
	 * 获取当前的事务是否被上锁了
	 * @return
	 */
	public static Boolean isTrans() {
		if (transLock.get() == null){
			transLock.set(false);
		}
		return transLock.get();
	}

	/**
	 * 获取当前线程下的所有数据源的连接
	 * @return
	 */
	public static Map<String, Connection> getCons() {
		return cons.get();
	}

	/**
	 * 获取当前线程下的给定数据源名称的连接
	 * @param dsName
	 * @return
	 */
	public static Connection getCon(String dsName) {
		if (dsName == null){
			dsName = DAOConfigConstant.MYDBINFO;
		}
		if (cons.get() == null){
			cons.set(new HashMap<String,Connection>());
		}
		return cons.get().get(dsName);
	}
	
	/**
	 * 获取默认数据源名称的连接
	 * @return
	 */
	public static Connection getCon(){
		return getCon(null);
	}

	public static void put(String dsName, Connection con) {
		if (transLock.get()) {
			Map<String, Connection> map = cons.get();
			if (map == null)
				map = new HashMap<String, Connection>();
			map.put(dsName, con);
		}
	}

	public static void remove() {
		cons.remove();
	}
}
