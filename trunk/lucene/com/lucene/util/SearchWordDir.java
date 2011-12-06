package com.lucene.util;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchWordDir {
	/**
	 * 查询字符串
	 * 
	 * @param keyword
	 *            搜索单词
	 * @param indexDir
	 *            索引文件夹
	 */
	public static void searcher(String keyword, File indexDir) {
		IndexSearcher isearcher = null;
		Directory directory = null;
		try {
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT); // 创建一个查詢语法分析器
			directory = FSDirectory.open(indexDir);
			// 检索前，需要对检索字符串进行分析，这是由QueryParser来完成的。为了保证查询的正确性，最好用创建索引文件时同样的分析器。QueryParser解析字符串时，可以指定查询域，实际可以在字符串中指定一个或多个域。例如：“Info:电视台
			// AND ID:3329”，“Info:电视台”，“电视台”，假如不指定默认域，就会在默认域查询。
			// QueryParser调用静态方法Parse后会返回Query的实例，原子查询。例如：“Info:电视台 AND
			// ID:3329”会返回BooleanQuery，“Info:电视台”或“电视台”会返回PhraseQuery，“台”会返回TermQuery。
			// 3.1 Lucene内建Query对象
			// TermQuery：词条查询。通过对某个词条的指定，实现检索索引中存在该词条的所有文档。
			// BooleanQuery：布尔查询。Lucene中包含逻辑关系：“与”，“或”，“非”的复杂查询，最终都会表示成BooleanQuery。布尔查询就是一个由多个子句和子句之间组成的布尔逻辑所组成的查询。
			// RangeQuery：范围查询。这种范围可以是日期，时间，数字，大小等等。
			// PrefixQuery：前缀查询。
			// PhraseQuery：短语查询。默认为完全匹配，但可以指定坡度（Slop，默认为0）改变范围。比如Slop=1，检索短语为“电台”，那么在“电台”中间有一个字的也可以被查找出来，比如“电视台”。
			// MultiPhraseQuery：多短语查询。
			// FuzzyQuery：模糊查询。模糊查询使用的匹配算法是levensh-itein算法。此算法在比较两个字符串时，将动作分为3种：加一个字母（Insert），删一个字母（Delete），改变一个字母（Substitute）。
			// WildcardQuery：通配符查询。“*”号表示0到多个字符，“？”表示单个字符。
			// SpanQuery：跨度查询。此类为抽象类。
			// SpanTermQuery：检索效果完全同TermQuery，但内部会记录一些位置信息，供SpanQuery的其它API使用，是其它属于SpanQuery的Query的基础。
			// SpanFirstQuery：查找方式为从Field的内容起始位置开始，在一个固定的宽度内查找所指定的词条。
			// SpanNearQuery：功能类似PharaseQuery。SpanNearQuery查找所匹配的不一定是短语，还有可能是另一个SpanQuery的查询结果作为整体考虑，进行嵌套查询。
			// SpanOrQuery：把所有SpanQuery查询结果综合起来，作为检索结果。
			// SpanNotQuery：从第一个SpanQuery查询结果中，去掉第二个SpanQuery查询结果，作为检索结果。
			// 创建解析器
			QueryParser parser = new QueryParser(Version.LUCENE_CURRENT,
					"contents", analyzer);
			Query query = parser.parse(keyword);// 获取查询对象

			// Query query1 = new TermQuery(new Term("contents", keyword));
			// Query query2 = new TermQuery(new Term("contents", keyword2));
			// 括：BooleanClause.Occur.MUST，BooleanClause.Occur.MUST_NOT，BooleanClause.Occur.SHOULD。有以下6种组合：
			// A．MUST和MUST：取得连个查询子句的交集。
			// B．MUST和MUST_NOT：表示查询结果中不能包含MUST_NOT所对应得查询子句的检索结果。
			// C．MUST_NOT和MUST_NOT：无意义，检索无结果。
			// D．SHOULD与MUST、SHOULD与MUST_NOT：SHOULD与MUST连用时，无意义，结果为MUST子句的检索结果。与MUST_NOT连用时，功能同MUST。
			// E．SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。

			// BooleanQuery query = new BooleanQuery();
			// query.add(query1, Occur.SHOULD);
			// query.add(query2, Occur.SHOULD);

			// QueryParser parser = new
			// MultiFieldQueryParser(Version.LUCENE_CURRENT, new
			// String[]{"path", "contents"}, analyzer);
			// Query query = parser.parse(keyword);

			isearcher = new IndexSearcher(directory, true); // 创建索引搜索器
			// 多索引搜索：
			// 根据不同的索引目录创建多个Searcher：
			// IndexSearcher Searcher1=new IndexSearcher(INDEX_STORE_PATH1);
			// IndexSearcher Searcher2=new IndexSearcher(INDEX_STORE_PATH2);
			// IndexSearcher [] searchers={ Searcher1, Searcher2};
			// MultiSearcher searcher=new MultiSearcher(searchers);
			// Hits hits=searcher.search(q)

			// 执行搜索，获取查询结果集对象
			//TopDocs search(Query query Filter filter int n); 
			//执行查询 。Filter: 用来过虑搜索结果的对象  n指的是最多返回的Document的数量 。  //搜索相似度最高的n条记录   
			TopDocs ts = isearcher.search(query,100);
			int totalHits = ts.totalHits; // 获取命中数
			System.out.println("命中数：" + totalHits);
			// 获取命中的文档信息对象 查询结果信息 。 它包括符合条件的Document的内部编号(doc)及评分(score) 。
			ScoreDoc[] hits = ts.scoreDocs;  //老版本中 Hits --length
			for (int i = 0; i < hits.length; i++) {
				// 根据命中的文档的内部编号获取该文档 老版本中 Hits --doc(n)
				Document hitDoc = isearcher.doc(hits[i].doc); 
				// 输出该文档指定域的值 老版本中 Hits --id(n)
				System.out.println(hitDoc.getField("contents").stringValue()); 
				//以上的方法需要在文档还没有被放入缓存之前，就将其从索引中读取出来。因此我们建议你，
				//若非真正需要显示或者访问这些文档，就不要调用这些方法。
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (isearcher != null) {
				try {
					isearcher.close(); // 关闭搜索器
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (directory != null) {
				try {
					directory.close(); // 关闭索引存放目录
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 检索索引
	 * 
	 * @param query
	 *            索引查找字符串
	 * @return 返回结果：id的数组
	 */
	public int[] indexSearch(String query, File indexDir) {
		try {

			IndexSearcher seacrcher = new IndexSearcher(FSDirectory
					.open(indexDir));
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);// jeasy.analysis.MMAnalyzer();
			BooleanClause.Occur[] clauses = { BooleanClause.Occur.SHOULD,
					BooleanClause.Occur.SHOULD };
			Query q = MultiFieldQueryParser.parse(Version.LUCENE_CURRENT,
					query, new String[] { "name", "content" }, clauses,
					analyzer);
			// org.apache.lucene.queryParser.QueryParser mq=new
			// org.apache.lucene.queryParser.QueryParser("name",analyzer);
			// mq.setDefaultOperator(org.apache.lucene.queryParser.QueryParser.AND_OPERATOR);
			// Query q=mq.parse(query);
			System.out.println("查寻表达式：" + q.toString());
			TopDocs ts = seacrcher.search(q, null, 100);
			int totalHits = ts.totalHits; // 获取命中数
			System.out.println("命中数：" + totalHits);
			ScoreDoc[] hits = ts.scoreDocs; // 获取命中的文档信息对象
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = seacrcher.doc(hits[i].doc); // 根据命中的文档的内部编号获取该文档
				System.out.println(hitDoc.getField("contents").stringValue()); // 输出该文档指定域的值
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		File src = new File("C:\\Documents and Settings\\admin\\桌面\\cs");
		File destDir = new File("C:\\Documents and Settings\\admin\\桌面\\test");
		CreateIndexerDir.index(src, destDir);
		SearchWordDir.searcher("页面", destDir);
	}
}
