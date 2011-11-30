package com.cfuture08.eweb4j.orm.jdbc.transaction;

import java.sql.SQLException;

import com.cfuture08.eweb4j.cache.SingleBeanCache;
import com.cfuture08.eweb4j.config.ConfigConstant;
import com.cfuture08.eweb4j.config.bean.ConfigBean;
import com.cfuture08.eweb4j.config.log.LogFactory;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.util.ExceptionInfoUtil;

public class Transaction {
	public static void execute(Trans trans,Object... args) throws DAOException {
		try {
			trans.begin();
			trans.run(args);// 执行事务
			trans.commit();
		} catch (Exception e) {
			try {
				trans.rollback();// 回滚事务
			} catch (SQLException e1) {
				throw new DAOException(e1.toString());
			}
			String info = ExceptionInfoUtil.toString(e);
			LogFactory.getORMLogger("info").write(info);
			String debug = ((ConfigBean) SingleBeanCache
					.get(ConfigConstant.CONFIGBEAN_ID)).getOrm().getDebug();
			if ("true".equals(debug) || "1".equals(debug))
				System.out.print("debug--ORM:Transaction rollback ");

			throw new DAOException(e.toString());
		} finally {
			try {
				trans.close();
			} catch (SQLException e) {
				throw new DAOException(e.toString());
			}
		}
	}
}
