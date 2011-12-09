package org.mjz.ioc;



import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mjz.ioc.annotation.Component;
import org.mjz.ioc.annotation.Inject;

/**
 * Ioc的默认实现类
 * @author Administrator
 *
 */
public class DefaultIoc implements MutableIoc, Serializable {
	
	private static final long serialVersionUID = 8013656866359460948L;
	
	/** 组件容器 */
	private Map<String, HashMap<String, Object>> componentMap = new HashMap<String, HashMap<String, Object>>();
	/** 类型容器*/
	private Map<String, Set<Class<?>>> interfacesMap = new HashMap<String, Set<Class<?>>>();
	/** 别名容器*/
	private Map<String, String> aliasMap = new HashMap<String, String>();
	
	/** 依赖容器 */
	private Set<String> set = new LinkedHashSet<String>();

	
	/** 
	 * 返回实体或抛异常
	 * @see org.mjz.ioc.Ioc#$(java.lang.String)
	 */
	public Object $(String name) {
		Object instance = null;
		String className = aliasMap.get(name);
		if(className==null){
			throw new RuntimeException("ioc not contain the component ["+name+"]");
		}
		if(set.contains(className)){
			throw new RuntimeException(name + "依赖出现回路 ["+set+"]");
		}else{
			set.add(className);
		}
		HashMap<String, Object> componentObject = componentMap.get(className);
		ComponentType scope = (ComponentType)componentObject.get(Ioc.scope);
		if(ComponentType.Singleton.equals(scope)){ //单例
			instance = componentObject.get(Ioc.instance);
			if (instance == null) {
				instance = getInstance(className);
				componentObject.put(Ioc.instance, instance);
				injectFiled(instance);
				postConstruct(instance);
			}			
		}else{ //原型
			instance = getInstance(className);	
			injectFiled(instance);
			postConstruct(instance);
		}
		set.remove(className);
		return instance;		
	}
	
	private Object getInstance(String className){
		Object instance = null;
		
		HashMap<String, Object> componentObject = componentMap.get(className);
		
		Constructor<?> constructor = (Constructor<?>) componentObject.get(Ioc.constructor);
		Class<?> clazz = (Class<?>) componentObject.get(Ioc.clazz);
		try {
			if (constructor == null) {
				System.out.println("默认构造器实例化 : " + className);
				instance = clazz.newInstance();
			} else {
				Class<?>[] parameters = constructor.getParameterTypes();
				System.out.println("start 实例化 : " + className);

				if (parameters.length > 0) {
					Object[] parameterObject = new Object[parameters.length];
					System.out.println("parameters   size is "
							+ parameters.length);
					Annotation[][] annotationss = constructor
							.getParameterAnnotations();// .getParameterTypes();
					System.out.println("Annotation[] size is "
							+ annotationss.length);

					for (int i = 0; i < parameters.length; i++) {
						Object p = null;
						Annotation[] annotations = annotationss[i];
						for (Annotation annotation : annotations) {
							if (annotation instanceof Inject) {
								String componentName = ((Inject) annotation)
										.value();
								System.out.println(parameters[i].getName()
										+ " need Inject " + componentName);
								p = $(componentName);
								if (p != null && !parameters[i].isInstance(p)) {
									throw new RuntimeException(componentName
											+ " 不是" + parameters[i].getName()
											+ "类型的");
								}
								break;
							}
						}
						if (p == null) {
							if (parameters[i].isInterface()) { // 接口
								Class<?> depenClass = getDepen(parameters[i]);
								p = $(depenClass.getName());
							} else {
								// 类
								p = $(parameters[i]);
							}

						}
						parameterObject[i] = p;
						System.out
								.println("parameterObject[" + i + "] is " + p);
					}
					instance = constructor.newInstance(parameterObject);
				}
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}		
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T> T $(Class<T> clazz) {
		return (T)$(clazz.getName());
	}
	
	

	public Ioc getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		return componentMap.size();
	}
	
	public void register(String className, String alias){
		String alias_ = alias;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Class<?> clazz;
		try {
			clazz = classLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}		
		
		String componentName = addComponent(clazz);// 增加组件
		alias_ = alias_ != null ? alias_ : componentName; 
		addAlias(alias_, className);// 加入别名		
		Set<Class<?>> interfacesSet = getType(clazz);// 加入类型说明	
		postImpl(clazz, interfacesSet); 	
	}
	
	public void register(Class<?> clazz, String alias){
		register(clazz.getName(), alias);
	}

	public void register(String className) {
		register(className, null);
	
	}

	public void register(Class<?> clazz) {
		register(clazz.getName(),null);
	}

	public void unregister(String name) {
		// TODO Auto-generated method stub

	}

	public void unregister(Class<?> clazz) {
		// TODO Auto-generated method stub

	}
	
	public void init(){
	}
	
	// 依赖实现类
	private Class<?> getDepen(Class<?> cls) {		
		String className = cls.getName();
		Set<Class<?>> interfacesSet = interfacesMap.get(className);
		System.out.println(className + " depend " + interfacesSet);
		if(interfacesSet == null ){
			throw new RuntimeException(className+" 类型没有注册实现类");
		}
		if(interfacesSet.size()>1){
			throw new RuntimeException(className+" 类型注册实现类有多个:"+interfacesSet);
		}
		return interfacesSet.iterator().next();
	}
	
	private String addComponent(Class<?> clazz) {
		String className = clazz.getName();		
		if (componentMap.get(className) == null) {
			// 组件处理
			HashMap<String, Object> componentObject = new HashMap<String, Object>();
			String componentName = "";
			ComponentType scope = ComponentType.Singleton;
			if(clazz.isAnnotationPresent(Component.class)){
				Component component = (Component)clazz.getAnnotation(Component.class);
				componentName = component.value().trim();
				scope = component.scope();
			}
			componentObject.put(Ioc.clazz, clazz);
			componentObject.put(Ioc.alias, componentName);
			componentObject.put(Ioc.scope, scope);
			componentObject.put(Ioc.constructor, getdefaultConstructor(clazz.getConstructors()));
			componentMap.put(className, componentObject);
			return componentName;
		}else{
			return (String)((HashMap<String, Object>)componentMap.get(className)).get(Ioc.alias);
		}
		
	}
	
	private Constructor<?> getdefaultConstructor(Constructor<?>[] Constructors){
		List<Constructor<?>> defaultConstructorList = new ArrayList<Constructor<?>>();
		for(Constructor<?> constructor : Constructors){
			if(constructor.isAnnotationPresent(org.mjz.ioc.annotation.Constructor.class)){
				defaultConstructorList.add(constructor);				
			}
		}
		switch (defaultConstructorList.size()) {
		case 0:
			return null;
		case 1:
			return defaultConstructorList.get(0);
		default: {
			StringBuilder sb = new StringBuilder().append("Constructor 不应该存于多个构造函数: ");
			for(Constructor<?> c : defaultConstructorList){
				sb.append(c.getName()+", ");				
			}
			throw new RuntimeException(sb.toString());
		}
		}
	}
	
	private void postImpl(Class<?> clazz, Set<Class<?>> interfacesSet) {
		for (Class<?> cls : interfacesSet) {
			String className = cls.getName();
			Set<Class<?>> implclasses = null;
			if (interfacesMap.get(className) != null) {
				implclasses = interfacesMap.get(className);
			} else {
				implclasses = new HashSet<Class<?>>();
				interfacesMap.put(className, implclasses);
			}
			implclasses.add(clazz);
		}
	}
	
	private void addAlias(String alias, String className) {
	
		if (aliasMap.get(alias) != null) {
			throw new RuntimeException("已存别名：" + alias);
		} if(alias != null && alias.length()>0) {
			aliasMap.put(alias, className);
			System.out.println("alias " + alias + " className " + className);
		}		
		
		if(aliasMap.get(className) == null){
			aliasMap.put(className, className);
		}
	}

	
	private Set<Class<?>> getType(Class<?> clazz) {
		Set<Class<?>> interfacesSet = new HashSet<Class<?>>();

		Class<?>[] clazzs = clazz.getInterfaces(); //接口		
		for (Class<?> c : clazzs) {
			interfacesSet.add(c);
			interfacesSet.addAll(getType(c));
		}
		Class<?> superClass = clazz.getSuperclass();//父类
		if(superClass != null && !superClass.equals(Object.class)){ 
			interfacesSet.add(superClass);
			interfacesSet.addAll(getType(superClass));
		}
		return interfacesSet;
	}
	
	private void injectFiled(Object instance){
		Class<?> clazz = instance.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(Inject.class)){
				Inject inject =(Inject)field.getAnnotation(Inject.class);
				String value = inject.value();
				Object inObject = null;
				if(value.length() > 0){
					inObject = $(value);					
				}else{ //类型注入
					int modifier = field.getType().getModifiers();
					if(modifier == 1537 || modifier == 1025){ //接口 、 抽象
						Class<?> depenClass = getDepen(field.getType());
						inObject = $(depenClass.getName());	
					}
					if(modifier == 1){ //类
						inObject = $(field.getType());
					}
				}
				if(field.getType().isInstance(inObject)){					
					try {
						field.setAccessible(true);
						field.set(instance, inObject);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} else {
					throw new RuntimeException(inObject + " 不是" + field.getType().getName() + "类型的");
				}
			}
		}
		
	}
	
	private void postConstruct(Object instance){
		Class<?> clazz = instance.getClass();
		Method startMethod = null;
		if(clazz.isAnnotationPresent(org.mjz.ioc.annotation.Stateful.class)){
			Method[] methods = clazz.getMethods();
			for(Method method : methods){
				if(method.isAnnotationPresent(org.mjz.ioc.annotation.PostConstruct.class) &&
						method.getParameterTypes().length==0){	//开始方法
					startMethod = method;
				}
			}
			if(startMethod != null){
				try {
					startMethod.invoke(instance);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}		
	}
	
	private void preDestroy(Object instance){
		Class<?> clazz = instance.getClass();
		Method preDestroy = null;
		if(clazz.isAnnotationPresent(org.mjz.ioc.annotation.Stateful.class)){
			Method[] methods = clazz.getMethods();
			for(Method method : methods){
				if(method.isAnnotationPresent(org.mjz.ioc.annotation.PreDestroy.class) &&
						method.getParameterTypes().length==0){	//结束方法
					preDestroy = method;
				}
			}
			if(preDestroy != null){
				try {
					preDestroy.invoke(instance);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}		
	}
	
	
	
	 public void destrory() {
		 for(HashMap<String, Object> componentObject :componentMap.values()){
			 Object instance = componentObject.get(Ioc.instance);
			 if(instance != null){
				 preDestroy(instance);
			 }
		 }		
	}

	public void showDetails(){
		for(String alias : aliasMap.keySet()){
			System.out.println("["+alias+"] "+aliasMap.get(alias));
		}
		System.out.println("######################################");
		for(String interfacesKey : interfacesMap.keySet()){
			System.out.println("["+interfacesKey+"] "+interfacesMap.get(interfacesKey));
		}
		System.out.println("######################################");
	}

}
