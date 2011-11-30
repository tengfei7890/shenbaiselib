package test.cascade;

import java.util.List;

import test.po.Master;
import test.po.Pet;

import com.cfuture08.eweb4j.config.EWeb4JConfig;
import com.cfuture08.eweb4j.orm.dao.DAOException;
import com.cfuture08.eweb4j.orm.dao.factory.DAOFactory;

public class TestCascadeDAO {

	public static void testOneSelect() {
		List<Pet> petList;
		try {
			petList = DAOFactory.getSelectDAO().selectAll(Pet.class);
			if (petList != null) {
				for (Pet p : petList) {
					System.out.println(p + "|" + p.getMaster());
					DAOFactory.getCascadeDAO().select(p, "master");
					System.out.println(p.getMaster());
					// break;
				}
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testOneUpdate() {
		Pet pet = new Pet();
		pet.setId(211);
		pet.setAge(8);
		pet.setName("test");
		pet.setType("fuck");
		Master master = new Master();
		master.setId(180);
		pet.setMaster(master);
		DAOFactory.getCascadeDAO().update(pet, "master");
	}

	public static void testManyInsert() {
		Master master = new Master();
		master.setName("小日主人1");
		master.setGender("boy");
		Integer id = (Integer) DAOFactory.getInsertDAO().insert(master);
		master.setId(id);
		Pet pet = new Pet();
		pet.setName("小日1");
		pet.setType("dog");
		master.getPets().add(pet);

		pet = new Pet();
		pet.setName("小日2");
		pet.setType("cat");
		master.getPets().add(pet);

		DAOFactory.getCascadeDAO().insert(master);
	}

	public static void testManySelect() {
		List<Master> masterList;
		try {
			masterList = DAOFactory.getSelectDAO().selectAll(Master.class);
			if (masterList != null) {
				for (Master m : masterList) {
					System.out.println(m + "|" + m.getPets());
					DAOFactory.getCascadeDAO().select(m);
					System.out.println(m.getPets());
					// break;
				}
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void testManyDelete() {
		List<Master> masterList;
		try {
			masterList = DAOFactory.getSelectDAO().selectAll(Master.class);
			if (masterList != null) {
				for (Master m : masterList) {
					System.out.println(m + "|" + m.getPets());
					DAOFactory.getCascadeDAO().delete(m, "pets");

				}
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void testManyManyInsert() {
		// Pet pet = new Pet();
		// pet.setName("test");
		// pet.setType("dog");
		// pet.setAge(3);
		//
		// Master master = new Master();
		// master.setName("_test");
		// master.setGender("man");
		//
		// pet.getMasters().add(master);
		//
		// master = new Master();
		// master.setName("_test1");
		// master.setGender("man1");
		//
		// pet.getMasters().add(master);
		//
		// master = new Master();
		// master.setName("_test2");
		// master.setGender("man2");
		//
		// pet.getMasters().add(master);
		//
		// master = new Master();
		// master.setName("_test3");
		// master.setGender("man3");
		//
		// pet.getMasters().add(master);
		// boolean flag = DAOFactory.getCascadeDAO().insert(pet);
		// System.out.println(flag);

		Master master = new Master();
		// master.setId(36);
		master.setName("日本人");
		master.setGender("boy");
		Pet pet = new Pet();
		pet.setName("小日1");
		pet.setType("dog");
		pet.setId(6490);
		master.getPets().add(pet);

		pet = new Pet();
		pet.setName("小日2");
		pet.setType("cat");
		master.getPets().add(pet);

		DAOFactory.getCascadeDAO().insert(master);
	}

	/**
	 * 测试多对多更新
	 */
	public static void testManyManyUpdate() {
		List<Master> masterList = DAOFactory.getSelectDAO().selectAll(
				Master.class);
		if (masterList != null) {
			for (Master m : masterList) {
				Pet p = new Pet();
				p.setId(3);
				// DAOFactory.getCascadeDAO().select(m);
				m.getPets().add(p);
				DAOFactory.getCascadeDAO().update(m);
				break;
			}
		}
	}

	public static void testManyManySelect() {
		// List<Master> masterList = DAOFactory.getSelectDAO().selectAll(
		// Master.class);
		// if (masterList != null) {
		// for (Master m : masterList) {
		// System.out.println(m + "|" + m.getPets());
		// DAOFactory.getCascadeDAO().select(m, "pets");
		// System.out.println(m.getPets());
		// break;
		// }
		// }

		// List<Pet> petList;
		// try {
		// petList = DAOFactory.getSelectDAO().selectAll(Pet.class);
		// if (petList != null) {
		// for (Pet p : petList) {
		// System.out.println(p + "|" + p.getMasters());
		// DAOFactory.getCascadeDAO().select(p);
		// System.out.println(p.getMasters());
		// break;
		// }
		// }
		// } catch (DAOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		List<Master> masterList = DAOFactory.getSelectDAO().selectAll(
				Master.class);
		if (masterList != null) {
			for (Master m : masterList) {
				System.out.println(m + "|" + m.getPets());
				DAOFactory.getCascadeDAO().select(m);
				System.out.println(m + "|" + m.getPets());
				break;
			}

		}

	}

	public static void testManyManyDelete() {
		List<Master> masterList;
		try {
			masterList = DAOFactory.getSelectDAO().selectAll(Master.class);
			if (masterList != null) {
				for (Master m : masterList) {
					System.out.println(m + "|" + m.getPets());
					Pet p = new Pet();
					p.setId(5);
					m.getPets().add(p);
					DAOFactory.getCascadeDAO().delete(m, "pets");
					break;
				}
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String error = EWeb4JConfig.start();
		if (error == null) {
			 TestCascadeDAO.testOneSelect();
			// TestCascadeDAO.testOneUpdate();
//			 TestCascadeDAO.testManyInsert();
			// TestCascadeDAO.testManySelect();
			// TestCascadeDAO.testManyDelete();
			// TestCascadeDAO.testManyManyInsert();
			// TestCascadeDAO.testManyManySelect();
			// TestCascadeDAO.testManyManyDelete();
			// TestCascadeDAO.testManyManyUpdate();
			
		} else {
			System.out.println(error);
		}
	}
}
