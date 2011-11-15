/**
 * 
 */
package org.sbs.compressor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.sbs.compressor.utils.TimeCounter;

import com.mucommander.file.AbstractFile;
import com.mucommander.file.ArchiveEntry;
import com.mucommander.file.ArchiveEntryIterator;
import com.mucommander.file.FileFactory;
import com.mucommander.file.UnsupportedFileOperationException;
import com.mucommander.file.impl.zip.ZipArchiveFile;

/**
 * @author shenbaise
 *
 */
public class CopyUnCommpress {
	
	public CopyUnCommpress() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * ��ѹzip�ĵ�
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unZip(String filename,String outputDirectory){
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		try {
			ZipArchiveFile zipArchiveFile = new ZipArchiveFile(file);
			if(zipArchiveFile.isArchive()&&zipArchiveFile.isBrowsable())
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
                    /*int b;
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }*/
                    byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
                    out.close();
                    fileCount++;
                }
                //�����¸�
                entry = iterator.nextEntry();
			}
			System.out.println("�ļ���:"+dirCount+"�ļ�:"+fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ѹ�ļ����ٶȽ���
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unZipSlow(String filename,String outputDirectory){
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
                    /*int b;
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }*/
                    byte[] b = new byte[1024];
					while (in.read(b) != -1) {
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		test();
		TimeCounter.start();
//		unZip("G:/commpressorTestfile/apache-ant-1.8.2-bin.zip","G:/cTestfile");
		unZipSlow("G:/commpressorTestfile/sdf.zip","G:/cTestfile");
//		unCompressor4("G:/commpressorTestfile/apache-ant-1.8.2-bin.zip","G:/cTestfile");
		System.err.println("��ʱ��"+TimeCounter.costTime());
	}
}
