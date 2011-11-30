package test.controller;

import java.io.PrintWriter;

import com.cfuture08.eweb4j.mvc.annotation.ValField;
import com.cfuture08.eweb4j.mvc.annotation.ValMess;
import com.cfuture08.eweb4j.mvc.annotation.Validator;

public class ValidateControl {
	private String name;

	@Validator("required")
	@ValField("name")
	@ValMess("请输入name！")
	public void testVal(PrintWriter out) {
		out.print("your input name is : " + name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
