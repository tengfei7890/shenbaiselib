package test.sql;

import junit.framework.TestCase;
import junit.swingui.TestRunner;
import test.po.Pet;

import com.cfuture08.eweb4j.config.EWeb4JConfig;
import com.cfuture08.eweb4j.orm.sql.SqlFactory;
import com.cfuture08.eweb4j.orm.sql.constant.DBType;
import com.cfuture08.eweb4j.orm.sql.constant.LikeType;
import com.cfuture08.eweb4j.orm.sql.constant.OrderType;

/**
 * 对SearchSql接口方法的测试
 * 
 * @author weiwei
 * 
 */
public class TestSearchSql extends TestCase {
	public TestSearchSql() {}
	public TestSearchSql(String name) {
		super(name);
		String error = EWeb4JConfig.start();
		if (error != null) {
			System.out.println(error);
		}
	}

	public void testSearchByDivPage() throws Exception {
		Pet pet = new Pet();
		String[] fields = new String[] {"name","type"};
		String[] values = new String[] {"weiwei","dog"};
		int likeType = LikeType.ALL_LIKE;
		boolean isLike = false;
		boolean isNot = false;
		boolean isOR = false;
		String orderField = null;
		int oType = OrderType.DESC_ORDER;
		int currentPage = -1;
		int numPerPage = -1;
		String sql = SqlFactory.getSelectSql(pet, DBType.MYSQL_DB).selectWhere(fields,
				values, likeType, isLike, isNot, isOR, orderField, oType,
				currentPage, numPerPage);
		assertEquals("", sql);
	}

	public static void main(String[] args) {
		TestRunner.run(TestSearchSql.class);
	}
}
