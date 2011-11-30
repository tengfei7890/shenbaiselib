package test.controller;

import java.lang.reflect.Field;

import com.cfuture08.util.ReflectUtil;

public class TestChild extends TestParent{
	public static void main(String[] args) throws InstantiationException, IllegalAccessException{
		ReflectUtil ru = new ReflectUtil(TestParent.class);
		for(String f : ru.getFieldsName()){
			System.out.println(f);
		}
		System.out.println();
		System.out.println(ru.getGetter("parentName"));
		System.out.println(ru.getSetter("parentName"));
	}
}
