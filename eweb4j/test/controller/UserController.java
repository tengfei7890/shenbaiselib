package test.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import test.po.Pet;

import com.cfuture08.eweb4j.mvc.annotation.Controller;
import com.cfuture08.eweb4j.mvc.annotation.Param;
import com.cfuture08.eweb4j.mvc.annotation.Singleton;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;

@Singleton
@Controller
public class UserController {
	@RequestMapping("hello/new")
	public String sayHello(HttpServletRequest request, PrintWriter out,
			Pet pet, @Param("fuck") String fuck,@Param("testID")int testID) {
		out.print(pet + "|fuck-->" + fuck+"|testID-->"+testID);
		return "ajax";
	}
}
