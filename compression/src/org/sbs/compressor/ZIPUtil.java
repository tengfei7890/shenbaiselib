package org.sbs.compressor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.sbs.compressor.utils.TimeCounter;

import com.mucommander.file.impl.tar.provider.TarEntry;
import com.mucommander.file.impl.tar.provider.TarOutputStream;
import com.mucommander.file.impl.zip.provider.ZipEntry;
import com.mucommander.file.impl.zip.provider.ZipOutputStream;

public class ZIPUtil {

	private ZIPUtil() {

	}

	private static ZIPUtil zu = null;

	public static ZIPUtil getInstance() {
		if (zu == null)
			zu = new ZIPUtil();
		return zu;

	}

	public void CreateZipFile(String filePath, String zipFilePath) {
		BufferedOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new BufferedOutputStream(new FileOutputStream(zipFilePath));
			zos = new ZipOutputStream(fos);
			writeZipFile(new File(filePath), zos, "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zos != null)
					zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void writeZipFile(File f, ZipOutputStream zos, String hiberarchy) {
		if (f.exists()) {
			if (f.isDirectory()) {
				hiberarchy += f.getName() + "/";
				File[] fif = f.listFiles();
				for (int i = 0; i < fif.length; i++) {
					writeZipFile(fif[i], zos, hiberarchy);
				}

			} else {
				BufferedInputStream bis = null;
				// FileInputStream fis = null;
				try {
					bis = new BufferedInputStream(new FileInputStream(f));
					// fis = new FileInputStream(f);
					ZipEntry ze = new ZipEntry(hiberarchy + f.getName());
					zos.putNextEntry(ze);
					byte[] b = new byte[1024];
					while (bis.read(b) != -1) {
						zos.write(b);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (bis != null)
							bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}

	}

	/**************************************************************** tar ****************************************************/

	public void CreateTarFile(String filePath, String zipFilePath) {
		BufferedOutputStream fos = null;
		TarOutputStream zos = null;
		try {
			fos = new BufferedOutputStream(new FileOutputStream(zipFilePath));
			zos = new TarOutputStream(fos);
			try {
				tartar(new File(filePath), zos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zos != null)
					zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void writeTarFile(File f, TarOutputStream zos, String hiberarchy) {
		if (f.exists()) {
			if (f.isDirectory()) {
				hiberarchy += f.getName() + "/";
				File[] fif = f.listFiles();
				for (int i = 0; i < fif.length; i++) {
					writeTarFile(fif[i], zos, hiberarchy);
				}

			} else {
				BufferedInputStream bis = null;
				// FileInputStream fis = null;
				try {
					bis = new BufferedInputStream(new FileInputStream(f));
					// fis = new FileInputStream(f);
					TarEntry ze = new TarEntry(hiberarchy + f.getName());
					zos.putNextEntry(ze);
					byte[] b = new byte[1024];
					while (bis.read(b) != -1) {
						zos.write(b);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (bis != null)
							bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}

	}

	public static void tartar(File dir, TarOutputStream tos) throws IOException {
		File[] flist = dir.listFiles();
		int buffersize = 1024;
		byte[] buf = new byte[buffersize];
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				tartar(flist[i], tos);
				continue;
			}
			String abs = dir.getAbsolutePath();
			String fabs = flist[i].getAbsolutePath();
//			if (fabs.startsWith(abs))
//				fabs = fabs.substring(abs.length());
			
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(flist[i]));
			TarEntry te = new TarEntry(fabs);
			te.setSize(flist[i].length());
			tos.setLongFileMode(2);
			tos.putNextEntry(te);
			int count = 0;
			while ((count = bis.read(buf, 0, buffersize)) != -1) {
				tos.write(buf, 0, count);
			}
			tos.closeEntry();
			bis.close();
		}
	}

	public static void main(String[] args) {
//		 TimeCounter.start();
//		 ZIPUtil.getInstance().CreateZipFile("G:/testzip","G:/testzip.zip");
//		 System.out.println("用时："+TimeCounter.costTime());
		TimeCounter.start();
		ZIPUtil.getInstance().CreateTarFile("G:/testzip", "G:/testzip.tar");
		System.out.println("用时：" + TimeCounter.costTime());
	}

}