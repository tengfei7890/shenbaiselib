package test.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import test.po.Master;
import test.po.Pet;

import com.cfuture08.eweb4j.mvc.annotation.Param;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;

/**
 * this is a pojo action test
 * 
 * 约定代替配置，可以看到整个action类真正实现0配置！ 方法名——> URL访问映射。 属性名——>URL参数名
 * 类名以Controller、Control、Action结尾的框架以控制器处理。
 * 
 * 并且action类兼容Struts2和SpringMVC3的写法。
 * 
 * @author weiwei
 * 
 */
public class ZeroConfigAction {
	/**
	 * 从当前会话中获取输出对象进行页面打印。 只需要声明即可，框架会自行注入。 返回类型为void、返回值为ajax（忽略大小写）、null的时候，
	 * 访问action后，框架不会做任何跳转。
	 * 
	 * @param out
	 */
	public void sayHello(PrintWriter out) {
		out.print("Hello!");
	}

	@RequestMapping("hello/{name}/{ints}")
	public void hello(PrintWriter out, @Param("ints") int ints) {
		out.print("name->" + name + ",ints->" + ints);
	}

	public void fuck(PrintWriter out, @Param("pet") Pet pet,
			@Param("name") String _name) {
		out.print("pet.name-->"+pet.getName()+",this.name-->"+_name);
	}

	/**
	 * 重定向跳转
	 * 
	 * @return
	 */
	public String testRedirect() {
		return "redirect:sayHello";
	}

	/**
	 * 服务端跳转
	 * 
	 * @return
	 */
	public String testForward() {
		return "index.jsp";// or "forward:index.jsp"
	}

	/**
	 * pojo action 的属性与url属性绑定，同时方法参数传递（框架实现）
	 * 
	 * @param out
	 */
	public void testPojoParam(PrintWriter out) {
		out.print(info);
	}

	/**
	 * 当pojo action 的属性是pojo的时候，支持无限嵌套的参数绑定。 当然，方法的参数是pojo这种情况也是一样的。
	 * //http://localhost
	 * :8080/eweb/testPojoParamAndUrlParam?name=thisName&pet.master
	 * .name=masterName&pet.name=petName
	 * 
	 * @param out
	 * @param fuck
	 */

	public void testPojoParamAndUrlParam(PrintWriter out) {
		out.print("this.name-->" + name + "<br/>\n-----|this.pet.name-->"
				+ pet.getName() + "<br/>\n----------|this.pet.master.name-->"
				+ pet.getMaster().getName());
	}

	/**
	 * pojo action 的属性有四个缺省值，只要声明+setter&getter方法。 框架就会在运行时刻实例化它们，并且传入当前会员的上下文。
	 * 这四个参数是：HttpServletRequest，HttpServletResponse，PrintWriter，HttpSession
	 */
	public void testOut() {
		out.print("out-->" + info);
	}

	// ------------------------------------------------------
	private Pet pet;
	private Master master;
	private String info;
	private String name;
	private HttpServletRequest req;
	private PrintWriter out;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Master getMaster() {
		return master;
	}

	public void setMaster(Master master) {
		this.master = master;
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
