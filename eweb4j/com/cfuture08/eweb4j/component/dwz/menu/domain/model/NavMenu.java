package com.cfuture08.eweb4j.component.dwz.menu.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.orm.config.annotation.Column;
import com.cfuture08.eweb4j.orm.config.annotation.Id;
import com.cfuture08.eweb4j.orm.config.annotation.Many;
import com.cfuture08.eweb4j.orm.config.annotation.Table;
import com.cfuture08.util.JsonConverter;

/**
 * DWZ中导航菜单模型
 * 
 * @author weiwei
 * 
 */

@Table("t_NavMenu")
public class NavMenu implements Serializable {
	private static final long serialVersionUID = -4323821316335244056L;
	@Id
	@Column("id")
	private Long navMenuId;
	private String name;
	private String href;
	private int rank;
	@Many(target = TreeMenu.class, column = "navMenuId")
	private List<TreeMenu> treeMenus = new ArrayList<TreeMenu>();

	public Long getNavMenuId() {
		return navMenuId;
	}

	public void setNavMenuId(Long navMenuId) {
		this.navMenuId = navMenuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeMenu> getTreeMenus() {
		return treeMenus;
	}

	public void setTreeMenus(List<TreeMenu> treeMenus) {
		this.treeMenus = treeMenus;
	}

	public String getHref() {
		if (href != null) {
			if (href.contains("{id}")) {
				href = href.replace("{id}", String.valueOf(navMenuId));
			}
		}
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String toString() {
		return JsonConverter.convert(this);
	}
}
