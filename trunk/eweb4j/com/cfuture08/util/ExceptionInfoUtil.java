package com.cfuture08.util;

public class ExceptionInfoUtil {
	public static String toString(Exception e) {
		StringBuilder sbException = new StringBuilder(e.toString());
		for (StackTraceElement ste : e.getStackTrace()) {
			sbException.append("\n").append(ste.toString());
		}
		
		return sbException.toString();
	}
}
