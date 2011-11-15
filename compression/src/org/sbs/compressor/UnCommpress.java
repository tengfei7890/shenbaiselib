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
import com.mucommander.file.impl.ar.ArArchiveFile;
import com.mucommander.file.impl.bzip2.Bzip2ArchiveFile;
import com.mucommander.file.impl.gzip.GzipArchiveFile;
import com.mucommander.file.impl.iso.IsoArchiveFile;
import com.mucommander.file.impl.rar.RarArchiveFile;
import com.mucommander.file.impl.sevenzip.SevenZipArchiveFile;
import com.mucommander.file.impl.tar.TarArchiveFile;
import com.mucommander.file.impl.zip.ZipArchiveFile;

/**
 * @author shenbaise
 * 
 */
public class UnCommpress<T> {

	public UnCommpress() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 解压zip文档
	 * 
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unZip(String filename, String outputDirectory) {
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		StringBuffer path = new StringBuffer();
		try {
			ZipArchiveFile zipArchiveFile = new ZipArchiveFile(file);
			if (zipArchiveFile.isArchive() && zipArchiveFile.isBrowsable())
				System.out.println("size:" + zipArchiveFile.getSize());
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			ArchiveEntryIterator iterator = zipArchiveFile.getEntryIterator();
			// 创建目标文件夹
			File f = new File(outputDirectory);
			f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(entry.getPath());
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					dirCount++;
				} else {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(
							entry.getPath().replace(entry.getName(), ""));
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					f = new File(path.append(File.separator).append(
							entry.getName()).toString());
					if (!f.exists())
						f.createNewFile();
					in = new BufferedInputStream(zipArchiveFile
							.getEntryInputStream(entry, iterator));
					out = new BufferedOutputStream(new FileOutputStream(f));
					/*
					 * int b; while ((b = in.read()) != -1) { out.write(b); }
					 */
					byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
					in.close();
					out.close();
					fileCount++;
				}
				// 处理下个
				entry = iterator.nextEntry();
			}
			System.out.println("文件夹:" + dirCount + "文件:" + fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压rar文档
	 * 
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unRar(String filename, String outputDirectory) {
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		StringBuffer path = new StringBuffer();
		try {
			RarArchiveFile rarArchiveFile = new RarArchiveFile(file);
			if (rarArchiveFile.isArchive() && rarArchiveFile.isBrowsable())
				System.out.println("size:" + rarArchiveFile.getSize());
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			ArchiveEntryIterator iterator = rarArchiveFile.getEntryIterator();
			// 创建目标文件夹
			File f = new File(outputDirectory);
			if (!f.exists())
				f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(entry.getPath());
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					dirCount++;
				} else {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(
							entry.getPath().replace(entry.getName(), ""));
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					f = new File(path.append(File.separator).append(
							entry.getName()).toString());
					if (!f.exists())
						f.createNewFile();
					in = new BufferedInputStream(rarArchiveFile
							.getEntryInputStream(entry, iterator));
					out = new BufferedOutputStream(new FileOutputStream(f));
					byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
					in.close();
					out.close();
					fileCount++;
				}
				// 处理下个
				entry = iterator.nextEntry();
			}
			System.out.println("文件夹:" + dirCount + "文件:" + fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解压tar文档
	 * 
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unTar(String filename, String outputDirectory) {
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		StringBuffer path = new StringBuffer();
		try {
			TarArchiveFile tarArchiveFile = new TarArchiveFile(file);
			if (tarArchiveFile.isArchive() && tarArchiveFile.isBrowsable())
				System.out.println("size:" + tarArchiveFile.getSize());
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			ArchiveEntryIterator iterator = tarArchiveFile.getEntryIterator();
			// 创建目标文件夹
			File f = new File(outputDirectory);
			if (!f.exists())
				f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(entry.getPath());
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					dirCount++;
				} else {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(
							entry.getPath().replace(entry.getName(), ""));
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					f = new File(path.append(File.separator).append(
							entry.getName()).toString());
					if (!f.exists())
						f.createNewFile();
					in = new BufferedInputStream(tarArchiveFile
							.getEntryInputStream(entry, iterator));
					out = new BufferedOutputStream(new FileOutputStream(f));
					byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
					in.close();
					out.close();
					fileCount++;
				}
				// 处理下个
				entry = iterator.nextEntry();
			}
			System.out.println("文件夹:" + dirCount + "文件:" + fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解压bz2文档
	 * 
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unBzip2(String filename, String outputDirectory) {
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		StringBuffer path = new StringBuffer();
		try {
			Bzip2ArchiveFile bzip2ArchiveFile = new Bzip2ArchiveFile(file);
			if (bzip2ArchiveFile.isArchive() && bzip2ArchiveFile.isBrowsable())
				System.out.println("size:" + bzip2ArchiveFile.getSize());
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			ArchiveEntryIterator iterator = bzip2ArchiveFile.getEntryIterator();
			// 创建目标文件夹
			File f = new File(outputDirectory);
			if (!f.exists())
				f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(entry.getPath());
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					dirCount++;
				} else {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(
							entry.getPath().replace(entry.getName(), ""));
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					f = new File(path.append(File.separator).append(
							entry.getName()).toString());
					if (!f.exists())
						f.createNewFile();
					in = new BufferedInputStream(bzip2ArchiveFile
							.getEntryInputStream(entry, iterator));
					out = new BufferedOutputStream(new FileOutputStream(f));
					byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
					in.close();
					out.close();
					fileCount++;
				}
				// 处理下个
				entry = iterator.nextEntry();
			}
			System.out.println("文件夹:" + dirCount + "文件:" + fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解压7zip文档
	 * 
	 * @param filename
	 * @param outputDirectory
	 */
	public static void un7zip(String filename, String outputDirectory) {
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		StringBuffer path = new StringBuffer();
		try {
			SevenZipArchiveFile sevenZipArchiveFile = new SevenZipArchiveFile(file);
			if (sevenZipArchiveFile.isArchive() && sevenZipArchiveFile.isBrowsable())
				System.out.println("size:" + sevenZipArchiveFile.getSize());
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			ArchiveEntryIterator iterator = sevenZipArchiveFile.getEntryIterator();
			// 创建目标文件夹
			File f = new File(outputDirectory);
			if (!f.exists())
				f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(entry.getPath());
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					dirCount++;
				} else {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(
							entry.getPath().replace(entry.getName(), ""));
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					f = new File(path.append(File.separator).append(
							entry.getName()).toString());
					if (!f.exists())
						f.createNewFile();
					in = new BufferedInputStream(sevenZipArchiveFile
							.getEntryInputStream(entry, iterator));
					out = new BufferedOutputStream(new FileOutputStream(f));
					byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
					in.close();
					out.close();
					fileCount++;
				}
				// 处理下个
				entry = iterator.nextEntry();
			}
			System.out.println("文件夹:" + dirCount + "文件:" + fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解压Gzip文档
	 * 
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unGzip(String filename, String outputDirectory) {
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		StringBuffer path = new StringBuffer();
		try {
			GzipArchiveFile gzipArchiveFile = new GzipArchiveFile(file);
			if (gzipArchiveFile.isArchive() && gzipArchiveFile.isBrowsable())
				System.out.println("size:" + gzipArchiveFile.getSize());
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			ArchiveEntryIterator iterator = gzipArchiveFile.getEntryIterator();
			// 创建目标文件夹
			File f = new File(outputDirectory);
			if (!f.exists())
				f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(entry.getPath());
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					dirCount++;
				} else {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(
							entry.getPath().replace(entry.getName(), ""));
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					f = new File(path.append(File.separator).append(
							entry.getName()).toString());
					if (!f.exists())
						f.createNewFile();
					in = new BufferedInputStream(gzipArchiveFile
							.getEntryInputStream(entry, iterator));
					out = new BufferedOutputStream(new FileOutputStream(f));
					byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
					in.close();
					out.close();
					fileCount++;
				}
				// 处理下个
				entry = iterator.nextEntry();
			}
			System.out.println("文件夹:" + dirCount + "文件:" + fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解压iso文档
	 * 
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unIso(String filename, String outputDirectory) {
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		StringBuffer path = new StringBuffer();
		try {
			IsoArchiveFile isoArchiveFile = new IsoArchiveFile(file);
			if (isoArchiveFile.isArchive() && isoArchiveFile.isBrowsable())
				System.out.println("size:" + isoArchiveFile.getSize());
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			ArchiveEntryIterator iterator = isoArchiveFile.getEntryIterator();
			// 创建目标文件夹
			File f = new File(outputDirectory);
			if (!f.exists())
				f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(entry.getPath());
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					dirCount++;
				} else {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(
							entry.getPath().replace(entry.getName(), ""));
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					f = new File(path.append(File.separator).append(
							entry.getName()).toString());
					if (!f.exists())
						f.createNewFile();
					in = new BufferedInputStream(isoArchiveFile
							.getEntryInputStream(entry, iterator));
					out = new BufferedOutputStream(new FileOutputStream(f));
					byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
					in.close();
					out.close();
					fileCount++;
				}
				// 处理下个
				entry = iterator.nextEntry();
			}
			System.out.println("文件夹:" + dirCount + "文件:" + fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解压Gzip文档
	 * 
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unAr(String filename, String outputDirectory) {
		int dirCount = 0;
		int fileCount = 0;
		AbstractFile file = FileFactory.getFile(filename);
		StringBuffer path = new StringBuffer();
		try {
			ArArchiveFile arArchiveFile = new ArArchiveFile(file);
			
			if (arArchiveFile.isArchive() && arArchiveFile.isBrowsable())
				System.out.println("size:" + arArchiveFile.getSize());
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			ArchiveEntryIterator iterator = arArchiveFile.getEntryIterator();
			// 创建目标文件夹
			File f = new File(outputDirectory);
			if (!f.exists())
				f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(entry.getPath());
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					dirCount++;
				} else {
					path = new StringBuffer(outputDirectory).append(
							File.separator).append(
							entry.getPath().replace(entry.getName(), ""));
					f = new File(path.toString());
					if (!f.exists())
						f.mkdirs();
					f = new File(path.append(File.separator).append(
							entry.getName()).toString());
					if (!f.exists())
						f.createNewFile();
					in = new BufferedInputStream(arArchiveFile
							.getEntryInputStream(entry, iterator));
					out = new BufferedOutputStream(new FileOutputStream(f));
					byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
					in.close();
					out.close();
					fileCount++;
				}
				// 处理下个
				entry = iterator.nextEntry();
			}
			System.out.println("文件夹:" + dirCount + "文件:" + fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解压文件，速度较慢
	 * 
	 * @param filename
	 * @param outputDirectory
	 */
	public static void unZipSlow(String filename, String outputDirectory) {
		int dirCount = 0;
		int fileCount = 0;
		FileOutputStream out = null;
		InputStream in = null;
		AbstractFile file = FileFactory.getFile(filename);
		try {
			ZipArchiveFile zipArchiveFile = new ZipArchiveFile(file);
			System.out.println("size:" + zipArchiveFile.getSize());
			ArchiveEntryIterator iterator = zipArchiveFile.getEntryIterator();
			// 创建目标文件夹
			File f = new File(outputDirectory);
			f.mkdir();
			ArchiveEntry entry = iterator.nextEntry();
			while (entry != null) {
				// 创建以zip包文件名为目录名的根目录
				if (entry.isDirectory()) {
					String dir = entry.getPath();
					f = new File(outputDirectory + File.separator + dir);
					if (!f.exists())
						f.mkdirs();
					dirCount++;
				} else {
					f = new File(outputDirectory + File.separator
							+ entry.getPath());
					if (!f.exists())
						f.createNewFile();
					in = zipArchiveFile.getEntryInputStream(entry, iterator);
					out = new FileOutputStream(f);
					/*
					 * int b; while ((b = in.read()) != -1) { out.write(b); }
					 */
					byte[] b = new byte[1024];
					while (in.read(b) != -1) {
						out.write(b);
					}
					out.close();
					in.close();
					fileCount++;
				}
				// 处理下个
				entry = iterator.nextEntry();
			}
			
			System.out.println("文件夹:" + dirCount + "文件:" + fileCount);
		} catch (UnsupportedFileOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// test();
		TimeCounter.start();
		unZip("G:/commpressorTestfile/apache-ant-1.8.2-bin.zip", "G:/cTestfile");
		
		System.err.println("cost:" + TimeCounter.costTime());
	}
}
