package com.cfuture08.eweb4j.component.dwz.menu.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.orm.config.annotation.Column;
import com.cfuture08.eweb4j.orm.config.annotation.Id;
import com.cfuture08.eweb4j.orm.config.annotation.Many;
import com.cfuture08.eweb4j.orm.config.annotation.One;
import com.cfuture08.eweb4j.orm.config.annotation.Table;

/**
 * DWZ中左边树形结构菜单模型
 * 
 * @author weiwei
 * 
 */
@Table("t_TreeMenu")
public class TreeMenu implements Serializable {
	private static final long serialVersionUID = 5264149396283615258L;
	@Id
	@Column("id")
	private Long treeMenuId = 0L;
	private String name;// 菜单名
	private String target = "navTab";// navTab或dialog
	private String rel;// navTabId
	private String reloadFlag = "1";// 是否自动刷新
	private String href;// 跳转路径
	private int rank;// 排序
	@One(column = "pid")
	private TreeMenu parent;// 父亲菜单
	@One(column = "navMenuId")
	private NavMenu navMenu = new NavMenu();// 所在的导航菜单
	@Many(target = TreeMenu.class, column = "pid")
	private List<TreeMenu> children = new ArrayList<TreeMenu>();// 孩子菜单们

	public Long getTreeMenuId() {
		return treeMenuId;
	}

	public void setTreeMenuId(Long treeMenuId) {
		this.treeMenuId = treeMenuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TreeMenu getParent() {
		return parent;
	}

	public void setParent(TreeMenu parent) {
		this.parent = parent;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
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
		return this.reloadFlag;
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

	public NavMenu getNavMenu() {
		return navMenu;
	}

	public void setNavMenu(NavMenu navMenu) {
		this.navMenu = navMenu;
	}

	public List<TreeMenu> getChildren() {
		return children;
	}

	public void setChildren(List<TreeMenu> children) {
		this.children = children;
	}

	public String toString() {
		Long pid = parent == null ? 0L : parent.treeMenuId;
		return "{\"treeMenuId\":\"" + treeMenuId + "\",\"navMenuId\":\""
				+ navMenu.getNavMenuId() + "\",\"pid\":\"" + pid
				+ "\",\"name\":\"" + name + "\",\"href\":\"" + href + "\"}";
	}
}
