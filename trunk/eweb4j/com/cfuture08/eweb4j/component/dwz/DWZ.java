package com.cfuture08.eweb4j.component.dwz;

import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotFindNavMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.CannotFindParentMenuException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuDataAccessException;
import com.cfuture08.eweb4j.component.dwz.menu.exception.MenuNotFoundException;
import com.cfuture08.eweb4j.component.dwz.view.CallBackJson;
import com.cfuture08.eweb4j.component.dwz.view.UlTag;

public interface DWZ {
	/**
	 * 给定一个导航菜单名字，取得其对应的树形菜单 例如： 导航菜单：文章管理，用户管理
	 * 
	 * 点击“文章管理”，树形菜单就变成： 文章管理 |——显示所有文章 |——新增文章 点击“用户管理”，树形菜单就变成： 用户管理 |——显示所有用户
	 * |——新增用户
	 * 
	 * @param navMenu
	 *            导航菜单
	 * @return
	 * @throws MenuDataAccessException
	 * @throws CannotFindNavMenuException
	 * @throws MenuNotFoundException
	 * @throws CannotFindParentMenuException
	 */
	public String getAccordion(String navMenuName)
			throws CannotFindNavMenuException, MenuDataAccessException,
			MenuNotFoundException, CannotFindParentMenuException;

	/**
	 * 给定一个导航菜单的数据库记录ID，取得其对应的树形菜单 例如： 导航菜单：文章管理，用户管理
	 * 
	 * 点击“文章管理”，树形菜单就变成： 文章管理 |——显示所有文章 |——新增文章 点击“用户管理”，树形菜单就变成： 用户管理 |——显示所有用户
	 * |——新增用户
	 * 
	 * @param navMenuId
	 *            导航菜单数据库保存记录的ID
	 * @return
	 * @throws MenuDataAccessException
	 * @throws CannotFindNavMenuException
	 * @throws CannotFindParentMenuException
	 */
	public String getAccordion(Long navMenuId)
			throws CannotFindNavMenuException, MenuDataAccessException,
			CannotFindParentMenuException;

	/**
	 * 创建并返回一个dwz的操作成功json信息。
	 * 
	 * @param message
	 *            消息文字
	 * @param navTabId
	 *            标签页ID
	 * @param forwardUrl
	 *            跳转路径
	 * @param callType
	 *            回调类型，@see CallBackType
	 * @param title
	 * @return
	 */
	public CallBackJson getSuccessJson(String message, String navTabId,
			String forwardUrl, String callType, String title);

	/**
	 * 创建并返回一个dwz的操作失败json信息
	 * 
	 * @param message
	 * @return
	 */
	public CallBackJson getFailedJson(String message);

	/**
	 * 给定根节点ID,创建一棵DWZ树形菜单HTML代码
	 * 
	 * @param rootNodeId
	 *            根节点
	 * @return
	 * @throws MenuDataAccessException
	 * @throws CannotFindParentMenuException
	 */
	public String createTreeMenu(Long rootNodeId)
			throws CannotFindParentMenuException, MenuDataAccessException;

	/**
	 * 给定根节点,创建一棵DWZ树形菜单(JAVA对象)
	 * 
	 * @param rootNodeId
	 *            根节点
	 * @return
	 * @throws MenuDataAccessException
	 * @throws CannotFindParentMenuException
	 */
	public UlTag getUlTag(Long rootNodeId)
			throws CannotFindParentMenuException, MenuDataAccessException;
}
