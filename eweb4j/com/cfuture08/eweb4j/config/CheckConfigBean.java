package com.cfuture08.eweb4j.config;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.bean.ActionXmlFile;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.bean.ConfigIOC;
import com.cfuture08.eweb4j.config.bean.ConfigMVC;
import com.cfuture08.eweb4j.config.bean.ConfigORM;
import com.cfuture08.eweb4j.config.bean.IOCXmlFiles;
import com.cfuture08.eweb4j.config.bean.InterXmlFile;
import com.cfuture08.eweb4j.config.bean.ORMXmlFiles;
import com.cfuture08.eweb4j.ioc.config.bean.IOCConfigBean;
import com.cfuture08.eweb4j.ioc.config.bean.Injection;
import com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.InterConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ResultConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.Url;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;
import com.cfuture08.eweb4j.mvc.validate.ValidatorConstant;
import com.cfuture08.eweb4j.orm.config.bean.ORMConfigBean;
import com.cfuture08.eweb4j.orm.config.bean.Property;
import com.cfuture08.eweb4j.orm.dao.config.bean.DBInfoConfigBean;
import com.cfuture08.eweb4j.orm.sql.constant.DBType;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.ReflectUtil;
import com.cfuture08.util.RegexList;
import com.cfuture08.util.StringUtil;

/**
 * Check EWeb4J framework startup configuration files. Also included the ORM,
 * IOC, MVC three components
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class CheckConfigBean {
	public static String checkEWeb4JConfigBean(ConfigBean cb) {
		String error = null;
		StringBuilder sb = new StringBuilder();

		if (!"true".equalsIgnoreCase(cb.getDebug())
				&& !"false".equalsIgnoreCase(cb.getDebug())
				&& !"1".equals(cb.getDebug()) && !"0".equals(cb.getDebug())) {
			sb.append("当前您填写的：( debug=").append(cb.getDebug())
					.append(" )是错误的！它只能填写为：true|false|1|0 中的一种 ;").append("\n");
		}
		if (!"".equals(sb.toString())) {
			error = "\n<br /><b>" + ConfigConstant.XMLFILE_PATH + ":</b>\n"
					+ sb.toString();
		}

		return error;
	}

	/**
	 * Check mainly in the configuration file IOC parts
	 * 
	 * @param ioc
	 * @return
	 */
	public static String checkEWeb4JIOCPart(ConfigIOC ioc) {
		String error = null;
		StringBuilder sb = new StringBuilder();
		if (!"true".equalsIgnoreCase(ioc.getOpen())
				&& !"false".equalsIgnoreCase(ioc.getOpen())
				&& !"1".equals(ioc.getOpen()) && !"0".equals(ioc.getOpen())) {
			sb.append("当前您填写的：( open=").append(ioc.getOpen())
					.append(" )是错误的！它只能填写为：true|false|1|0 中的一种 ;").append("\n");
		} else if ("true".equalsIgnoreCase(ioc.getOpen())
				|| "1".equals(ioc.getOpen())) {
			if (!"true".equalsIgnoreCase(ioc.getDebug())
					&& !"false".equalsIgnoreCase(ioc.getDebug())
					&& !"1".equals(ioc.getDebug())
					&& !"0".equals(ioc.getDebug())) {
				sb.append("当前您填写的：( debug=").append(ioc.getDebug())
						.append(" )是错误的！它只能填写为：true|false|1|0 中的一种 ;")
						.append("\n");
			}
			/*
			 * if (!FileUtil.isValidFileName(ioc.getLogFile())) {
			 * sb.append("当前您填写的：( logFile=").append(ioc.getLogFile()).append(
			 * " )是错误的！它要符合文件命名规范 ;").append("\n"); }
			 */
		}
		if (!StringUtil.isNumeric(ioc.getLogMaxSize())) {
			sb.append("当前您填写的：( logMaxSize=").append(ioc.getLogMaxSize())
					.append(" )是错误的！它只能填写为数字，注意单位是“兆”（M）;").append("\n");
		}
		if (!"".equals(sb.toString())) {
			error = "\n<br /><b>" + ConfigConstant.XMLFILE_PATH
					+ "[ioc]:</b>\n" + sb.toString();
		}

		return error;
	}

	public static String checkIOCXml(IOCXmlFiles xmlFiles) {
		String error = null;
		/*
		 * StringBuilder sb = new StringBuilder(); for (IOCXmlFiles xml :
		 * xmlFiles) { if (!FileUtil.isValidFileName(xml.getPath())) {
		 * sb.append("当前您填写的：( path=").append(xml.getPath()).append(
		 * " )是错误的！它要符合文件命名规范 ;").append("\n"); } }
		 * 
		 * if (!"".equals(sb.toString())) { error = "\n<br /><b>" +
		 * ConfigConstant.XMLFILE_PATH + "[ioc][xmlFiles]:</b>\n" +
		 * sb.toString(); }
		 */

		return error;
	}

	/**
	 * Check mainly in the configuration file ORM parts
	 * 
	 * @param orm
	 * @return
	 */
	public static String checkEWeb4JORMPart(ConfigORM orm) {
		String error = null;
		StringBuilder sb = new StringBuilder();
		if (!"true".equalsIgnoreCase(orm.getOpen())
				&& !"false".equalsIgnoreCase(orm.getOpen())
				&& !"1".equals(orm.getOpen()) && !"0".equals(orm.getOpen())) {
			sb.append("当前您填写的：( open=").append(orm.getOpen())
					.append(" )是错误的！它只能填写为：true|false|1|0 中的一种 ;").append("\n");
		} else if ("true".equalsIgnoreCase(orm.getOpen())
				|| "1".equals(orm.getOpen())) {
			if (!"true".equalsIgnoreCase(orm.getDebug())
					&& !"false".equalsIgnoreCase(orm.getDebug())
					&& !"1".equals(orm.getDebug())
					&& !"0".equals(orm.getDebug())) {
				sb.append("当前您填写的：( debug=").append(orm.getDebug())
						.append(" )是错误的！它只能填写为：true|false|1|0 中的一种 ;")
						.append("\n");
			}
			if (!StringUtil.isNumeric(orm.getLogMaxSize())) {
				sb.append("当前您填写的：( logMaxSize=").append(orm.getLogMaxSize())
						.append(" )是错误的！它只能填写为数字，注意单位是“兆”（M）;").append("\n");
			}
			List<String> scanPackages = orm.getScanPojoPackage().getPath();
			if (scanPackages != null) {
				for (String scanPackage : scanPackages) {
					if (scanPackage != null && scanPackage.length() > 0) {
						String classDir = FileUtil
								.getRootDir(CheckConfigBean.class)
								+ "classes"
								+ File.separator;
						if (!new File(classDir
								+ scanPackage.replace(".", File.separator))
								.isDirectory()) {
							sb.append("当前您填写的：( scanPojoPackage=")
									.append(scanPackage)
									.append(" )是错误的！它必须是一个有效的包名 ;")
									.append("\n");
						}
					}
				}
			}
			String dataSource = orm.getDataSource();
			if (null != dataSource && dataSource.trim().length() > 0) {
				try {
					if (Class.forName(dataSource) == null) {
						sb.append("当前您填写的( dataSource=").append(dataSource)
								.append(" )是错误的！它必须是一个有效的类 ;\n");
					}
				} catch (ClassNotFoundException e) {
					sb.append("当前您填写的( dataSource=").append(dataSource)
							.append(" )是错误的！它必须是一个有效的类 ;\n");
				}
			}
		}
		if (!"".equals(sb.toString())) {
			error = "\n<br /><b>" + ConfigConstant.XMLFILE_PATH
					+ "[orm]:</b>\n" + sb.toString();
		}

		return error;
	}

	public static String checkORMXml(ORMXmlFiles xmlFiles) {
		String error = null;
		/*
		 * StringBuilder sb = new StringBuilder(); for (ORMXmlFiles xml :
		 * xmlFiles) { if (!FileUtil.isValidFileName(xml.getPath())) {
		 * sb.append("当前您填写的：( path=").append(xml.getPath()).append(
		 * " )是错误的！它要符合文件命名规范 ;").append("\n"); } }
		 * 
		 * if (!"".equals(sb.toString())) { error = "\n<br /><b>" +
		 * ConfigConstant.XMLFILE_PATH + "[orm][xmlFiles]:</b>\n" +
		 * sb.toString(); }
		 */

		return error;
	}

	/**
	 * Check mainly in the configuration file MVC parts
	 * 
	 * @param mvc
	 * @return
	 */
	public static String checkEWeb4JMVCPart(ConfigMVC mvc) {
		String error = null;
		StringBuilder sb = new StringBuilder();
		if (!"true".equalsIgnoreCase(mvc.getOpen())
				&& !"false".equalsIgnoreCase(mvc.getOpen())
				&& !"1".equals(mvc.getOpen()) && !"0".equals(mvc.getOpen())) {
			sb.append("当前您填写的：( open=").append(mvc.getOpen())
					.append(" )是错误的！它只能填写为：true|false|1|0 中的一种 ;").append("\n");
		} else if ("true".equalsIgnoreCase(mvc.getOpen())
				|| "1".equals(mvc.getOpen())) {
			if (!"true".equalsIgnoreCase(mvc.getDebug())
					&& !"false".equalsIgnoreCase(mvc.getDebug())
					&& !"1".equals(mvc.getDebug())
					&& !"0".equals(mvc.getDebug())) {
				sb.append("当前您填写的：( debug=").append(mvc.getDebug())
						.append(" )是错误的！它只能填写为：true|false|1|0 中的一种 ;")
						.append("\n");
			}
			if (!StringUtil.isNumeric(mvc.getLogMaxSize())) {
				sb.append("当前您填写的：( logMaxSize=").append(mvc.getLogMaxSize())
						.append(" )是错误的！它只能填写为数字，注意单位是“兆”（M）;").append("\n");
			}
			List<String> scanPackages = mvc.getScanActionPackage().getPath();
			if (scanPackages != null) {
				for (String scanPackage : scanPackages) {
					if (scanPackage != null && scanPackage.length() > 0) {
						if (!new File(
								FileUtil.getRootDir(CheckConfigBean.class)
										+ "classes"
										+ File.separator
										+ scanPackage.replace(".",
												File.separator)).isDirectory()) {
							sb.append("当前您填写的：( scanActionPackage=")
									.append(scanPackage)
									.append(" )是错误的！它必须是一个有效的包名 ;")
									.append("\n");
						}
					}
				}
			}
			/*
			 * if (!FileUtil.isValidFileName(mvc.getLogFile())) {
			 * sb.append("当前您填写的：( logFile=").append(mvc.getLogFile()).append(
			 * " )是错误的！它要符合文件命名规范 ;").append("\n"); }
			 */
		}
		if (!"".equals(sb.toString())) {
			error = "\n<br /><b>" + ConfigConstant.XMLFILE_PATH
					+ "[mvc]:</b>\n" + sb.toString();
		}

		return error;
	}

	public static String checkMVCActionXmlFile(ActionXmlFile xmlFiles) {
		String error = null;
		/*
		 * StringBuilder sb = new StringBuilder(); for (MVCXmlFiles xml :
		 * xmlFiles) { if (!FileUtil.isValidFileName(xml.getPath())) {
		 * sb.append("当前您填写的：( path=").append(xml.getPath()).append(
		 * " )是错误的！它要符合文件命名规范 ;").append("\n"); } }
		 * 
		 * if (!"".equals(sb.toString())) { error = "\n<br /><b>" +
		 * ConfigConstant.XMLFILE_PATH + "[mvc][xmlFiles]:</b>\n" +
		 * sb.toString(); }
		 */

		return error;
	}

	public static String checkInter(InterXmlFile xmlFiles) {
		String error = null;
		/*
		 * StringBuilder sb = new StringBuilder(); for (MVCXmlFiles xml :
		 * xmlFiles) { if (!FileUtil.isValidFileName(xml.getPath())) {
		 * sb.append("当前您填写的：( path=").append(xml.getPath()).append(
		 * " )是错误的！它要符合文件命名规范 ;").append("\n"); } }
		 * 
		 * if (!"".equals(sb.toString())) { error = "\n<br /><b>" +
		 * ConfigConstant.XMLFILE_PATH + "[mvc][xmlFiles]:</b>\n" +
		 * sb.toString(); }
		 */

		return error;
	}

	/**
	 * Check the IOC independent components configuration files
	 * 
	 * @param ioc
	 * @return
	 */
	public static String checkIOC(IOCConfigBean ioc, String xmlFile) {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		if ("true".equalsIgnoreCase(cb.getIoc().getOpen())
				|| "1".equals(cb.getIoc().getOpen())) {
			StringBuilder sb = new StringBuilder();
			if (!"prototype".equalsIgnoreCase(ioc.getScope())
					&& !"singleton".equalsIgnoreCase(ioc.getScope())
					&& !"".equals(ioc.getScope())) {
				sb.append("当前您填写的：( scope=").append(ioc.getScope())
						.append(" )是错误的！它只能填写为：prototype|singleton 中的一种 ;")
						.append("\n");
			}
			if (!"".equals(ioc.getClazz())) {
				try {
					if (Class.forName(ioc.getClazz()) == null) {
						sb.append("当前您填写的( class=").append(ioc.getClazz())
								.append(" )是错误的！它必须是一个有效的类 ;\n");
					}
				} catch (ClassNotFoundException e) {
					sb.append("当前您填写的( class=").append(ioc.getClazz())
							.append(" )是错误的！它必须是一个有效的类 ;\n");
				}
			}
			if (!"".equals(sb.toString())) {
				error = "\n<br /><b>" + xmlFile + ":[bean id=" + ioc.getId()
						+ "]</b>\n" + sb.toString();
			}
		}

		return error;
	}

	public static String checkMVCInterceptor(InterConfigBean inter,
			String xmlFile) {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		if ("true".equalsIgnoreCase(cb.getMvc().getOpen())
				|| "1".equals(cb.getMvc().getOpen())) {
			StringBuilder sb = new StringBuilder();
			if (!"".equals(inter.getClazz())) {
				try {
					if (Class.forName(inter.getClazz()) == null) {
						sb.append("当前您填写的( class=").append(inter.getClazz())
								.append(" )是错误的！它必须是一个有效的类 ;\n");
					}
				} catch (ClassNotFoundException e) {
					sb.append("当前您填写的( class=").append(inter.getClazz())
							.append(" )是错误的！它必须是一个有效的类 ;\n");
				}
			}
			if (!"".equals(inter.getType())
					&& !"before".equalsIgnoreCase(inter.getType())
					&& !"after".equalsIgnoreCase(inter.getType())) {
				sb.append("当前您填写的：( type=").append(inter.getType())
						.append(" )是错误的！它只能填写为：before|after|留空  中的一种 ;")
						.append("\n");
			}
			for (Url url : inter.getUrl()) {
				if (!"start".equalsIgnoreCase(url.getType())
						&& !"end".equalsIgnoreCase(url.getType())
						&& !"contains".equalsIgnoreCase(url.getType())
						&& !"all".equalsIgnoreCase(url.getType())
						&& !"regex".equalsIgnoreCase(url.getType())
						&& !"".equals(url.getType())) {
					sb.append("当前您填写的：( type=")
							.append(url.getType())
							.append(" )是错误的！它只能填写为：start|end|contains|all|regex|留空  中的一种 ;")
							.append("\n");
				}
			}
			if (!"".equals(sb.toString())) {
				error = "\n<br /><b>" + xmlFile + ":[bean name="
						+ inter.getName() + "]</b>\n" + sb.toString();
			}
		}

		return error;
	}

	/**
	 * Check the IOC component Inject part configuration
	 * 
	 * @param inject
	 * @param iocList
	 * @return
	 */
	public static String checkIOCJnject(List<Injection> inject,
			List<IOCConfigBean> iocList, String beanID, String xmlFile) {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		if ("true".equalsIgnoreCase(cb.getIoc().getOpen())
				|| "1".equals(cb.getIoc().getOpen())) {
			StringBuilder sb = new StringBuilder();
			for (Injection inj : inject) {
				if (inj.getRef() != null && !"".equals(inj.getRef())) {
					boolean flag = false;
					for (IOCConfigBean iocBean : iocList) {
						if (!iocBean.getId().equals(inj.getRef())) {
							flag = true;
						} else {
							flag = false;
							break;
						}
					}
					if (flag) {
						sb.append("当前您填写的 :( ref=").append(inj.getRef())
								.append(" )是错误的！它必须是本文件中某个bean的id值 ;\n");
					}
				}
				if (!"".equals(inj.getType())) {
					if (!"int".equalsIgnoreCase(inj.getType())
							&& !"String".equalsIgnoreCase(inj.getType())
							&& !"boolean".equalsIgnoreCase(inj.getType())
							&& !"long".equalsIgnoreCase(inj.getType())
							&& !"double".equalsIgnoreCase(inj.getType())
							&& !"float".equalsIgnoreCase(inj.getType())) {
						sb.append("当前您填写的：( type=")
								.append(inj.getType())
								.append(" )是错误的！它只能填写为：int|String|long|float|boolean|double 中的一种 ;")
								.append("\n");
					}
				}
				if ("int".equalsIgnoreCase(inj.getType())) {
					if (!inj.getValue().matches(RegexList.integer_regexp)) {
						sb.append("当前您填写的：( value=").append(inj.getValue())
								.append(" )是错误的！它必须是整数！ ;").append("\n");
					}
				}

			}

			if (!"".equals(sb.toString())) {
				error = "\n<br /><b>" + xmlFile + "[bean id=" + beanID
						+ "][inject]:</b>\n" + sb.toString();
			}
		}
		return error;
	}

	/**
	 * Check the IOC component DBInfo part configuration
	 * 
	 * @param dcb
	 * @return
	 */
	public static String checkORMDBInfo(DBInfoConfigBean dcb, String filePath) {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		if ("true".equals(cb.getOrm().getOpen())
				|| "1".equals(cb.getOrm().getOpen())) {
			StringBuilder sb = new StringBuilder();
			if (!DBType.MYSQL_DB.equalsIgnoreCase(dcb.getDataBaseType())
					&& !DBType.MSSQL2000_DB.equalsIgnoreCase(dcb
							.getDataBaseType())
					&& !DBType.MSSQL2005_DB.equalsIgnoreCase(dcb
							.getDataBaseType())) {
				// && !DBType.ORACLE_DB.equalsIgnoreCase(dcb.getDataBaseType())
				sb.append("当前您填写的: ( dataBaseType=")
						.append(dcb.getDataBaseType()).append(" )是错误的！它必须为：")
						.append(DBType.MYSQL_DB).append("|")
						.append(DBType.MSSQL2000_DB).append("|")
						.append(DBType.MSSQL2005_DB).append("中的其中一个,忽略大小写 ;")
						.append("\n");// .append("|").append(DBType.ORACLE_DB)
			}

			if (!"".equals(sb.toString())) {
				error = "\n<br /><b>" + filePath + ":</b>\n" + sb.toString();
			}
		}

		return error;
	}

	/**
	 * Check the ORM independent components configuration files
	 * 
	 * @param orm
	 * @return
	 */
	public static String checkORM(ORMConfigBean orm, String xmlFile) {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		if ("true".equalsIgnoreCase(cb.getOrm().getOpen())
				|| "1".equals(cb.getOrm().getOpen())) {
			StringBuilder sb = new StringBuilder();
			if (!"".equals(orm.getClazz())) {
				try {
					if (Class.forName(orm.getClazz()) == null) {
						sb.append("当前您填写的( class=").append(orm.getClazz())
								.append(" )是错误的！它必须是一个有效的类 ;\n");
					}
				} catch (ClassNotFoundException e) {
					sb.append("当前您填写的( class=").append(orm.getClazz())
							.append(" )是错误的！它必须是一个有效的类 ;\n");
				}
			}

			if (!"".equals(sb.toString())) {
				error = "\n<br /><b>" + xmlFile + ":[bean name=" + orm.getId()
						+ "]</b>\n" + sb.toString();
			}
		}

		return error;
	}

	/**
	 * Check the Property part of ORM components configuration
	 * 
	 * @param pList
	 * @param ormList
	 * @return
	 */
	public static String checkORMProperty(List<Property> pList,
			List<ORMConfigBean> ormList, String beanID, String xmlFile) {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		if ("true".equalsIgnoreCase(cb.getOrm().getOpen())
				|| "1".equals(cb.getOrm().getOpen())) {
			StringBuilder sb = new StringBuilder();
			for (Property p : pList) {
				if (p.getRelBean() != null && !"".equals(p.getRelBean())) {
					boolean flag = false;
					for (ORMConfigBean ormBean : ormList) {
						if (!ormBean.getId().equals(p.getRelBean())) {
							flag = true;
						} else {
							flag = false;
							break;// 如果找到了就不必继续遍历了
						}
					}
					if (flag) {
						sb.append("当前您填写的 : ( relBean=").append(p.getRelBean())
								.append(" )是错误的！它必须是本文件中某个bean的id值 ;\n");
					}
				}
			}

			if (!"".equals(sb.toString())) {
				error = "\n<br /><b>" + xmlFile + "[bean id=" + beanID
						+ "][property]:</b>\n" + sb.toString();
			}
		}

		return error;
	}

	/**
	 * Check the MVC independent components configuration files
	 * 
	 * @param mvc
	 * @return
	 */
	public static String checkMVCAction(ActionConfigBean mvc, String xmlFile) {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		if ("true".equalsIgnoreCase(cb.getMvc().getOpen())
				|| "1".equals(cb.getMvc().getOpen())) {
			StringBuilder sb = new StringBuilder();
			if (!"".equals(mvc.getClazz())) {
				try {
					Class<?> clazz = Class.forName(mvc.getClazz());
					if (clazz == null) {
						sb.append("当前您填写的( class=").append(mvc.getClazz())
								.append(" )是错误的！它必须是一个有效的类 ;\n");
					} else {
						if (mvc.getMethod() != null
								&& !"".equals(mvc.getMethod())) {
							Method m = new ReflectUtil(clazz.newInstance())
									.getMethod(mvc.getMethod());
							if (m == null) {
								sb.append("当前您填写的( method=")
										.append(mvc.getMethod())
										.append(" )是错误的！它必须是一个有效的方法 ;\n");
							}
						}
					}
				} catch (ClassNotFoundException e) {
					sb.append("当前您填写的( class=").append(mvc.getClazz())
							.append(" )是错误的！它必须是一个有效的类 ;\n");
				} catch (InstantiationException e) {
					sb.append("当前您填写的( class=").append(mvc.getClazz())
							.append(" )是错误的！它必须是一个提供无参构造方法的类 ;\n");
				} catch (IllegalAccessException e) {
					sb.append("当前您填写的( class=").append(mvc.getClazz())
							.append(" )是错误的！它必须是一个有效的类 ;\n");
				}
			}

			if (!"".equals(sb.toString())) {
				error = "\n<br /><b>" + xmlFile + ":[bean name="
						+ mvc.getName() + "]</b>\n" + sb.toString();
			}
		}

		return error;
	}

	/**
	 * Check the Result part of MVC components configuration
	 * 
	 * @param rList
	 * @return
	 */
	public static String checkMVCResultPart(List<ResultConfigBean> rList,
			String beanID, String xmlFile) {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		if ("true".equalsIgnoreCase(cb.getMvc().getOpen())
				|| "1".equals(cb.getMvc().getOpen())) {
			StringBuilder sb = new StringBuilder();

			for (Iterator<ResultConfigBean> it = rList.iterator(); it.hasNext();) {
				ResultConfigBean r = it.next();
				if (!"forward".equalsIgnoreCase(r.getType())
						&& !"redirect".equalsIgnoreCase(r.getType())
						&& !"ajax".equalsIgnoreCase(r.getType())
						&& !"".equals(r.getType())) {
					sb.append("当前您填写的：( type=")
							.append(r.getType())
							.append(" )是错误的！它只能填写为：success|error|ajax|留空  中的一种 ;")
							.append("\n");
				}
			}

			if (!"".equals(sb.toString())) {
				error = "\n<br /><b>" + xmlFile + "[bean name=" + beanID
						+ "][result]:</b>\n" + sb.toString();
			}
		}

		return error;
	}

	/**
	 * Check the Validator part of MVC components configuration
	 * 
	 * @param rList
	 * @return
	 */
	public static String checkMVCValidator(List<ValidatorConfigBean> vList,
			String beanID, String xmlFile) {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		if ("true".equals(cb.getMvc().getOpen())
				|| "1".equals(cb.getMvc().getOpen())) {
			if (vList != null && !vList.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (Iterator<ValidatorConfigBean> it = vList.iterator(); it
						.hasNext();) {
					ValidatorConfigBean v = it.next();
					if (!"".equals(v.getName())) {
						if (!ValidatorConstant.REQUIRED_VAL.equalsIgnoreCase(v
								.getName())
								&& !ValidatorConstant.INT_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.EMAIL_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.DATE_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.URL_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.ID_CARD_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.ZIP_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.PHONE_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.QQ_VAL.equalsIgnoreCase(v
										.getName())
								&& !ValidatorConstant.IP_VAL
										.equals(v.getName())
								&& !ValidatorConstant.CHINESE_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.STRING_LENGTH_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.INT_SIZE_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.FORBID_VAL
										.equalsIgnoreCase(v.getName())
								&& !ValidatorConstant.ENUM_VAL
										.equalsIgnoreCase(v.getName())) {
							sb.append("当前您填写的：( name=").append(v.getName());
							sb.append(" )是错误的！它只能填写为：required|int|");
							sb.append("email|date|url|idCard|zip|phone|qq|ip|");
							sb.append("chinese|length|size|forbid|enum|留空  中的一种 ,忽略大小写 ;\n");
						} else if (ValidatorConstant.INT_SIZE_VAL
								.equalsIgnoreCase(v.getName())) {
							int minSize = 0;
							for (FieldConfigBean f : v.getField()) {
								for (ParamConfigBean p : f.getParam()) {
									if (ValidatorConstant.MIN_SIZE_PARAM
											.equalsIgnoreCase(p.getName())) {
										if (!(p.getValue()
												.matches(RegexList.integer_regexp))) {
											sb.append("<param>当前您填写的：( value=")
													.append(p.getValue());
											sb.append(" )是错误的！它应该填写为数字");

										} else {
											minSize = Integer.parseInt(p
													.getValue());
										}
									} else if (ValidatorConstant.MAX_SIZE_PARAM
											.equalsIgnoreCase(p.getName())) {
										if (!(p.getValue()
												.matches(RegexList.integer_regexp))) {
											sb.append("<param>当前您填写的：( value=")
													.append(p.getValue());
											sb.append(" )是错误的！它应该填写为数字");
										} else {
											int maxSize = Integer.parseInt(p
													.getValue());
											if (minSize > maxSize) {
												sb.append(
														"<param>当前您填写的：( value=")
														.append(p.getValue());
												sb.append(" )是错误的！它不能比minSize的值");
												sb.append(minSize).append("更小");
											}
										}
									} else {
										sb.append("<param>当前您填写的：( name=")
												.append(p.getName());
										sb.append(" )是错误的！它只能填写为：minSize|maxSize|");
										sb.append("中的一种 ,忽略大小写 ;\n");
									}
								}
							}
						} else if (ValidatorConstant.STRING_LENGTH_VAL
								.equalsIgnoreCase(v.getName())) {
							int minLength = 0;
							for (FieldConfigBean f : v.getField()) {
								for (ParamConfigBean p : f.getParam()) {
									if (ValidatorConstant.MIN_LENGTH_PARAM
											.equalsIgnoreCase(p.getName())) {
										if (!(p.getValue()
												.matches(RegexList.integer_regexp))) {
											sb.append("<param>当前您填写的：( value=")
													.append(p.getValue());
											sb.append(" )是错误的！它应该填写为数字");
										} else {
											minLength = Integer.parseInt(p
													.getValue());
										}
									} else if (ValidatorConstant.MAX_LENGTH_PARAM
											.equalsIgnoreCase(p.getName())) {
										if (!(p.getValue()
												.matches(RegexList.integer_regexp))) {
											sb.append("<param>当前您填写的：( value=")
													.append(p.getValue());
											sb.append(" )是错误的！它应该填写为数字");
										} else {
											int maxLength = Integer.parseInt(p
													.getValue());
											if (minLength > maxLength) {
												sb.append(
														"<param>当前您填写的：( value=")
														.append(p.getValue());
												sb.append(" )是错误的！它不能比minLength的值");
												sb.append(minLength).append(
														"更小");
											}
										}
									} else {
										sb.append("<param>当前您填写的：( name=")
												.append(p.getName());
										sb.append(" )是错误的！它只能填写为：minLength|maxLength|");
										sb.append("中的一种 ,忽略大小写 ;\n");
									}
								}
							}
						} else if (ValidatorConstant.FORBID_VAL
								.equalsIgnoreCase(v.getName())) {
							for (FieldConfigBean f : v.getField()) {
								for (ParamConfigBean p : f.getParam()) {
									if (!ValidatorConstant.FORBID_WORD_PARAM
											.equalsIgnoreCase(p.getName())) {
										sb.append("<param>当前您填写的：( name=")
												.append(p.getName());
										sb.append(" )是错误的！它只能填写为：forbidWord|");
										sb.append("忽略大小写 ;\n");
									}
								}
							}
						} else if (ValidatorConstant.ENUM_VAL
								.equalsIgnoreCase(v.getName())) {
							for (FieldConfigBean f : v.getField()) {
								for (ParamConfigBean p : f.getParam()) {
									if (!ValidatorConstant.ENUM_WORD_PARAM
											.equalsIgnoreCase(p.getName())) {
										sb.append("<param>当前您填写的：( name=")
												.append(p.getName());
										sb.append(" )是错误的！它只能填写为：enumWord|");
										sb.append("忽略大小写 ;\n");
									}
								}
							}
						}
					}

					if (!"".equals(v.getClazz())) {
						try {
							if (Class.forName(v.getClazz()) == null) {
								sb.append("当前您填写的( class=")
										.append(v.getClazz())
										.append(" )是错误的！它必须是一个有效的类 ;\n");
							}
						} catch (ClassNotFoundException e) {
							sb.append("当前您填写的( class=").append(v.getClazz())
									.append(" )是错误的！它必须是一个有效的类 ;\n");
						}
					}
				}

				if (!"".equals(sb.toString())) {
					error = "\n<br /><b>" + xmlFile + "[bean name=" + beanID
							+ "][validator]:</b>\n" + sb.toString();
				}
			}
		}

		return error;
	}
}
