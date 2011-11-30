package com.cfuture08.util;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClassUtil {
	public static boolean isPojo(Class<?> cls) {
		if (HttpServletRequest.class.isAssignableFrom(cls)) {
		} else if (HttpServletResponse.class.isAssignableFrom(cls)) {
		} else if (PrintWriter.class.isAssignableFrom(cls)) {
		} else if (HttpSession.class.isAssignableFrom(cls)) {
		} else if (Collection.class.isAssignableFrom(cls)) {
		} else {
			if (Integer.class.isAssignableFrom(cls)
					|| int.class.isAssignableFrom(cls)) {
			} else if (Integer[].class.isAssignableFrom(cls)
					|| int[].class.isAssignableFrom(cls)) {

			} else if (Long.class.isAssignableFrom(cls)
					|| long.class.isAssignableFrom(cls)) {

			} else if (Long[].class.isAssignableFrom(cls)
					|| long[].class.isAssignableFrom(cls)) {

			} else if (Float.class.isAssignableFrom(cls)
					|| float.class.isAssignableFrom(cls)) {

			} else if (Float[].class.isAssignableFrom(cls)
					|| float[].class.isAssignableFrom(cls)) {

			} else if (Double.class.isAssignableFrom(cls)
					|| double.class.isAssignableFrom(cls)) {

			} else if (Double[].class.isAssignableFrom(cls)
					|| double[].class.isAssignableFrom(cls)) {

			} else if (String.class.isAssignableFrom(cls)) {
			} else if (String[].class.isAssignableFrom(cls)) {
			} else {
				try {
					cls.newInstance();
					return true;
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
		
		return false;
	}
}
