package test.cache;

import java.util.List;

import test.po.Pet;

import com.cfuture08.eweb4j.config.EWeb4JConfig;
import com.cfuture08.eweb4j.orm.dao.factory.DAOFactory;

public class TestCache {
	public static void main(String[] args) throws InterruptedException {
		String error = EWeb4JConfig.start();
		if (error == null) {
			int i = 0;
			long start = System.currentTimeMillis();
			for (;i < 300; i++) {
				List<Pet> pets = DAOFactory.getSelectDAO().selectAll(Pet.class);
//				if (pets != null)
//					System.out.println(pets);
			}
			System.out.println(System.currentTimeMillis()-start);
		} else
			System.out.println(error);
	}
}
