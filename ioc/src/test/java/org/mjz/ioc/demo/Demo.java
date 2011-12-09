package org.mjz.ioc.demo;

import org.mjz.ioc.Ch;
import org.mjz.ioc.Ck;
import org.mjz.ioc.Ia;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Ch.class.getModifiers());
		System.out.println(Ck.class.getModifiers());
		System.out.println(Ia.class.getModifiers());
		System.out.println(Rss.class.getModifiers());
		
		System.out.println(0 > 0);

	}

}
