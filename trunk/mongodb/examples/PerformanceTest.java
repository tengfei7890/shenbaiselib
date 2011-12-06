package examples;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class PerformanceTest {

	private Mongo mongo = null;

	public PerformanceTest() {
	}

	private Mongo getInstance() {
		if (mongo == null)
			try {
				mongo = new Mongo();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (MongoException e) {
				e.printStackTrace();
			}
		return mongo;
	}

	/**
	 * 插入简单数据
	 */
	public static void Performance() {
		PerformanceTest pt = new PerformanceTest();
		Mongo mongo = pt.getInstance();
		DB db = mongo.getDB("mytest");
		/*// make a document and insert it
		BasicDBObject doc = new BasicDBObject();

		doc.put("name", "MongoDB");
		doc.put("type", "database");
		doc.put("count", 1);

		BasicDBObject info = new BasicDBObject();

		info.put("x", 203);
		info.put("y", 102);

		doc.put("info", info);
		Set<String> colls = db.getCollectionNames();
		for (String s : colls) {
			System.out.println(s);
		}*/

		DBCollection coll = db.getCollection("testCollection");
		int i = 0;

		
		DBObject myDoc = coll.findOne();
//		System.out.println(myDoc);

		for (i = 0; i < 1000000; i++) {
			coll.insert(new BasicDBObject().append("i", i).append("j", i).append("k", i));
		}

		/*DBCursor cur = coll.find();
		while (cur.hasNext()) {
			System.out.println(cur.next());
		}
		coll.drop();*/
		System.out.println(coll.count());
	}
	
	public static void creatIndex(){
		PerformanceTest pt = new PerformanceTest();
		Mongo mongo = pt.getInstance();
		
		DB db = mongo.getDB("mytest");
		DBCollection coll = db.getCollection("testCollection");
		coll.createIndex(new BasicDBObject("i", 1));
	}
	
	public static void query(){
		PerformanceTest pt = new PerformanceTest();
		Mongo mongo = pt.getInstance();
		DB db = mongo.getDB("mytest");
		
		BasicDBObject query = new BasicDBObject();
        query.put("i", new BasicDBObject("$gt", 1234).append("$lte", 1240));  // i.e.   20 < i <= 30
        DBCollection coll = db.getCollection("testCollection");
        DBCursor cur = coll.find(query);

        while(cur.hasNext()) {
            System.out.println(cur.next());
        }
	}
	
	public static void main(String[] args) {
		
		Long cost = System.currentTimeMillis();
		
		PerformanceTest pt = new PerformanceTest();
		Mongo mongo = pt.getInstance();
		DB db = mongo.getDB("mytest");
		
		DBCollection coll = db.getCollection("testCollection");
		System.out.println("count:"+coll.count());
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Long.MAX_VALUE);
		//创建索引后，效率有大幅度的提升，600000条数据(仅有一列)没有索引时查询耗时2865毫秒，
		//创建索引后，查询耗时111毫秒。
//		creatIndex();
//		query();
//		System.err.println(System.currentTimeMillis());
//		System.out.println(System.currentTimeMillis());
//		Performance();
//		System.out.println(System.currentTimeMillis());
//		System.err.println(System.currentTimeMillis());
		cost = System.currentTimeMillis() - cost;
		System.out.println("cost:"+cost);
		
		/*
		coll.dropIndexes();
		coll.drop();
		db.dropDatabase();
		*/
	}

}
