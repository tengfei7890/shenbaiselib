package com.cfuture08.eweb4j.orm.jdbc;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import com.cfuture08.eweb4j.orm.dao.config.DAOConfigConstant;

public class DataSourceCache {
	private static Hashtable<String, DataSource> ht = new Hashtable<String, DataSource>();

	public static void put(String key, DataSource ds) {
		ht.put(key, ds);
	}

	public static DataSource get(String key) {
		if (key == null) {
			key = DAOConfigConstant.MYDBINFO;
		}
		return ht.get(key);
	}
	
	public static DataSource get(){
		return get(null);
	}

	public static boolean containsKey(String key) {
		return ht.containsKey(key);
	}

	public static boolean contains(DataSource ds) {
		return ht.containsValue(ds);
	}

	public static boolean containsValue(DataSource ds) {
		return ht.containsValue(ds);
	}
	
	public static void clear(){
		ht.clear();
	}
	
	public static void remove(String dsName){
		ht.remove(dsName);
	}
	
	public static Set<Entry<String, DataSource>> entrySet(){
		return ht.entrySet();
	}
}
