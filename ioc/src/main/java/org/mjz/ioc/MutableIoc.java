package org.mjz.ioc;

/**
 * 容器核心接口,主要注册、反注册实体
 * @author Administrator
 *
 */
/**
 * @author EX-MOJIEZHONG001
 *
 */
public interface MutableIoc extends Ioc {
	
	/**
	 * 通过类名注册实体
	 * @param name
	 */
	void register(String className);
	
	/**
	 * 通过类注册实体
	 * @param className
	 */
	void register(Class<?> clazz); 
	
	/**
	 * 通过类名反注册实体
	 * @param name
	 */
	void unregister(String name);
	
	/**
	 * 通过类反注册实体
	 * @param className
	 */
	void unregister(Class<?> clazz);	
}	
