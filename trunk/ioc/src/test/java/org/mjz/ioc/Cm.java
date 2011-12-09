package org.mjz.ioc;

import org.mjz.ioc.annotation.Component;
import org.mjz.ioc.annotation.Inject;


public class Cm {
	

	private Cl cl;
	
	public void SetCl(Ioc ioc){
		cl = ioc.$(Cl.class);
	}

}
