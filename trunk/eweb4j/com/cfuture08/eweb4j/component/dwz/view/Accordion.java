package com.cfuture08.eweb4j.component.dwz.view;

/**
 * 滑动菜单模型
 * @author weiwei
 *
 */
public class Accordion {
	private String name;
	private UlTag ul;
	public Accordion(){
		
	}
	public Accordion(String name, UlTag ul){
		this.name = name;
		this.ul = ul;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UlTag getUl() {
		return ul;
	}

	public void setUl(UlTag ul) {
		this.ul = ul;
	}

	public String toString() {
		String format = "<div class = \"trendsTree\"><div class=\"accordionHeader\"><h2><span>Folder</span>%s</h2></div><div class=\"accordionContent\">%s</div></div>";
		if (this.name == null) {
			name = "";
		}
		if (this.ul == null) {
			return String.format(format, this.name, "");
		} else {
			return String.format(format, this.name, this.ul.toString());
		}
	}
}
