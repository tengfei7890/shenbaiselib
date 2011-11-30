package com.cfuture08.eweb4j.mvc.config;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.cfuture08.eweb4j.cache.ActionConfigBeanCache;
import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.CheckConfigBean;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.mvc.config.bean.ActionConfigBean;
import com.cfuture08.util.BeanXMLUtil;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.StringUtil;

/**
 * Action类配置信息的读取、错误检查、自动修复
 * 
 * @author weiwei
 * 
 */
public class ActionConfig {
	/** 涉及到文件IO，需要同步保证正确性 */
	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);

		if (cb == null)
			return null;

		// eweb4j-start-config.xml中<mvc>节点里的actionXmlFile
		List<String> xmlFilePaths = cb.getMvc().getActionXmlFiles().getPath();
		for (String filePath : xmlFilePaths) {
			if (filePath == null || filePath.length() == 0)
				continue;

			// 解决中文文件夹名
			String unicodeDir = StringUtil.uriDecoding(FileUtil
					.getRootDir(ActionConfig.class));
			File configFile = new File(unicodeDir + filePath);
			try {
				List<ActionConfigBean> mvcList = BeanXMLUtil.getBeanXMLReader(
						configFile).read();
				if (mvcList == null || mvcList.isEmpty())
					error = ConfigErrCons.CANNOT_READ_CONFIG_INFO;
				else {
					for (Iterator<ActionConfigBean> it = mvcList.iterator(); it
							.hasNext();) {
						ActionConfigBean mvc = it.next();

						// 检查MVC.Action配置是否有错误
						String error1 = CheckConfigBean.checkMVCAction(mvc,
								filePath);
						if (error1 != null)
							if (error != null)
								error += error1;
							else
								error = error1;

						// 检查MVC.Action中的Result部分配置是否有错误
						String error2 = CheckConfigBean.checkMVCResultPart(
								mvc.getResult(), mvc.getName(), filePath);
						if (error2 != null)
							if (error != null)
								error += error2;
							else
								error = error2;

						// 检查MVC.Action.Validator中的部分配置是否有错误
						String error4 = CheckConfigBean.checkMVCValidator(
								mvc.getValidator(), mvc.getName(), filePath);
						if (error4 != null)
							if (error != null)
								error += error4;
							else
								error = error4;
					}

					// 如果没有任何错误，将Action的配置信息放入缓存，供框架运行期使用
					if (error == null)
						for (Iterator<ActionConfigBean> it = mvcList.iterator(); it
								.hasNext();) {
							ActionConfigBean mvc = it.next();
							if (!"".equals(mvc.getClazz()))
								if (!"".equals(mvc.getName()))
									ActionConfigBeanCache.add(mvc.getName(),
											mvc);
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					// 保存为备份文件
					File tf = new File(configFile.getAbsolutePath() + ".back"
							+ StringUtil.getNowTime("_MMddHHmmss"));
					FileUtil.copy(configFile, tf);
					BeanXMLUtil.getBeanXMLWriter(configFile,
							MVCConfigBeanCreator.getActionBean()).write();

					StringBuilder tsb = new StringBuilder(
							ConfigErrCons.CONFIG_INFO_READ_ERROR);
					tsb.append(StringUtil.getExceptionString(e));
					error = tsb.toString();
					// log-----
					LogFactory.getIOCLogger("ERROR").write(error);
				} catch (Exception e1) {
					e1.printStackTrace();
					StringBuilder sb2 = new StringBuilder(
							ConfigErrCons.CANNOT_REPAIR_CONFIG_FILE);
					sb2.append(StringUtil.getExceptionString(e1));

					error = sb2.toString();
					LogFactory.getIOCLogger("ERROR").write(error);
				}
			}
		}

		// 如果有错误，清空缓存
		if (error != null)
			ActionConfigBeanCache.clear();

		return error;
	}
}
