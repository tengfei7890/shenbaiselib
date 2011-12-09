package org.mjz.ioc;

import org.junit.Assert;
import org.junit.Test;

public class DefaultIocTest {
	
	/**
	 * 测试没有实体依赖的注
	 */
	@Test
	public void constructorSTest(){
		
		DefaultIoc defaultIoc = new DefaultIoc();

		defaultIoc.register("org.mjz.ioc.Ca","a");
		defaultIoc.register("org.mjz.ioc.Cb","b");
		defaultIoc.register(Cc.class);
		defaultIoc.register(Cc.class);
		defaultIoc.register(Cd.class);
		defaultIoc.register(Ce.class);
		defaultIoc.register(Cf.class);
		defaultIoc.register(Cg.class);
		defaultIoc.register(Ch.class);
		defaultIoc.register(Ci.class);
		defaultIoc.register(Cj.class);
		defaultIoc.register(Ck.class,"ck");
		
		defaultIoc.register(Cy.class);
		defaultIoc.register(Cx.class);
		defaultIoc.register(Cz.class);
		
		defaultIoc.showDetails();
		Ioc ioc  = (Ioc)defaultIoc;
		Cy cy = ioc.$(Cy.class);
		Cz cz = ioc.$(Cz.class);
		Assert.assertNotNull(cy);;
		cy.hello();
		cz.hello();
		System.out.println(ioc.size());
		ioc.destrory();
	}
	
	@Test
	public void xx(){
		DefaultIoc defaultIoc = new DefaultIoc();
		defaultIoc.register(Cl.class);
		defaultIoc.register(Cn.class);
		defaultIoc.register(Cm.class);
		defaultIoc.showDetails();
		Ioc ioc  = (Ioc)defaultIoc;
		Cm cm =ioc.$(Cm.class);
		cm.SetCl(ioc);
	}

}
