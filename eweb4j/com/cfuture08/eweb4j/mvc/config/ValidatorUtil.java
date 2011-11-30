package com.cfuture08.eweb4j.mvc.config;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.mvc.annotation.ValField;
import com.cfuture08.eweb4j.mvc.annotation.ValMess;
import com.cfuture08.eweb4j.mvc.annotation.ValParam;
import com.cfuture08.eweb4j.mvc.annotation.ValParamName;
import com.cfuture08.eweb4j.mvc.annotation.Validator;
import com.cfuture08.eweb4j.mvc.config.bean.FieldConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ParamConfigBean;
import com.cfuture08.eweb4j.mvc.config.bean.ValidatorConfigBean;

public class ValidatorUtil {
	/**
	 * 读取注解中验证器部分
	 * 
	 * @param actionIndex
	 * @param validatorAnn
	 * @param fieldAnn
	 * @param paramAnn
	 * @return
	 */
	public static List<ValidatorConfigBean> readValidator(
			Validator validatorAnn, ValField valFieldAnn, ValMess valMessAnn,
			ValParamName paramName, ValParam paramAnn) {
		List<ValidatorConfigBean> vList = new ArrayList<ValidatorConfigBean>();
		String[] name = validatorAnn.value();
		String[] clsName = validatorAnn.clazz();

		for (int a = 0; a < name.length; ++a) {
			ValidatorConfigBean v = new ValidatorConfigBean();
			if (name != null && name.length > a)
				v.setName(name[a]);

			if (clsName != null && clsName.length > a)
				v.setClazz(clsName[a]);

			if (valMessAnn == null || valFieldAnn == null)
				continue;

			//验证器数组下标
			int[] valIndex = valMessAnn.validator();
			//需要验证的属性域数组下标
			int[] fieldIndex = valMessAnn.field();

			String[] valField = valFieldAnn.value();
			String[] mess = valMessAnn.value();

			List<String> fnamelist = new ArrayList<String>();
			for (int in : fieldIndex)
				fnamelist.add(valField[in]);

			String[] fname = fnamelist.toArray(new String[] {});

			List<FieldConfigBean> fList = new ArrayList<FieldConfigBean>();
			for (int b = 0; b < valIndex.length; ++b) {
				if (valIndex[b] == a) {
					FieldConfigBean f = new FieldConfigBean();
					f.setName(fname[b]);
					f.setMessage(mess[b]);

					if (paramAnn == null || paramName == null)
						continue;

					int[] pindex = paramAnn.valMess();
					int[] pnameIndex = paramAnn.name();
					String[] pnames = paramName.value();
					
					List<String> pnamelist = new ArrayList<String>();
					for (int in : pnameIndex)
						pnamelist.add(pnames[in]);

					String[] pname = pnamelist.toArray(new String[] {});
					String[] pvalue = paramAnn.value();
					
					List<ParamConfigBean> pList = new ArrayList<ParamConfigBean>();
					for (int c = 0; c < pindex.length; ++c) {
						if (pindex[c] == b) {
							ParamConfigBean p = new ParamConfigBean();
							p.setName(pname[c]);
							p.setValue(pvalue[c]);
							pList.add(p);
						}
					}

					f.setParam(pList);
					fList.add(f);
				}
			}

			v.setField(fList);
			vList.add(v);
		}

		return vList;
	}
}
