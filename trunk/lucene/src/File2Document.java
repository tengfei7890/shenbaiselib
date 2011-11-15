import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

public class File2Document {  
    //文件属性： content,name,size,path  
    public static Document file2Document(String path){  
        File file = new File(path);  
        Document doc = new Document();  
        //Store.YES 是否存储 yes no compress   
        //Index 是否进行索引 Index.ANALYZED 分词后进行索引  
        doc.add(new Field("name",file.getName(),Store.YES,Index.ANALYZED));       
        doc.add(new Field("content",readFileContent(file),Store.YES,Index.ANALYZED));//readFileContent()读取文件类容        
        doc.add(new Field("size",String.valueOf(file.length()),Store.YES,Index.NOT_ANALYZED));//不分词,文件大小(int)转换成String         
        doc.add(new Field("path",file.getAbsolutePath(),Store.YES,Index.NOT_ANALYZED));//不需要根据文件的路径来查询    
        return doc;  
    }  
    /**  
     * 读取文件类容  
     * */ 
 
    private static String readFileContent(File file) {  
        try {  
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));  
            StringBuffer content = new StringBuffer();  
            try {  
                for(String line=null;(line = reader.readLine())!=null;){  
                    content.append(line).append("\n");  
                }  
            } catch (IOException e) {  
                   
                e.printStackTrace();  
            }  
            return content.toString();  
        } catch (FileNotFoundException e) {  
               
            e.printStackTrace();  
        }  
        return null;  
    }  
    /**  
     * <pre>  
     * 获取name属性值的两种方法  
     * 1.Filed field = doc.getFiled("name");  
     *         field.stringValue();  
     * 2.doc.get("name");  
     * </pre>  
     * @param doc  
     * */ 
    public static void printDocumentInfo(Document doc){  
        System.out.println("name -->"+doc.get("name"));  
        System.out.println("content -->"+doc.get("content"));  
        System.out.println("path -->"+doc.get("path"));  
        System.out.println("size -->"+doc.get("size"));  
          
    }  
 
}  