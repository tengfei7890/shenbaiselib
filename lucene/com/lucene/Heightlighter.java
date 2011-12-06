package com.lucene;

import java.io.File;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.lucene.util.CreateIndexerDir;

public class Heightlighter {

	public static void searcher(String keyword, File indexDir) {
		IndexSearcher searcher = null;
		try {
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT); // 创建一个查詢语法分析器
			Directory dir = FSDirectory.open(indexDir);
			QueryParser parser = new QueryParser(Version.LUCENE_CURRENT,
					"contents", analyzer);
			Query query = parser.parse(keyword);// 获取查询对象
			// 获取查询内容
			searcher = new IndexSearcher(dir, true); 
			TopDocs ts = searcher.search(query, 10);
			int totalHits = ts.totalHits; // 获取命中数
			System.out.println("命中数：" + totalHits);
			// 获取命中的文档信息对象 查询结果信息 。 它包括符合条件的Document的内部编号(doc)及评分(score) 。
			ScoreDoc[] hits = ts.scoreDocs;
			 // 用这个进行高亮显示，默认是<b>..</b>   
		      // 用这个指定<read>..</read>   
		      SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<read>", "</read>");   
		      // 构造高亮   
		      // 指定高亮的格式   
		      // 指定查询评分   
		      Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));   
		      // 这个一般等于你要返回的，高亮的数据长度   
		      // 如果太小，则只有数据的开始部分被解析并高亮，且返回的数据也少   
		      // 太大，有时太浪费了。   
		      highlighter.setTextFragmenter(new SimpleFragmenter(Integer.MAX_VALUE));   
		      // 这个100是指定关键字字符串的context的长度，你可以自己设定，因为不可能返回整篇正文内容
				highlighter.setTextFragmenter(new SimpleFragmenter(100));
			
			Document doc;
			for (int i = 0; i < hits.length; i++) {

				System.out.println(hits[i].doc);
				System.out.println(hits[i].score);
				// 打印文档的内容
				doc = searcher.doc(hits[i].doc);
				System.out.println(doc.toString());
				// 高亮出显示
				TermPositionVector tpv = (TermPositionVector) searcher.getIndexReader().getTermFreqVector(
						hits[i].doc, "contents");
				TokenStream tokenStream = TokenSources.getTokenStream(tpv, true);// 没有stopwords
				System.out.println(highlighter.getBestFragment(tokenStream, doc.get("contents")));
				// 没查API，我的理解是:先把content域的文本包装成Reader类型,调用MMAnalyzer的tokenStrea方法进行分词；
				// 分词结果是具有offset信息的.然后用highlighter对象的getBestFragment方法，把符合query条件的分词在文本中高亮标注
				// 有三个参数   
		        // 分析器   
		        // 要解析的字段名   
		        // 要解析的数据   
				System.out.println("***************");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		File src = new File("F:\\luceneDataSource\\");
		File destDir = new File("F:\\luceneIndex");
		CreateIndexerDir.index(src, destDir);
		Heightlighter.searcher("Hello", destDir);
	}
}
