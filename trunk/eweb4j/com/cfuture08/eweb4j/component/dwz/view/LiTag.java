package com.cfuture08.eweb4j.component.dwz.view;

/**
 * <li标签
 * 
 * @author weiwei
 * 
 */
public class LiTag {
	public LiTag() {
	}

	public LiTag(ATag dwza, UlTag dwzul) {
		this.a = dwza;
		this.ul = dwzul;
	}

	public ATag getA() {
		return a;
	}

	public void setA(ATag dwza) {
		this.a = dwza;
	}

	public UlTag getUl() {
		return ul;
	}

	public void setUl(UlTag dwzul) {
		this.ul = dwzul;
	}

	private ATag a;
	private UlTag ul;

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<li>");
		if (this.a != null) {
			sb.append(this.a.toString());
		}
		if (this.ul != null) {
			sb.append(this.ul.toString());
		}
		sb.append("</li>");
		return sb.toString();
	}
}
