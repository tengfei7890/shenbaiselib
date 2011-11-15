/**
 * 
 */
package org.sbs.compressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.sbs.compressor.exception.CpxException;
import org.sbs.compressor.utils.TimeCounter;

import com.mucommander.file.AbstractFile;
import com.mucommander.file.FileFactory;
import com.mucommander.file.UnsupportedFileOperationException;
import com.mucommander.file.impl.zip.provider.ZipEntry;
import com.mucommander.file.impl.zip.provider.ZipOutputStream;

/**
 * @author shenbaise
 * 
 */
public class Compressor {

	public static int fileCount = 0;
	public static int innerFileCount = 0;
	public static int innerDirCount = 0;
	public static String[] files = {
			"G:/commpressorTestfile/apache-ant-1.8.2-bin.zip",
			"G:/commpressorTestfile/apktool1.4.1.tar.bz2",
			"G:/commpressorTestfile/MongoDB_003.rar",
			"G:/commpressorTestfile/mucommander-current-docs.tar.gz",
			"G:/commpressorTestfile/test7zip.7z" };

	/**
	 * 默认构造函数
	 */
	public Compressor() {
		super();
	}

	public static void test() {
		// 读取压缩文件
		for (int i = 0; i < files.length; i++) {
			fileCount++;
			try {
				AbstractFile abstractFile = FileFactory.getFile(files[i]);
				listFiles(abstractFile);
			} catch (UnsupportedFileOperationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("fileCount:" + fileCount + "\tinnerDirCount:"
				+ innerDirCount + "\tinnerFileCount:" + innerFileCount);
	}

	/**
	 * 打印所有文件路径
	 * 
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
				System.out.println("file-?" + file.getAbsolutePath());
			}
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param out
	 * @param f
	 * @param base
	 * @throws Exception
	 */
	private void zip(ZipOutputStream out, File f, String outPutDir) {
		
		try {
			if (f.isDirectory()) {
				File[] fl = f.listFiles();
				// out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
				out.putNextEntry(new ZipEntry(outPutDir + File.separator));

				outPutDir = outPutDir.length() == 0 ? "" : outPutDir + "/";
				for (int i = 0; i < fl.length; i++) {
					zip(out, fl[i], outPutDir + fl[i].getName());
				}
			} else {
				// out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
				out.putNextEntry(new ZipEntry(outPutDir));
				FileInputStream in = new FileInputStream(f);
				int b;
				System.out.println(outPutDir);
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void zip(String inputFile,String outPutFile) throws CpxException{
		File file = new File(inputFile);
		if(file.exists()){
			try {
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outPutFile));
				if (file.isDirectory()) {
					File[] fies = file.listFiles();
					for (int i = 0; i < fies.length; i++) {
						zip(fies[i].getAbsolutePath(),outPutFile);
					}
				}else {
					out.putNextEntry(new ZipEntry(outPutFile));
					FileInputStream in = new FileInputStream(file.getAbsoluteFile());
					int b;
//					System.out.println(outPutFile);
					while ((b = in.read()) != -1) {
						out.write(b);
					}
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else {
			throw new CpxException("没有找到目标文件。");
		}
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TimeCounter.start();
		try {
			zip("G:/testzip","G:/testzip.zip");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("用时：" + TimeCounter.costTime());
	}
}
