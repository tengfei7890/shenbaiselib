package com.code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
/***
 * 编码转化器
 * @author admin
 */
public class EncodingChange {
	  String filesDir = "";  
	     String filesNDir = "";  
	   
	     public static void main(String args[]) throws IOException {  
	         cp("C:/Documents and Settings/Administrator/workspace/ABC/",  
	                 "C:/Documents and Settings/Administrator/workspace/ABC2",  
	                 "UTF-8", "GBK");  
	     }  
	   
	     static void cp(String baseDir, String ndir, String code1, String code2)  
	             throws IOException {  
	         File file = new File(baseDir);  
	         if(file.isDirectory())  
	          new File(ndir).mkdirs();  
	         if (file.isDirectory()) {  
	               
	             String list[] = file.list();  
	             for (String f : list) {  
	                 cp(baseDir + "/" + f, ndir + "/" + f, code1, code2);  
	             }  
	         } else {  
	             if (baseDir.indexOf(".php") > 0 || baseDir.indexOf(".html") > 0 || baseDir.indexOf(".js") > 0 || baseDir.indexOf(".xml") > 0 || baseDir.indexOf(".css") > 0) {  
	   
	                 convert(baseDir, ndir, code1, code2);  
	                 InputStreamReader read = new InputStreamReader(  
	                         new FileInputStream(baseDir), code1);// 或者UNICODE,UTF-16  
	                 BufferedReader reader = new BufferedReader(read);  
	                 String line;  
	   
	                 while ((line = reader.readLine()) != null) {  
	                     System.out.println(line);  
	                 }  
	                 reader.close();  
	   
	                 read.close();  
	             }  
	   
	         }  
	     }  
	   
	     public static void convert(String infile, String outfile, String from,  
	             String to) throws IOException, UnsupportedEncodingException {  
	           
	         // set up byte streams  
	         InputStream in;  
	         if (infile != null)  
	             in = new FileInputStream(infile);  
	         else  
	             in = System.in;  
	         OutputStream out;  
	         if (outfile != null)  
	             out = new FileOutputStream(outfile);  
	         else  
	             out = System.out;  
	   
	         // Use default encoding if no encoding is specified.  
	         if (from == null)  
	             from = System.getProperty("file.encoding");  
	         if (to == null)  
	             to = System.getProperty("file.encoding");  
	   
	         // Set up character stream  
	         Reader r = new BufferedReader(new InputStreamReader(in, from));  
	         Writer w = new BufferedWriter(new OutputStreamWriter(out, to));  
	   
	         // Copy characters from input to output. The InputStreamReader  
	         // converts from the input encoding to Unicode,, and the  
	         // OutputStreamWriter  
	         // converts from Unicode to the output encoding. Characters that cannot  
	         // be  
	         // represented in the output encoding are output as '?'  
	         char[] buffer = new char[4096];  
	         int len;  
	         while ((len = r.read(buffer)) != -1)  
	             w.write(buffer, 0, len);  
	         r.close();  
	         w.flush();  
	         w.close();  
	     }  
	   
}
