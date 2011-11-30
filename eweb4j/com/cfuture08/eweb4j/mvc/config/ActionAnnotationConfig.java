package com.cfuture08.eweb4j.mvc.config;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import com.cfuture08.eweb4j.cache.ActionConfigBeanCache;
import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.mvc.annotation.Controller;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.Result;
import com.cfuture08.eweb4j.mvc.annotation.Singleton;
import com.cfuture08.eweb4j.mvc.annotation.ValField;
import com.cfuture08.eweb4j.mvc.annotation.ValMess;
import com.cfuture08.eweb4j.mvc.annotation.ValParam;
import com.cfuture08.eweb4j.mvc.annotation.ValParamName;
import com.cfuture08.eweb4j.mvc.annotation.Validator;
import com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean;
import com.cfuture08.eweb4j.mvc.config.cache.ActionClassCache;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.ReflectUtil;
import com.cfuture08.util.StringUtil;

public class ActionAnnotationConfig {
	/**
	 * action注解
	 * 
	 * @param cb
	 * @return
	 */
	public static String readAnnotation(List<String> scanPackages) {
		String error = null;
		try {
			if (scanPackages == null)
				return error;

			for (String scanPackage : scanPackages) {
				if (scanPackage == null || scanPackage.length() == 0)
					continue;
				String classDir = FileUtil
						.getRootDir(ActionAnnotationConfig.class) + "classes";
				File dir = null;
				if (".".equals(scanPackage)) {
					scanPackage = "";
					dir = new File(classDir);
				} else
					dir = new File(classDir + File.separator
							+ scanPackage.replace(".", File.separator));
				// 递归文件目录
				if (dir.isDirectory())
					scanPackage(dir, scanPackage);
			}

		} catch (Exception e) {
			e.printStackTrace();

			error = StringUtil.getExceptionString(e);
		}

		return error;
	}

	/**
	 * 扫描action文件
	 * 
	 * @param dir
	 * @param actionPackage
	 * @throws Exception
	 */
	private static void scanPackage(File dir, String actionPackage)
			throws Exception {
		if (!dir.isDirectory())
			return;

		File[] files = dir.listFiles();
		if (files == null || files.length == 0)
			return;
		for (File f : files) {

			if (f.isDirectory())
				if (actionPackage.length() == 0)
					scanPackage(f, f.getName());
				else
					scanPackage(f, actionPackage + "." + f.getName());

			else if (f.isFile()) {
				if (!f.getName().endsWith(".class"))
					continue;

				int endIndex = f.getName().lastIndexOf(".");
				StringBuilder sb = new StringBuilder(actionPackage);
				sb.append(".").append(f.getName().subSequence(0, endIndex));
				String clazz = sb.toString();
				if (clazz == null || "".equals(clazz))
					continue;
				Class<?> cls = Class.forName(clazz);
				if (cls == null)
					continue;

				if (cls.getAnnotation(Controller.class) == null
						&& !clazz.endsWith("Controller")
						&& !clazz.endsWith("Action")
						&& !clazz.endsWith("Control"))
					continue;

				Object obj = null;
				try {
					if (cls.getAnnotation(Singleton.class) != null) {
						obj = SingleBeanCache.get(cls);
						if (obj == null) {
							obj = cls.newInstance();
							SingleBeanCache.add(cls, obj);
						}
					} else
						obj = cls.newInstance();

				} catch (Exception e) {
					continue;
				}

				ReflectUtil ru = new ReflectUtil(obj);
				Method[] ms = ru.getMethods();
				if (ms == null)
					continue;

				// 扫描方法的注解信息
				for (Method m : ms) {
					if (m.getModifiers() != 1)
						continue;

					RequestMapping actionAnn = m
							.getAnnotation(RequestMapping.class);

					if (actionAnn == null) {
						String methodName = m.getName();
						Method getter = ru.getGetter(methodName.replace("get",
								""));
						Method setter = ru.getSetter(methodName.replace("set",
								""));
						// 默认下setter和getter不作为action方法
						if (getter != null || setter != null)
							continue;
					}

					readMethodAnnotation(cls, m, actionAnn);
				}
			}
		}
	}

	/**
	 * 读取方法里的注解
	 * 
	 * @param cls
	 * @param controlReqMapVal
	 * @param m
	 * @throws Exception
	 */
	private static void readMethodAnnotation(Class<?> cls, Method m,
			RequestMapping actionAnn) throws Exception {

		RequestMapping reqMapAnn = cls.getAnnotation(RequestMapping.class);
		String controlReqMapVal = reqMapAnn == null ? "" : reqMapAnn.value();
		String controlMethod = reqMapAnn == null ? "" : reqMapAnn.method();
		String controlShowValErr = reqMapAnn == null ? "" : reqMapAnn
				.showValErr();

		String clazzName = cls.getName();
		String methodName = m.getName();
		String methodReqMapVal = "/" + methodName;// 默认方法名
		String reqMethod = controlMethod.length() == 0 ? "GET|POST|PUT|DELETE"
				: controlMethod;// 默认四种HTTP方法都支持
		String valErrType = controlShowValErr.length() == 0 ? "alert"
				: controlShowValErr;// 验证器验证信息输出方式默认”alert“

		if (actionAnn != null) {
			methodReqMapVal = actionAnn.value();
			reqMethod = actionAnn.method();
			valErrType = actionAnn.showValErr();
		}

		String actionName = controlReqMapVal + methodReqMapVal;
		ActionConfigBean action = new ActionConfigBean();
		action.setShowValErrorType(valErrType);
		action.setClazz(clazzName);
		action.setMethod(methodName);
		action.setName(actionName);
		action.setReqMethod(reqMethod);

		// 读取@Result注解
		Result resultAnn = m.getAnnotation(Result.class);
		if (resultAnn != null)
			action.setResult(ResultAnnUtil.readResultAnn(resultAnn));

		// 读取@Validator注解
		Validator validatorAnn = m.getAnnotation(Validator.class);
		if (validatorAnn != null)
			action.setValidator(ValidatorUtil.readValidator(validatorAnn,
					m.getAnnotation(ValField.class),
					m.getAnnotation(ValMess.class),
					m.getAnnotation(ValParamName.class),
					m.getAnnotation(ValParam.class)));

		// Action全名，框架用，包括对“{xxx}”url参数的正则化，HttpRequestMethod
		String actionFullName = ActionUrlUtil.mathersUrlMapping(m, actionName,
				cls) + "@" + reqMethod;

		// 将读取成功的配置信息放入缓存供框架运行期使用
		ActionConfigBeanCache.add(actionFullName, action);
		ActionClassCache.add(clazzName, cls);
	}

}
