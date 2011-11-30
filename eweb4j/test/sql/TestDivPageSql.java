package test.sql;

import java.util.List;

import junit.framework.TestCase;
import junit.swingui.TestRunner;
import test.po.Pet;

import com.cfuture08.eweb4j.config.EWeb4JConfig;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;
import com.cfuture08.eweb4j.orm.sql.constant.DBType;
import com.cfuture08.eweb4j.orm.sql.constant.LikeType;
import com.cfuture08.eweb4j.orm.sql.constant.OrderType;

/**
 * 测试分页语句
 * 
 * @author CFuture.aw
 * @version 2011-05-11
 * @since 1.a.433
 * 
 */
public class TestDivPageSql extends TestCase {
	private Pet pet = new Pet();
	public TestDivPageSql(){
//		System.out.println("fuck");
//		String error = EWeb4JConfig.start();
//		if (error != null) {
//			System.out.println(error);
//			return;
//		}
	}
	public static void main(String[] args) {
		TestRunner.run(TestDivPageSql.class);
	}

	public void testNextOne() {
		try {
			this.pet.setId(5);
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).nextOne(1);
			assertEquals("SELECT * FROM t_pet WHERE id > 5 ORDER BY id ASC LIMIT 1;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testNextOneByField() {
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).nextOne("age", "3",1);
			assertEquals("SELECT * FROM t_pet WHERE age > 3 ORDER BY age ASC LIMIT 1;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testPreOne() {
		try {
			this.pet.setId(5);
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).preOne(1);
			assertEquals("SELECT * FROM t_pet WHERE id < 5 ORDER BY id ASC LIMIT 1;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testPreOneByField() {
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).preOne("id", "3",1);
			assertEquals("SELECT * FROM t_pet WHERE id < 3 ORDER BY id ASC LIMIT 1;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDivPageByWhere() {
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).divPage(2, 5, "age", 1,
					"xxx = 'ooo'");
			assertEquals(
					"SELECT * FROM t_pet  WHERE xxx = 'ooo' ORDER BY age ASC LIMIT 5, 5 ;",
					sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDivPage() {
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).divPage(1, 5, "age", 1);
			assertEquals("SELECT * FROM t_pet  ORDER BY age ASC LIMIT 0, 5 ;",
					sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDivPageOrderByIdField() {
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).divPage(1, 5, 1);
			assertEquals("SELECT * FROM t_pet  ORDER BY id ASC LIMIT 0, 5 ;", sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDivPageByWhereOrderByIdField() {
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).divPage(1, 5, 1,
					"ooo = 'xxx'");
			assertEquals(
					"SELECT * FROM t_pet  WHERE ooo = 'xxx' ORDER BY id ASC LIMIT 0, 5 ;",
					sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDivPageOrderByIdFieldDESC() {
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).divPage(1, 2);
			assertEquals("SELECT * FROM t_pet  ORDER BY id DESC LIMIT 0, 2 ;",
					sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDivPageOrderByIdFieldDESCAndByWhere() {
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).divPage(1, 2,
					"xxx = 'ooo'");
			assertEquals(
					"SELECT * FROM t_pet  WHERE xxx = 'ooo' ORDER BY id DESC LIMIT 0, 2 ;",
					sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDivPageByFieldIsValue() {
		String[] fields = new String[] { "name", "age" };
		String[] values = new String[] { "小黑", "4" };

		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).selectWhere(fields,
					values, "age", 1, 2, 5);
			assertEquals(
					"SELECT * FROM t_pet  WHERE name  =  '小黑'  AND age  =  '4'  ORDER BY age ASC LIMIT 5, 5 ;",
					sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testDivPageByFieldNotIsValue() {
		String[] fields = new String[] { "name", "age" };
		String[] values = new String[] { "小黑", "4" };

		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).selectWhereNot(fields,
					values, "age", -1, 1, 2);
			assertEquals(
					"SELECT * FROM t_pet  WHERE name  <>  '小黑'  AND age  <>  '4'  ORDER BY age DESC LIMIT 0, 2 ;",
					sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testDivPageByFieldIsValueByPOJO() {
		String[] fields = new String[] { "name", "age" };
		pet.setName("小白");
		pet.setAge(19);
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).selectWhere(fields,
					"age", 1, 2, 5);
			assertEquals(
					"SELECT * FROM t_pet  WHERE name  =  '小白'  AND age  =  '19'  ORDER BY age ASC LIMIT 5, 5 ;",
					sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testDivPageByFieldNotIsValueByPOJO() {
		String[] fields = new String[] { "name", "age" };
		pet.setName("小白");
		pet.setAge(19);
		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).selectWhereNot(fields,
					"age", 1, 2, 5);
			assertEquals(
					"SELECT * FROM t_pet  WHERE name  <>  '小白'  AND age  <>  '19'  ORDER BY age ASC LIMIT 5, 5 ;",
					sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testDivPageByFieldIsValueOrderByIdField() {
		String[] fields = new String[] { "name", "age" };
		String[] values = new String[] { "小黑", "4" };

		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).selectWhere(fields,
					values, 1, 2, 5);
			assertEquals(
					"SELECT * FROM t_pet  WHERE name  =  '小黑'  AND age  =  '4'  ORDER BY id ASC LIMIT 5, 5 ;",
					sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testDivPageByFieldNotIsValueOrderByIdField() {
		String[] fields = new String[] { "name", "age" };
		String[] values = new String[] { "小黑", "4" };

		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).selectWhereNot(fields,
					values, -1, 1, 2);
			assertEquals(
					"SELECT * FROM t_pet  WHERE name  <>  '小黑'  AND age  <>  '4'  ORDER BY id DESC LIMIT 0, 2 ;",
					sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public <T> List<T> divPageByFieldIsValues(T t, String[] fields,
			int orderType, int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldNotIsValues(T t, String[] fields,
			int orderType, int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldIsValue(Class<T> clazz, String[] fields,
			String[] values, int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldNotIsValue(Class<T> clazz,
			String[] fields, String[] values, int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldIsValue(T t, String[] fields,
			int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldNotIsValue(T t, String[] fields,
			int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public void testDivPageByFieldLikeValue() {
		String[] fields = new String[] {"name","age"};
		String[] values = new String[] {"猴子","11"};

		try {
			String sql = SqlFactory.getSelectSql(pet,DBType.MYSQL_DB).selectWhereLike(fields,
					values, LikeType.LEFT_LIKE, "age", OrderType.DESC_ORDER, 1,
					2);
			assertEquals("SELECT * FROM t_pet  WHERE name  LIKE  '猴子%'  OR age  LIKE  '11%'  ORDER BY age DESC LIMIT 0, 2 ;", sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public <T> List<T> divPageNotLike(Class<T> clazz, String[] fields,
			String[] values, int likeType, String orderField, int orderType,
			int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldLikeValue(T t, String[] fields,
			int likeType, String orderField, int orderType, int currPage,
			int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldNotLikeValue(T t, String[] fields,
			int likeType, String orderField, int orderType, int currPage,
			int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldLikeValue(Class<T> clazz, String[] fields,
			String[] values, int likeType, int orderType, int currPage,
			int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFiledNotLikeValue(Class<T> clazz,
			String[] fields, String[] values, int likeType, int orderType,
			int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldLikeValue(T t, String[] fields,
			int likeType, int orderType, int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageByFieldNotLikeValue(T t, String[] fields,
			int likeType, int orderType, int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageLike(Class<T> clazz, String[] fields,
			String[] values, int likeType, int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> divPageNotLike(Class<T> clazz, String[] columns,
			String[] values, int likeType, int currPage, int numPerPage) {
		// TODO Auto-generated method stub
		return null;
	}
}
