package com.cfuture08.eweb4j.orm.config.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cfuture08.eweb4j.config.log.Logger;
import com.cfuture08.util.FileUtil;
import com.cfuture08.util.StringUtil;

public class ORMLogger implements Logger {
	private String filePath;
	private String level;
	private int maxSize;

	public ORMLogger(String level, String filePath, int maxSize) {
		this.filePath = filePath;
		this.level = level;
		this.maxSize = maxSize;
	}

	@Override
	public void write(String info) {
		BufferedWriter bw = null;
		try {
			if (this.filePath != null) {
				File file = new File(this.filePath);
				if (!file.exists()) {
					FileUtil.createFile(this.filePath);
				}
				if (file.length()/(1024*1024) >= this.maxSize){
					File tf = new File(file.getAbsolutePath()
							+ "."
							+ StringUtil.getNowTime("_MMddHHmmss"));
					FileUtil.copy(file, tf);
					file.delete();
					file = null;
					file = new File(this.filePath);
				}
				
				bw = new BufferedWriter(new FileWriter(file, true));
				StringBuilder sb = new StringBuilder();
				sb.append(StringUtil.getNowTime());
				sb.append(" -ORM -");
				sb.append(this.level);
				sb.append(" : ");
				sb.append(info);
				bw.newLine();
				bw.write(sb.toString());
				bw.close();
			}
		} catch (Exception e) {
			
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

}
