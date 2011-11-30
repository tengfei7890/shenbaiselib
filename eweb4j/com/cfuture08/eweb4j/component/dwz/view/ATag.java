package com.cfuture08.eweb4j.component.dwz.view;

import java.lang.reflect.Method;

import com.cfuture08.util.ReflectUtil;
import com.cfuture08.util.StringUtil;

/**
 * <a标签
 * 
 * @author weiwei
 * 
 */
public class ATag {
	public ATag() {
	}

	public ATag(String target, String rel, String reloadFlag, String href,
			String value) {
		this.target = target;
		this.rel = rel;
		this.reloadFlag = reloadFlag;
		this.href = href;
		this.value = value;
	}

	private String target;
	private String rel;
	private String reloadFlag;
	private String href;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getReloadFlag() {
		return reloadFlag;
	}

	public void setReloadFlag(String reloadFlag) {
		this.reloadFlag = reloadFlag;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("<a ");
			ReflectUtil ru = new ReflectUtil(this);
			String[] fieldsName = ru.getFieldsName();
			for (String name : fieldsName) {
				if (!"value".equals(name)) {
					Method m = ru.getMethod("get"
							+ StringUtil.toUpCaseFirst(name));
					if (m != null) {
						Object value = m.invoke(this);
						if (value != null && !"".equals(value)) {
							sb.append(" ").append(name).append("=\"");
							sb.append(m.invoke(this));
							sb.append("\"");
						}
					}
				}
			}

			sb.append(">").append(this.getValue()).append("</a>");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
