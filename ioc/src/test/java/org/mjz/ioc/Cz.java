package org.mjz.ioc;

import org.mjz.ioc.annotation.Inject;

public class Cz {
	@Inject
	private Ch ch;
	
	public void hello(){
		System.out.println(ch.hello());
	}

}
