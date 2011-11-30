package test.sql;

import com.cfuture08.eweb4j.config.EWeb4JConfig;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;

import junit.framework.TestCase;
import junit.swingui.TestRunner;
import test.po.Pet;

public class TestUpdateSql extends TestCase {
	public TestUpdateSql() {}
	public TestUpdateSql(String name) {
		super(name);
		String error = EWeb4JConfig.start();
		if (error != null){
			System.out.println(error);
			return ;
		}
	}

	/**
	 * 修改表记录所有不为null值的字段，通过主键值作为条件
	 * 
	 * @param <T>
	 * @param ts
	 * @return
	 */
	public <T> void testUpdate() {
		Pet pet = new Pet();
		pet.setId(5);
		pet.setName("小黑");
		pet.setType("dog");
		pet.setAge(1111);
		try {
			String sql = SqlFactory.getUpdateSql(pet).update()[0];
			assertEquals("UPDATE t_pet SET name = '小黑',type = 'dog',age = '1111' WHERE id = '5' ;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改表记录所有字段，通过给定condition条件
	 * 
	 * @param <T>
	 * @param condition
	 * @param ts
	 * @return
	 */
	public <T> void testUpdateWhere() {
		
	}

	/**
	 * 修改给定字段
	 * 
	 * @param <T>
	 * @param t
	 *            给定的对象
	 * @param fields
	 *            给定的字段
	 * @return 返回布尔
	 */
	public <T> void testUpdateByField() {

	}

	/**
	 * 修改给定字段为给定值
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fields
	 * @param values
	 * @return
	 */
	public <T> void testUpdateByFieldAndValue() {

	}

	/**
	 * 执行给定sql
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sqls
	 * @return
	 */
	public <T> void testUpdateBySQL() {

	}

	/**
	 * 执行给定sql,支持？占位符
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> void testUpdateBySQLAndArgs() {

	}

	public static void main(String[] args) {
		TestRunner.run(TestUpdateSql.class);
	}
}
