package com.cfuture08.eweb4j.component.dwz.view;

public class CateSelector {
	private String html;

	public CateSelector(String name, String action, String optionname,
			String tabid, String[] names, String[] values) {
		StringBuilder sb = new StringBuilder();
		String select = "%s<select onchange=\"onSelectChange('%s','%s',this.value,'%s')\" >%s</select><li class=\"line\">line</li>";
		for (int i = 0; i < names.length; ++i) {
			sb.append(String.format("<option value='%s'>%s</option>",
					values[i], names[i]));
		}
		this.html = String.format(select, name, action, optionname, tabid,
				sb.toString());
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
}
