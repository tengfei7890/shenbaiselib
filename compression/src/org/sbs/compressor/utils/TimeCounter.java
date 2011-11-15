/**
 * 
 */
package org.sbs.compressor.utils;


/**
 * @author shenbaise
 *
 */
public class TimeCounter {
	
	private static Long start;
	private static Long end;

	public TimeCounter() {
		super();
	}
	
	public static void start(){
		start = System.currentTimeMillis();
	}
	
	public static void end(){
		end = System.currentTimeMillis();
	}
	
	public static Long costTime(){
		end();
		return end - start;
	}

}
