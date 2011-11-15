import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;


public class LuceneDemo {  
    String filePath = "F:\\luceneDataSource\\hello.txt";
    String DataSourceDir = "F:\\luceneDataSource\\";
    File indexPath = new File("F:\\luceneIndex");  
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);  
      
 
    /**  
     * �������� IndexWriter ����ɾ����  
     * */ 
    @Test
    public void testCreatIndex() throws Exception {  
        // file-->Document  
    	File dir = new File(DataSourceDir);
    	if(!dir.exists())
    		dir.mkdirs();
    	if(dir.isDirectory()){
    		File[] files = dir.listFiles();
    		for (int i = 0; i < files.length; i++) {
    			Document doc = File2Document.file2Document(files[i].getAbsolutePath()); 
    			IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexPath), analyzer, true,MaxFieldLength.LIMITED);  
    	        indexWriter.addDocument(doc);  
    	        indexWriter.close();  
			}
    	}
//    	Document doc = File2Document.file2Document(filePath); 
//		IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexPath), analyzer, true,MaxFieldLength.LIMITED);  
//        indexWriter.addDocument(doc);  
//        indexWriter.close();  
         
    }  
   
 
    /**  
     * ���� IndexSearcher   
     * �������������н��в�ѯ  
     * */ 
    @Test
    public void testSearch() throws Exception {  
        String queryString = "Version";  
        //��Ҫ�������ı�����ΪQuery  
        String[] fields = {"name","content"};  
        QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_30, fields, analyzer); //��ѯ������  
        Query query = queryParser.parse(queryString);  
        //��ѯ  
        IndexSearcher indexSearcher = new IndexSearcher(FSDirectory.open(indexPath));  
        Filter filter = null;  
        TopDocs topDocs = indexSearcher.search(query, filter, 10000);//topDocs ���Ƽ���  
        System.out.println("�ܹ��С�"+topDocs.totalHits+"����ƥ����.");  
        //���      
            for(ScoreDoc scoreDoc:topDocs.scoreDocs){  
            int docSn = scoreDoc.doc;//�ĵ��ڲ����  
            Document doc = indexSearcher.doc(docSn);//�����ĵ����ȡ����Ӧ���ĵ�  
            File2Document.printDocumentInfo(doc);//��ӡ���ĵ���Ϣ  
              
        }  
 
    }  
 
 
 
 
}  