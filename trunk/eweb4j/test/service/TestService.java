package test.service;

import java.util.List;

import test.po.Pet;

import com.cfuture08.eweb4j.orm.dao.factory.DAOFactory;
import com.cfuture08.eweb4j.orm.jdbc.transaction.Trans;
import com.cfuture08.eweb4j.orm.jdbc.transaction.Transaction;

public class TestService {

	// 测试事务
	public String testTrans(Pet _pet) {
		String error = null;

		// 事务模板
		Transaction.execute(new Trans() {
			@Override
			public void run(Object... args) throws Exception {
				int arg1 = (Integer) args[0];
				int arg2 = (Integer) args[1];
				// some eweb4j dao
			}
		}, 1, 2);

		List<Pet> pets;

		pets = DAOFactory.getSelectDAO().selectAll(Pet.class);
		if (pets != null) {
		}

		return error;
	}
}
