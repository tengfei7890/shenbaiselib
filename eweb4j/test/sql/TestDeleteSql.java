package test.sql;

import junit.framework.TestCase;
import junit.swingui.TestRunner;
import test.po.Pet;

import com.cfuture08.eweb4j.config.EWeb4JConfig;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;

/**
 * 测试删除sql语句创建类的所有方法
 * 
 * @author weiwei
 * 
 */
public class TestDeleteSql extends TestCase {
	public TestDeleteSql() {
	}

	public TestDeleteSql(String name) {
		super(name);
		System.out.println("fuck");
		String error = EWeb4JConfig.start();
		if (error != null) {
			System.out.println(error);
			return;
		}
	}

	/**
	 * 删除记录(按主键)
	 * 
	 * @param <T>
	 * @param ts
	 * @return
	 */
	public <T> void testDeleteById() {
		Pet pet = new Pet();
		pet.setId(10);
		try {
			String sql = SqlFactory.getDeleteSql(new Object[] { pet }).delete()[0];
			assertEquals("DELETE FROM t_pet WHERE id = '10' ;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除记录，按给定字段
	 * 
	 * @param <T>
	 * @param t
	 * @param columns
	 * @return
	 */
	public <T> void testDeleteByField() {
		Pet pet = new Pet();
		pet.setAge(30);
		try {
			String sql = SqlFactory.getDeleteSql(new Object[] { pet }).delete(
					new String[] { "age" })[0];
			assertEquals("DELETE FROM t_pet WHERE age = '30' ;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除记录，按给定字段、给定值
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fields
	 * @param values
	 * @return
	 */
	public <T> void testDeleteByFieldAndValue() {
		Pet pet = new Pet();
		try {
			String sql = SqlFactory.getDeleteSql(new Object[] { pet }).delete(
					new String[] { "age" }, new String[] { "50" })[0];
			assertEquals("DELETE FROM t_pet WHERE age = '50' ;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给定条件删除记录
	 * 
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @return
	 */
	public <T> void testDeleteWhere() {
		Pet pet = new Pet();
		pet.setName("weiwei");
		try {
			String sql = SqlFactory.getDeleteSql(new Object[] { pet })
					.deleteWhere("name = 'weiwei'");
			assertEquals("DELETE FROM t_pet WHERE name = 'weiwei' ;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给定条件删除记录，支持？占位符
	 * 
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @param args
	 * @return
	 */
	public <T> void testDeleteWhereByArgs() {
		Pet pet = new Pet();
		try {
			String sql = SqlFactory.getDeleteSql(new Object[] { pet })
					.deleteWhere("name = ?");
			assertEquals("DELETE FROM t_pet WHERE name = ? ;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestRunner.run(TestDeleteSql.class);
	}
}
