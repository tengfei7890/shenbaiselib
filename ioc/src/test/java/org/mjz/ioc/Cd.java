package org.mjz.ioc;

import org.mjz.ioc.annotation.Inject;

public class Cd {
	@Inject
	private Ce ce;
	@Inject("a")
	private Ia ia;
	@Inject
	private Cj cj;

}
