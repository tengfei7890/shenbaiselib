/**
 * 
 */
package org.sbs.compressor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Timer;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.sbs.compressor.utils.TimeCounter;
import org.xml.sax.InputSource;

import com.mucommander.file.AbstractFile;
import com.mucommander.file.ArchiveEntry;
import com.mucommander.file.ArchiveEntryIterator;
import com.mucommander.file.FileFactory;
import com.mucommander.file.FileOperation;
import com.mucommander.file.UnsupportedFileOperationException;
import com.mucommander.file.impl.zip.ZipArchiveFile;
import com.mucommander.file.impl.zip.provider.ZipEntry;
import com.mucommander.io.RandomAccessInputStream;
import com.mucommander.io.RandomAccessOutputStream;

/**
 * @author shenbaise
 * 
 */
public class Test {
	
	public static int fileCount = 0;
	public static int innerFileCount = 0;
	public static int innerDirCount = 0;
	public static String[] files = { "G:/commpressorTestfile/apache-ant-1.8.2-bin.zip", "G:/commpressorTestfile/apktool1.4.1.tar.bz2",
			"G:/commpressorTestfile/MongoDB_003.rar", "G:/commpressorTestfile/mucommander-current-docs.tar.gz","G:/commpressorTestfile/test7zip.7z" };

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public Test() {
		super();
	}

	public static void test() {
		// ��ȡѹ���ļ�
		for (int i = 0; i < files.length; i++) {
			fileCount ++;
			try {
				AbstractFile abstractFile = FileFactory.getFile(files[i]);
				listFiles(abstractFile);
			} catch (UnsupportedFileOperationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("fileCount:"+fileCount+"\tinnerDirCount:"+innerDirCount+"\tinnerFileCount:"+innerFileCount);
	}
	/**
	 * ��ӡ�����ļ�·��
	 * @param file
	 * @throws UnsupportedFileOperationException
	 * @throws IOException
	 */
	public static void listFiles(AbstractFile file)
			throws UnsupportedFileOperationException, IOException {
		AbstractFile[] files = file.ls();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				innerDirCount++;
				System.out.println("dir->" + files[i].getPath());
				listFiles(files[i]);
			} else {
				innerFileCount++;
				String strFileName = files[i].getAbsolutePath();
				System.out.println("file->" + strFileName);
				System.out.println("file-?"+file.getAbsolutePath());
			}
		}
	}
	/**
	 * ��ѹ�ļ����ٶȽ���
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unCompressor(String filename,String outputDirectory){
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		try {
			ZipArchiveFile zipArchiveFile = new ZipArchiveFile(file);
			System.out.println("size:"+zipArchiveFile.getSize());
			InputStream in = null;
			FileOutputStream out = null;
			ArchiveEntryIterator iterator = zipArchiveFile.getEntryIterator();
			//����Ŀ���ļ���
			File f = new File(outputDirectory);
            f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while(entry!=null){
                //������zip���ļ���ΪĿ¼���ĸ�Ŀ¼
                
                if (entry.isDirectory()) {
                    String dir = entry.getPath();
//                    System.out.println("name: " + dir);
                    f = new File(outputDirectory + File.separator + dir);
                    if(!f.exists())
                    	f.mkdirs();
//                    System.out.println("mkdir: " + outputDirectory + File.separator + dir);
                    dirCount++;
                }
                else {
                    f = new File(outputDirectory + File.separator + entry.getPath());
                    if(!f.exists())
                    	f.createNewFile();
//                    System.out.println("path:"+f.getAbsolutePath());
                    
                    in = zipArchiveFile.getEntryInputStream(entry, iterator);
                    
                    out = new FileOutputStream(f);
                    int b;
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }
                    out.close();
                    fileCount++;
                }
                //�����¸�
                entry = iterator.nextEntry();
			}
			in.close();
			System.out.println("�ļ���:"+dirCount+"�ļ�:"+fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ѹ�ļ����ٶȽϿ�
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unCompressor2(String filename,String outputDirectory){
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		try {
			ZipArchiveFile zipArchiveFile = new ZipArchiveFile(file);
			System.out.println("size:"+zipArchiveFile.getSize());
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			ArchiveEntryIterator iterator = zipArchiveFile.getEntryIterator();
			//����Ŀ���ļ���
			File f = new File(outputDirectory);
            f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while(entry!=null){
                if (entry.isDirectory()) {
                    String dir = entry.getPath();
                    f = new File(outputDirectory + File.separator + dir);
                    if(!f.exists())
                    	f.mkdirs();
                    dirCount++;
                }
                else {
                    f = new File(outputDirectory + File.separator + entry.getPath());
                    if(!f.exists())
                    	f.createNewFile();
                    in = new BufferedInputStream(zipArchiveFile.getEntryInputStream(entry, iterator));
                    
                    out = new BufferedOutputStream(new FileOutputStream(f));
                    int b;
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }
                    out.close();
                    fileCount++;
                }
                //�����¸�
                entry = iterator.nextEntry();
			}
			in.close();
			System.out.println("�ļ���:"+dirCount+"�ļ�:"+fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������zip�ļ��ڲ�Ŀ¼�ṹ��û�н��н�ѹ���ٶȺܿ�
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unCompressor4(String filename,String outputDirectory){
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		try {
			ZipArchiveFile zipArchiveFile = new ZipArchiveFile(file);
			System.out.println("size:"+zipArchiveFile.getSize());
			FileInputStream fin = new FileInputStream(filename);
            FileChannel fcin = fin.getChannel();
			FileChannel fcout = null;
			Long pos = 0l;
			
			ArchiveEntryIterator iterator = zipArchiveFile.getEntryIterator();
			//����Ŀ���ļ���
			File f = new File(outputDirectory);
            f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while(entry!=null){
                if (entry.isDirectory()) {
                    String dir = entry.getPath();
                    f = new File(outputDirectory + File.separator + dir);
                    if(!f.exists())
                    	f.mkdirs();
                    pos+=entry.getSize();
                    dirCount++;
                }
                else {
                    f = new File(outputDirectory + File.separator + entry.getPath());
                    if(!f.exists())
                    	f.createNewFile();
    				fcout = new FileOutputStream(f).getChannel();
    				fcin.transferTo(pos, entry.getSize(), fcout);
    				pos+=entry.getSize();
    				fcout.close();
                    fileCount++;
                }
                //�����¸�
                entry = iterator.nextEntry();
			}
			fcin.close();
			System.out.println("�ļ���:"+dirCount+"�ļ�:"+fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ѹ��Ŀ¼
	 * @param dir
	 */
	public void commpressDir(String dir){
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		test();
		TimeCounter.start();
		unCompressor("G:/commpressorTestfile/apache-ant-1.8.2-bin.zip","G:/cTestfile");
//		unCompressor2("G:/commpressorTestfile/sdf.zip","G:/cTestfile");
//		unCompressor4("G:/commpressorTestfile/apache-ant-1.8.2-bin.zip","G:/cTestfile");
		System.err.println("��ʱ��"+TimeCounter.costTime());
	}
}
