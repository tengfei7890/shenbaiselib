package org.mjz.ioc;

/**
 * 容器核心接口,主要获取实体之用
 * @author Administrator
 *
 */
public interface Ioc {
	/**类键名*/
	static String clazz = "key_class";
	
	/**组件别名键名*/
	static String alias = "key_alias";
	
	/**组件作用域键名*/
	static String scope = "key_scope";
	
	/**组件标记的构造函数键名*/
	static String constructor = "key_constructor";
	
	/**组件单例键名*/
	static String instance = "key_instance";
	
	/**
	 * 实例选择器
	 * 通过名字获取实体，本容器没有就去父容器获取
	 * @param name
	 * @return
	 */
	Object $(String name);
	
	/**
	 * 实例选择器
	 * 通过类名获取实体，本容器没有就去父容器获取
	 * @param <T>
	 * @param className
	 * @return
	 */
	<T> T $(Class<T> clazz);
	
	/**
	 * 获取父容器
	 * @return
	 */
	Ioc getParent();
	
	/**
	 * 返回本容器实本个数
	 * @return
	 */
	int size();
	
	
	/**
	 * 
	 */
	void destrory(); 

}
