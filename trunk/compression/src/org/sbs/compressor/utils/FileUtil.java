package org.sbs.compressor.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.sbs.compressor.exception.CpxException;

public class FileUtil {
	/**
	 * ���ֽ���ת�����ַ�������
	 * 
	 * @param is
	 *            ������
	 * @return �ַ���
	 */
	public static String readFileByLines(InputStream is) {
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString + "\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * ���ļ�һ��һ�еĶ���List����
	 * 
	 * @param file
	 *            ��Ҫ��ȡ���ļ�
	 * @return �ļ���һ�о���һ��List��Item�ķ���
	 */
	public static List<String> readFileToList(File file) {
		BufferedReader reader = null;
		List<String> list = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				list.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * ���ļ�����һ���ı��뷽ʽһ��һ�еĶ���List����
	 * 
	 * @param file
	 *            ��Ҫ��ȡ���ļ�
	 * @param encodType
	 *            �ַ�����
	 * @return �ļ���һ�о���һ��List��Item�ķ���
	 */
	public static List<String> readFileToList(File file, String encodType) {
		BufferedReader reader = null;
		List<String> list = new ArrayList<String>();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), encodType));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				if (!(tempString.charAt(0) >= 'a' && tempString.charAt(0) <= 'z'))
					tempString = tempString.substring(1);
				list.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * ��ָ�����ַ���������ָ���ķ�ʽд�뵽ָ�����ļ���
	 * 
	 * @param file
	 *            ��Ҫд�˵��ļ�
	 * @param content
	 *            ��Ҫд�������
	 * @param flag
	 *            �Ƿ�׷��д��
	 */
	public static void writeFile(File file, String content, Boolean flag) {
		try {
			if (!file.exists())
				file.createNewFile();
			FileWriter writer = new FileWriter(file, flag);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ָ�����ַ���������ָ���ķ�ʽ������д�뵽ָ�����ļ���
	 * 
	 * @param file
	 *            ��Ҫд�˵��ļ�
	 * @param content
	 *            ��Ҫд�������
	 * @param flag
	 *            �Ƿ�׷��д��
	 * @param encodType
	 *            �ļ�����
	 */
	public static void writeFile(File file, String content, Boolean flag,
			String encodType) {
		try {
			FileOutputStream writerStream = new FileOutputStream(file, flag);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					writerStream, encodType));
			writer.write(content);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����ļ���
	 * 
	 * @param oldPath
	 *            ԴĿ¼
	 * @param newPath
	 *            Ŀ��Ŀ¼
	 */
	public static void copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs();
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ļ�������
	 * 
	 * @param oldName
	 *            Դ�ļ���
	 * @param newName
	 *            ���ļ���
	 */
	public static void reName(String oldName, String newName) {
		File oldF = new File(oldName);
		File newF = new File(newName);
		oldF.renameTo(newF);
	}

	/**
	 * ��һ���ļ��б��ļ��������ļ�������ָ��Ŀ¼��
	 * 
	 * @param listFile
	 *            ������Ҫ�������ļ����б���ļ���ÿ���ļ�д��һ��
	 * @param targetFloder
	 *            Ŀ��Ŀ¼
	 */
	public static void copyFilesFromList(String listFile, String targetFloder) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(listFile));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				copyFile(tempString, targetFloder);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * �����ļ�
	 * 
	 * @param oldPath
	 *            Դ�ļ�
	 * @param newPath
	 *            Ŀ���ļ�
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			File temp = new File(oldPath);
			FileInputStream input = new FileInputStream(temp);
			FileOutputStream output = new FileOutputStream(newPath + "/"
					+ (temp.getName()).toString());
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ���ļ��б�
	 * 
	 * @param files
	 *            ��Ҫɾ�����ļ�/�ļ����б�
	 * @return ɾ���ɹ�true�����򷵻�false
	 */
	public static boolean deleteFiles(List<String> files) {
		boolean flag = true;
		for (String file : files) {
			flag = delete(file);
			if (!flag)
				break;
		}
		return flag;
	}

	/**
	 * ɾ���ļ����ļ���
	 * 
	 * @param fileName
	 *            Ҫɾ�����ļ���
	 * @return ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return false;
		} else {
			if (file.isFile())
				return deleteFile(fileName);
			else
				return deleteDirectory(fileName);
		}
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param fileName
	 *            Ҫɾ�����ļ����ļ���
	 * @return ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile())
			return file.delete();
		return false;
	}

	/**
	 * ɾ��Ŀ¼��Ŀ¼�µ��ļ�
	 * 
	 * @param dir
	 *            Ҫɾ����Ŀ¼·��
	 * @return ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean deleteDirectory(String dir) {
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		if ((!dirFile.exists()) || (!dirFile.isDirectory()))
			return false;
		boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			return false;
		}
		return dirFile.delete();
	}
	/**
	 * ��ָ�����������ļ�
	 * 
	 * @param fileName
	 * @param in
	 * @return file
	 */
	public static File createFile(String fileName, InputStream in) {
		try {
			File file = new File(fileName);
			File Dir = new File(fileName.substring(0, fileName
					.lastIndexOf("\\")));
			if (Dir.exists())
				Dir.mkdirs();

			if (!file.exists()) {
				if (file.createNewFile()) {
					int b = 0;
					BufferedOutputStream bout = new BufferedOutputStream(
							new FileOutputStream(new File(fileName)));
					while ((b = in.read()) != -1) {
						bout.write(b);
					}
				} else {
					throw new CpxException("�޷������ļ���");
				}
			}
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ʹ��BufferedInputStream��ʽ�����ļ���ֱ��·��
	 * 
	 * @param srcFile
	 * @param destDir
	 * @param newFileName
	 * @return
	 * @throws CpxException 
	 */
	public static long copyFile(File srcFile, File destDir, String newFileName) {
		long copySizes = 0;
		if (!srcFile.exists()) {
			System.out.println("Ŀ���ļ������ڣ�");
			copySizes = -1;
		} else if (!destDir.exists()) {
			System.out.println("Ŀ��Ŀ¼������");
			copySizes = -1;
		} else if (newFileName == null) {
			System.out.println("�ļ���Ϊnull");
			copySizes = -1;
		} else {
			try {
				BufferedInputStream bin = new BufferedInputStream(
						new FileInputStream(srcFile));
				BufferedOutputStream bout = new BufferedOutputStream(
						new FileOutputStream(new File(destDir, newFileName)));
				int b = 0, i = 0;
				long t1 = System.currentTimeMillis();
				while ((b = bin.read()) != -1) {
					bout.write(b);
					i++;
				}
				long t2 = System.currentTimeMillis();
				bout.flush();
				bin.close();
				bout.close();
				copySizes = i;
				long t = t2 - t1;
				System.out.println("������" + i + "���ֽ�\n" + "ʱ��" + t);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return copySizes;
	}

	/**
	 * ʹ��FileChannel�����ļ�
	 * 
	 * @param srcFile
	 * @param destDir
	 * @param newFileName
	 * @return
	 */
	public static long copyFileWithFileChannel(File srcFile, File destDir,
			String newFileName) {
		long copySizes = 0;
		if (!srcFile.exists()) {
			System.out.println("Դ�ļ�������");
			copySizes = -1;
		} else if (!destDir.exists()) {
			System.out.println("Ŀ��Ŀ¼������");
			copySizes = -1;
		} else if (newFileName == null) {
			System.out.println("�ļ���Ϊnull");
			copySizes = -1;
		} else {
			try {
				FileChannel fcin = new FileInputStream(srcFile).getChannel();
				FileChannel fcout = new FileOutputStream(new File(destDir,
						newFileName)).getChannel();
				long size = fcin.size();
				fcin.transferTo(0, fcin.size(), fcout);
				fcin.close();
				fcout.close();
				copySizes = size;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return copySizes;
	}
	
}