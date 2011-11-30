package com.cfuture08.eweb4j.mvc.util;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cfuture08.util.ClassUtil;
import com.cfuture08.util.ReflectUtil;

/**
 * 如果是属性为pojo，例如：
 * private Pet pet;
 * 那么绑定参数的时候，优先找到 pet.name绑定到pet里面的name属性。
 * 如果找不到pet.name 参数，则找name参数绑定。如果还找不到，就不进行任何绑定。
 * 
 * 可以看到跟属性名字“pet”有关。支持深层次。例如：
 * pet.master.name
 * @author weiwei
 *
 */
public class ParamUtil {
	public static Object bindParam(Object pojo, Map<String, String[]> paramMap)
			throws Exception {
		return bindParam(null,null, pojo, paramMap, null, null, null, null);
	}

	/**
	 * 将参数绑定到pojo的属性中去
	 * 
	 * @param pojo
	 * @param paramMap
	 * @param req
	 * @param res
	 * @param out
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static Object bindParam(Object pojo, Map<String, String[]> paramMap,
			HttpServletRequest req, HttpServletResponse res, PrintWriter out,
			HttpSession session) throws Exception {
		return bindParam(null,null, pojo, paramMap, req, res, out, session);
	}

	/**
	 * 将参数绑定到pojo的属性中去
	 * 
	 * @param paramScope
	 *            一个pojo属性名，表示范围，例如 pet.name,master.name
	 * @param hasBindCls
	 *            记录已经被遍历过的POJO，防止循环递归
	 * 
	 * @param pojo
	 * @param paramMap
	 * @param req
	 * @param res
	 * @param out
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static Object bindParam(String paramScope,
			Map<Class<?>, Object> hasBindCls, Object pojo,
			Map<String, String[]> paramMap, HttpServletRequest req,
			HttpServletResponse res, PrintWriter out, HttpSession session)
			throws Exception {

		// 解决无限递归问题
		if (hasBindCls == null) {
			hasBindCls = new HashMap<Class<?>, Object>();
			hasBindCls.put(pojo.getClass(), pojo);
		}

		ReflectUtil ru = new ReflectUtil(pojo);

		// 利用反射，将参数注入到pojo中去
		for (String n : ru.getFieldsName()) {
			Method m = ru.getSetter(n);
			if (m == null)
				continue;

			Class<?> clazz = m.getParameterTypes()[0];
			if (ClassUtil.isPojo(clazz)) {
				// 如果pojo的属性参数类型是pojo，就递归调用
				// pojo的参数绑定需要一个scope来解决属性同名的问题
				if (!hasBindCls.containsKey(clazz)) {
					Object pojoArg = clazz.newInstance();
					hasBindCls.put(clazz, pojoArg);
					String _n = new String(n);
					//支持深层次，例如 pojo1.pojo2.pojo3.field
					if (paramScope != null && paramScope.length() > 0)
						_n = paramScope+"."+_n;
						
					bindParam(_n, hasBindCls, pojoArg, paramMap, req, res, out,
							session);
					m.invoke(pojo, pojoArg);
				} else {
					Object pojoArg = hasBindCls.get(clazz);
					m.invoke(pojo, pojoArg);
					continue;
				}
			} else if (HttpServletRequest.class.isAssignableFrom(clazz)) {
				m.invoke(pojo, req);
			} else if (HttpServletResponse.class.isAssignableFrom(clazz)) {
				m.invoke(pojo, res);
			} else if (PrintWriter.class.isAssignableFrom(clazz)) {
				m.invoke(pojo, out);
			} else if (HttpSession.class.isAssignableFrom(clazz)) {
				m.invoke(pojo, session);
			} else {
				String[] value = null;
				if (paramScope != null && paramScope.length() > 0)
					value = paramMap.get(paramScope + "." + n);

				if (value == null) {
					value = paramMap.get(n);
					if (value == null)
						continue;
				}

				String v = value[0];
				String[] vs = value;

				if (int.class.isAssignableFrom(clazz)
						|| Integer.class.isAssignableFrom(clazz)) {
					if ("".equals(v.trim()))
						v = "0";

					m.invoke(pojo, Integer.parseInt(v));
				} else if (long.class.isAssignableFrom(clazz)
						|| Long.class.isAssignableFrom(clazz)) {
					if ("".equals(v.trim()))
						v = "0";

					m.invoke(pojo, Long.parseLong(v));
				} else if (double.class.isAssignableFrom(clazz)
						|| Double.class.isAssignableFrom(clazz)) {
					if ("".equals(v.trim()))
						v = "0.0";

					m.invoke(pojo, Double.parseDouble(v));
				} else if (float.class.isAssignableFrom(clazz)
						|| Float.class.isAssignableFrom(clazz)) {
					if ("".equals(v.trim()))
						v = "0.0";

					m.invoke(pojo, Float.parseFloat(v));
				} else if (boolean.class.isAssignableFrom(clazz)
						|| Boolean.class.isAssignableFrom(clazz)) {
					if ("".equals(v.trim()))
						v = "false";

					m.invoke(pojo, Boolean.parseBoolean(v));
				} else if (String.class.isAssignableFrom(clazz)) {
					m.invoke(pojo, v);
				} else if (Integer[].class.isAssignableFrom(clazz)) {
					Integer[] args = new Integer[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim()))
							vs[i] = "0";

						args[i] = Integer.parseInt(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (Long[].class.isAssignableFrom(clazz)) {
					Long[] args = new Long[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim()))
							vs[i] = "0";

						args[i] = Long.parseLong(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (Double[].class.isAssignableFrom(clazz)) {
					Double[] args = new Double[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim())) 
							vs[i] = "0.0";
						
						args[i] = Double.parseDouble(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (Float[].class.isAssignableFrom(clazz)) {
					Float[] args = new Float[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim())) 
							vs[i] = "0.0";
						
						args[i] = Float.parseFloat(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (Boolean[].class.isAssignableFrom(clazz)) {
					Boolean[] args = new Boolean[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim()))
							vs[i] = "false";

						args[i] = Boolean.parseBoolean(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (int[].class.isAssignableFrom(clazz)) {
					int[] args = new int[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim()))
							vs[i] = "0";

						args[i] = Integer.parseInt(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (long[].class.isAssignableFrom(clazz)) {
					long[] args = new long[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim()))
							vs[i] = "0";

						args[i] = Long.parseLong(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (double[].class.isAssignableFrom(clazz)) {
					double[] args = new double[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim()))
							vs[i] = "0.0";

						args[i] = Double.parseDouble(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (float[].class.isAssignableFrom(clazz)) {
					float[] args = new float[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim()))
							vs[i] = "0.0";

						args[i] = Float.parseFloat(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (boolean[].class.isAssignableFrom(clazz)) {
					boolean[] args = new boolean[vs.length];
					for (int i = 0; i < vs.length; i++) {
						if ("".equals(vs[i].trim()))
							vs[i] = "false";

						args[i] = Boolean.parseBoolean(vs[i]);
					}
					m.invoke(pojo, new Object[] { args });
				} else if (String[].class.isAssignableFrom(clazz)) {
					m.invoke(pojo, new Object[] { vs });
				}
			}
		}

		hasBindCls.clear();
		hasBindCls = null;

		return (pojo);
	}
}
