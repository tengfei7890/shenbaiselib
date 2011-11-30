package test.sql;

import junit.framework.TestCase;
import junit.swingui.TestRunner;
import test.po.Master;
import test.po.Pet;

import com.cfuture08.eweb4j.config.EWeb4JConfig;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;

public class TestInsertSql extends TestCase {
	public TestInsertSql(){}
	public TestInsertSql(String name) {
		super(name);
		System.out.println("fuck");
		String error = EWeb4JConfig.start();
		if (error != null) {
			System.out.println(error);
			return;
		}
	}

	public void setup() {

	}

	/**
	 * 将若干个POJO的所有属性值插入数据库 例如： <code>
	 *  class Pet{
	 *  	private Integer id;
	 *  	private String name;
	 *  	private int age;
	 *      //此处省略setter和getter方法
	 *  }
	 * 	Pet pet = new Pet();
	 *  pet.setName("小黑");
	 *  pet.setAge(3);
	 *  insert(pet);
	 * </code> 会执行sql:INSERT INTO $table values('小黑','3');
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param ts
	 *            带有数据的POJO,可多个不同类型或同类型
	 * @return 如果插入成功,返回true,否则返回false.
	 */
	public <T> void testInsert() {
		Pet pet = new Pet();
		pet.setName("小黑");
		pet.setAge(3);
		pet.setType("dog");
		try {
			String sql = SqlFactory.getInsertSql(new Pet[] { pet }).create()[0];
			assertEquals(
					"INSERT INTO t_pet(name,type,age) VALUES('小黑','dog','3')  ;",
					sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 带Where条件子句,将若干个POJO所有属性值插入数据库 <code>
	 * 	class Pet{
	 * 		private Integer id;
	 * 		private String name;
	 * 		private int age;
	 * 		//此处省略setter和getter方法
	 * 	}
	 *  Pet pet = new Pet();
	 *  pet.setName("小白");
	 *  pet.setAge(4);
	 *  insertByCondition("xxx", pet);
	 * </code> 会执行sql:INSERT INTO $table values('小黑','3') WHERE xxx ;
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param condition
	 *            Where条件
	 * @param ts
	 *            带有数据的POJO,可多个不同类型或同类型
	 * @return 如果插入成功,返回true,否则返回false
	 */
	public <T> void testInsertByCondition() {
		Pet pet = new Pet();
		pet.setName("小黑");
		pet.setAge(3);
		pet.setType("dog");
		try {
			String sql = SqlFactory.getInsertSql(new Pet[] { pet }).create(
					"xxx = 'ooo'")[0];
			assertEquals(
					"INSERT INTO t_pet(name,type,age) VALUES('小黑','dog','3')  WHERE xxx = 'ooo' ;",
					sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给定POJO属性值,插入数据库 <code>
	 * 	class Pet{
	 * 		private Integer id;
	 * 		private String name;
	 * 		private int age;
	 * 		//此处省略setter和getter方法
	 * 	}
	 *  class Master{
	 *      private Integer id;
	 *      private String name;
	 *      private String gender;
	 *  }
	 *  
	 * 	Pet pet = new Pet();
	 *  pet.setName("小黄");
	 *  
	 *  Master master = new Master();
	 *  master.setGender("女");
	 *  
	 *  Object[] objs = new Object[]{master, pet};
	 *  String[] masterFields = new String[]{"gender"};
	 *  String[] petFields = new String[]{"name"};
	 *  String[][] fields = new String[][]{masterFields, petFields};
	 *  insertByFields(objs,masterFields, petFields);
	 * </code>会执行sql:INSERT INTO $masterTable(gender) values('女');INSERT INTO
	 * $petTable(name) values('小黄') ;
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param ts
	 *            带有数据的POJO,多个不同类型或同类型
	 * @param fields
	 *            按数组下标对应POJO的属性名
	 * @return 如果插入成功,返回true,否则返回false
	 */
	public <T> void testInsertByFields() {
		Pet pet = new Pet();
		pet.setName("小黑");
		pet.setAge(3);
		Master master = new Master();
		master.setGender("girl");
		pet.setType("dog");
		try {
			String sql = SqlFactory.getInsertSql(new Object[] { pet, master })
					.createByFields(new String[] { "name" },
							new String[] { "gender" })[0];
			assertEquals("INSERT INTO t_pet(name) VALUES('小黑') ;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestRunner.run(TestInsertSql.class);

	}
}
