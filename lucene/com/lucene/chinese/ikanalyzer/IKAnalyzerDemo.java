package com.lucene.chinese.ikanalyzer;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

public class IKAnalyzerDemo {
	public static void main(String[] args) {
		String fieldName = "text";
		String text = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";   
		Analyzer analyzer = new IKAnalyzer();
		Directory dir = null;
		IndexWriter writer = null;
		IndexSearcher searcher = null;
		try {
			dir = new RAMDirectory();
			writer = new IndexWriter(dir, analyzer, true,
					IndexWriter.MaxFieldLength.LIMITED);
			System.out.println(IndexWriter.MaxFieldLength.LIMITED);
			Document doc = new Document();
			doc.add(new Field(fieldName, text, Field.Store.YES,
					Field.Index.ANALYZED));
			writer.addDocument(doc);
			writer.close();
			//在索引其中使用IKSimilarity相似评估度
			searcher = new IndexSearcher(dir);
			searcher.setSimilarity(new IKSimilarity());
			String keyword = "中文分词工具包";
			Query query = IKQueryParser.parse(fieldName, keyword);
			TopDocs topDocs = searcher.search(query, 5);
			System.out.println("命中："+topDocs.totalHits);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (int i = 0; i < scoreDocs.length; i++) {
				Document targetDoc = searcher.doc(scoreDocs[i].doc);
				System.out.println("內容："+targetDoc.toString());
			}
		} catch (Exception e) {
			System.out.println(e);
		}finally{
			try {
				searcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				dir.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
