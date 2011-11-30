package test.sql;

import junit.awtui.TestRunner;
import junit.framework.TestCase;
import test.po.Pet;

import com.cfuture08.eweb4j.config.EWeb4JConfig;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;
import com.cfuture08.eweb4j.orm.sql.constant.DBType;

/**
 * <b>测试SELECT语句</b>
 * 
 * @author CFuture.aw
 * @version 2011-05-10
 * @since 1.a.433
 * 
 */
public class TestSelectSql extends TestCase {
	public TestSelectSql() {}
	public TestSelectSql(String name) {
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
	 * <b>查询表中所有记录</b> <code>
	 * List<Pet> result = selectAll(Pet.class,"age",OrderType.ASC_ORDER);
	 * 执行sql：SELECT * FROM Pet ORDER BY age ASC ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param clazz
	 *            POJO的class对象
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型
	 * @return 查询成功返回类集否则返回null
	 */
	public void testSelectAll() {
		Pet pet = new Pet();
		String sql = SqlFactory.getSelectSql(pet, DBType.MYSQL_DB).selectAll(
				"age", 1);
		assertEquals("SELECT * FROM t_pet ORDER BY age ASC ;", sql);
	}

	/**
	 * <b>查询单个记录，给定属性名作为条件.</b> <code>
	 * pet.setName("小黑");
	 * pet.setAge(5);
	 * pet = selectOne(pet,"name","age");
	 * 执行sql：SELECT * FROM Pet WHERE name = '小黑' AND age = '5' ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定的属性名
	 * @return
	 * @throws Exception
	 */
	public void testSelectOneByFields() throws Exception {
		Pet pet = new Pet();
		pet.setName("小黑");
		pet.setAge(5);
		String sql = SqlFactory.getSelectSql(pet, DBType.MYSQL_DB).selectWhere(
				"name", "age");
		assertEquals(
				"SELECT * FROM t_pet WHERE name  =  '小黑'  AND age  =  '5'  ORDER BY id DESC ;",
				sql);
	}

	/**
	 * <b>查询单个记录，当确认给定条件是只能够查询一条记录时使用</b> <code>
	 * String[] fields = new String[]{"name","age"};
	 * String[] values = new String[]{"小白","8"};
	 * Pet pet = selectOne(Pet.class,fields,values);
	 * 执行sql：SELECT * FROM Pet WHERE name = '小白' AND age = '8' ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param clazz
	 *            POJO的class对象
	 * @param fields
	 *            属性名字
	 * @param values
	 *            属性值
	 * @return
	 * @throws Exception
	 */
	public void testSelectOne() throws Exception {
		Pet pet = new Pet();
		String[] fields = new String[] { "name", "age" };
		String[] values = new String[] { "小白", "8" };
		String sql = SqlFactory.getSelectSql(pet, DBType.MYSQL_DB).selectWhere(
				fields, values);
		assertEquals(
				"SELECT * FROM t_pet WHERE name  =  '小白'  AND age  =  '8'  ORDER BY id DESC ;",
				sql);
	}

	/**
	 * <b>自定义条件子句查询单个记录</b> <code>String condition = "xxx = 'ooo'";
	 * Pet pet = selectOneByWhere(Pet.class, condition);
	 * 执行sql：SELECT * FROM Pet WHERE xxx = 'ooo' ;</code>
	 * 
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @return
	 */
	public void testSelectOneByWhere() {
		Pet pet = new Pet();
		String condition = "xxx = 'ooo'";
		String sql = SqlFactory.getSelectSql(pet, DBType.MYSQL_DB).select(
				condition);
		assertEquals("SELECT * FROM t_pet WHERE xxx = 'ooo' ;", sql);
	}

	/**
	 * <b>查询某表的记录数</b> <code>
	 * long count = selectCount(Pet.class);
	 * 执行sql：SELECT COUNT(*) FROM Pet ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @return 查询出来的记录数
	 */
	public void testSelectCount() {
		Pet pet = new Pet();
		String sql = SqlFactory.getSelectSql(pet, DBType.MYSQL_DB).selectCount(
				null);
		assertEquals("SELECT COUNT(*) FROM t_pet ;", sql);
	}

	/**
	 * <b>查询某表的记录数(带WHERE条件)</b> <code>
	 * String condition = "xxx = 'ooo'";
	 * long count = selectCount(Pet.class, condition);
	 * 执行sql：SELECT COUNT(*) FROM Pet WHERE xxx = 'ooo' ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @return 查询出来的记录数
	 */
	public void testSelectCountByCondition() {
		Pet pet = new Pet();
		String condition = "xxx = 'ooo'";
		String sql = SqlFactory.getSelectSql(pet, DBType.MYSQL_DB).selectCount(
				condition);
		assertEquals("SELECT COUNT(*) FROM t_pet WHERE xxx = 'ooo' ;", sql);
	}

	/**
	 * <b>自定义条件查询</b> <code>
	 * String condition = "xxx = 'ooo'";
	 * 或者
	 * condition = "xxx like '%ooo%'";
	 * 或者
	 * 任何有效的WHERE条件子句
	 * List<Pet> result = selectWhere(Pet.class, condition);
	 * 执行:SELECT * FROM Pet WHERE xxx = 'ooo' ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param condition
	 *            条件
	 * @return
	 */
	public void testSelectWhere() {
		Pet pet = new Pet();
		String condition = "xxx = 'ooo'";
		String sql = SqlFactory.getSelectSql(pet, DBType.MYSQL_DB).select(
				condition);
		assertEquals("SELECT * FROM t_pet WHERE xxx = 'ooo' ;", sql);
	}

	public static void main(String[] args) {
		TestRunner.run(TestSelectSql.class);

	}
}
