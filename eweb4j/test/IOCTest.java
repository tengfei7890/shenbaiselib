package test;

import java.util.ArrayList;
import java.util.List;

import test.po.Pet;

public class IOCTest {
	public static void main(String[] args) {
//		String error = EWeb4JConfig.start();
//		if (error == null) {
//			ORMConfigBeanWriter.write("pet-orm.xml",Pet.class);
//			ORMConfigBeanWriter.write("master-orm.xml", Master.class);
//			List<Master> masterList = DAOFactory.getSelectDAO().selectAll(
//					Master.class);
//			if (masterList != null) {
//				for (Master m : masterList) {
//					System.out.println(m + "|" + m.getPets());
//					boolean flag = DAOFactory.getCascadeDAO().delete(m);
//					System.out.println(flag);
//					//System.out.println(m.getPets());
//					//break;
//				}
//			}

//			List<Pet> petList = DAOFactory.getSelectDAO().selectAll(Pet.class);
//			if (petList != null) {
//				for (Pet p : petList) {
//					System.out.println(p + "|" + p.getMaster());
//					DAOFactory.getCascadeDAO().select(p);
//					System.out.println(p.getMaster());
//					//break;
//				}
//			}

			// Pet pet = new Pet();
			// pet.setName("test");
			// pet.setType("dog");
			// pet.setAge(3);
			//
			// Master master = new Master();
			// master.setName("_test");
			// master.setGender("man");
			//
			// pet.addMaster(master);
			//
			// master = new Master();
			// master.setName("_test1");
			// master.setGender("man1");
			//
			// pet.addMaster(master);
			//
			// master = new Master();
			// master.setName("_test2");
			// master.setGender("man2");
			//
			// pet.addMaster(master);
			//
			// master = new Master();
			// master.setName("_test3");
			// master.setGender("man3");
			//
			// pet.addMaster(master);
			// boolean flag = DAOFactory.getCascadeDAO().insert(pet);
			// System.out.println(flag);
			
//			Pet pet = new Pet();
//			pet.setName("小日1");
//			pet.setType("dog");
//			Master master = new Master();
//			master.setName("日本人");
//			master.setGender("boy");
//			master.getPets().add(pet);
//			pet = new Pet();
//			pet.setName("小日2");
//			pet.setType("cat");
//			master.getPets().add(pet);
//			DAOFactory.getCascadeDAO().insert(master);
//			PetPO pet = new PetPO();
//			pet.setName("TEST1");
//			pet.setType("dog");
//			//DAOFactory.getInsertDAO().insert(pet);
//			try {
//				List<PetPO> pets = DAOFactory.getSelectDAO().selectAll(PetPO.class);
//				PetPO p = pets.get(0);
//				System.out.println(StringUtil.dateToStr(p.getCreateTime(),"yyyyMMdd HH:mm:ss z"));
//			} catch (DAOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			System.out.println(error);
//		}
		
			List<Class<?>> hasBindCls = new ArrayList<Class<?>>();
			hasBindCls.add(String.class);
			//hasBindCls.add(PetPO.class);
			for (int i = 0; i < 5; i++)
				if (!hasBindCls.contains(Pet.class))
					hasBindCls.add(Pet.class);
				else
					System.out.println("not-->"+i);
			
			System.out.println(hasBindCls.size());
	}
}
