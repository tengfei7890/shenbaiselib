package org.mjz.ioc;

import org.mjz.ioc.annotation.Constructor;
import org.mjz.ioc.annotation.Inject;

public class Cy {
	
	@Inject
	private Cd cd;
	private Cg cg;
	
	public Cy(){
		super();
	}
	
	@Constructor
	public Cy(Cg cg){
		this.cg = cg;
	}
	
	public void hello(){
		System.out.println(cg.hello());
	}

}
