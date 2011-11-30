package com.cfuture08.eweb4j.component.dwz.view;

import java.util.Iterator;
import java.util.List;

/**
 * <ul标签
 * 
 * @author weiwei
 * 
 */
public class UlTag {
	public UlTag() {
	}

	public UlTag(String clazz, List<LiTag> liList) {
		this.li = liList;
		this.clazz = clazz;
	}

	public List<LiTag> getLi() {
		return li;
	}

	public void setLi(List<LiTag> liList) {
		this.li = liList;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	private String clazz;
	private List<LiTag> li;

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.li != null && this.li.size() > 0) {
			if (this.clazz == null || "".equals(clazz)) {
				sb.append("<ul>");
			} else {
				sb.append("<ul class=\"");
				sb.append(this.clazz).append("\">");
			}

			for (Iterator<LiTag> it = this.li.iterator(); it.hasNext();) {
				sb.append(it.next().toString());
			}

			sb.append("</ul>");
		}
		return sb.toString();
	}
}
