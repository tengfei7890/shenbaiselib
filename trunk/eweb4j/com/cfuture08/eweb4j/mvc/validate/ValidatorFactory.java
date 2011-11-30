package com.cfuture08.eweb4j.mvc.validate;


public class ValidatorFactory {
	public static ValidatorIF getValidator(String type){
		ValidatorIF val = null;
		if (ValidatorConstant.REQUIRED_VAL.equalsIgnoreCase(type)){
			//必填字段
			return new RequriedValidator();
		}else if (ValidatorConstant.EMAIL_VAL.equalsIgnoreCase(type)){
			//字符串长度
			return new EMailValidator();
		}else if (ValidatorConstant.INT_VAL.equalsIgnoreCase(type)){
			//数字
			return new IntegerValidator();
		}else if (ValidatorConstant.DATE_VAL.equalsIgnoreCase(type)){
			//日期
			return new DateValidator();
		}else if (ValidatorConstant.URL_VAL.equalsIgnoreCase(type)){
			//URL
			return new URLValidator();
		}else if (ValidatorConstant.ID_CARD_VAL.equalsIgnoreCase(type)){
			//身份证
			return new IDCardValidator();
		}else if (ValidatorConstant.ZIP_VAL.equalsIgnoreCase(type)){
			//邮编
			return new ZIPValidator();
		}else if (ValidatorConstant.PHONE_VAL.equalsIgnoreCase(type)){
			//电话号码
			return new PhoneValidator();
		}else if (ValidatorConstant.QQ_VAL.equalsIgnoreCase(type)){
			//QQ
			return new QQValidator();
		}else if (ValidatorConstant.CHINESE_VAL.equalsIgnoreCase(type)){
			//中文
			return new ChineseValidator();
		}else if (ValidatorConstant.IP_VAL.equalsIgnoreCase(type)){
			//IP
			return new IPValidator();
		}else if (ValidatorConstant.STRING_LENGTH_VAL.equalsIgnoreCase(type)){
			//字符串长度
			return new StringLengthValidator();
		}else if (ValidatorConstant.INT_SIZE_VAL.equalsIgnoreCase(type)){
			//数字大小
			return new IntegerSizeValidator();
		}else if (ValidatorConstant.FORBID_VAL.equalsIgnoreCase(type)){
			//数字大小
			return new ForbidWordValidator();
		}else if (ValidatorConstant.ENUM_VAL.equalsIgnoreCase(type)){
			//枚举类型
			return new EnumValidator();
		}
			
		return val;
	}
}
