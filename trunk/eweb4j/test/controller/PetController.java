package test.controller;

import java.io.PrintWriter;

import test.po.Pet;
import test.service.TestService;

import com.cfuture08.eweb4j.mvc.annotation.Controller;
import com.cfuture08.eweb4j.mvc.annotation.RequestMapping;
import com.cfuture08.eweb4j.mvc.annotation.Singleton;
@Singleton
@Controller
@RequestMapping("pets")
public class PetController {
	private final TestService service = new TestService();
	
	@RequestMapping("/testTrans")
	public String testTrans(Pet pet,PrintWriter out){
		service.testTrans(pet);
		out.print("pet->"+pet);
		return null;
	}

//	@RequestMapping("/")
//	@Result(location = { "showAll.jsp" }, name = { "success" }, type = { "" })
//	public String getAll(HttpServletRequest request) throws DAOException {
//		List<Pet> pets = this.service.getAll();
//		request.setAttribute("pets", pets);
//		return "success";
//	}
//
//	@RequestMapping("/分页")
//	@Result(location = { "showAll.jsp" }, name = { "success" }, type = { "" })
//	public String getList(HttpServletRequest request, @Param("p") int pageNum,
//			@Param("n") int numPerPage) throws DAOException {
//		List<Pet> pets = this.service.getPage(pageNum, numPerPage);
//		if (pets != null && pets.size() > 0) {
//			DivPageComp dpc = new DivPageComp(pageNum, numPerPage,
//					this.service.countAll(), 4);
//			dpc.setLocation(String.format("分页?p={pageNum}&n=%s", numPerPage));
//			dpc.doWork();
//			request.setAttribute("pets", pets);
//			request.setAttribute("dpc", dpc);
//		}
//		return "success";
//	}
//
//	@RequestMapping("/{id}")
//	@Result(location = { "seeDetail.jsp" }, name = { "success" }, type = { "" })
//	public String get(HttpServletRequest request, PrintWriter out,
//			@Param("id") Integer id, @Param("ext") String ext) throws DAOException {
//		Pet pet = this.service.getOne(id);
//		if ("json".equalsIgnoreCase(ext)) {
//			out.print("<pre>" + JsonConverter.convert(pet) + "</pre>");
//			return "ajax";
//		}
//
//		request.setAttribute("pet", pet);
//		return "success";
//	}
//
//	@RequestMapping(value="/{id}",method="DELETE")
//	@Result(location = { "分页?p=1&n=10" }, name = { "success" }, type = { "redirect" })
//	public String deleteOne(PrintWriter out, @Param("id") Integer id) throws DAOException {
//		String error = this.service.delete(new Integer[] { id });
//		if (error == null) {
//			return "success";
//		} else {
//			out.print(String.format(
//					"<script>alert('%s');history.go(-1)</script>", error));
//			return "ajax";
//		}
//
//	}
//
//	@RequestMapping(value="/{id}",method="PUT")
//	@Result(location = { "分页?p=1&n=10" }, name = { "success" }, type = { "redirect" })
//	@Validator(value = { "requried", "int", "int", "length" }, clazz = {})
//	@ValField(name = { "id", "id", "age", "type" }, index = { 0, 0, 2,
//			3 }, message = { "请选择要修改的记录", "id必须是数字", "年龄必须是数字", "类型长度不能超过10" })
//	@FieldParam(index = { 3 }, name = { "maxLength" }, value = { "10" })
//	public String put(PrintWriter out, Pet aPet) throws DAOException {
//		String error = this.service.update(aPet);
//		if (error == null) {
//			return "success";
//		} else {
//			out.print(String.format(
//					"<script>alert('%s');history.go(-1)</script>", error));
//		}
//		return "ajax";
//	}
//
//	@RequestMapping("/{id}/编辑")
//	@Result(location = { "../edit.jsp" }, name = { "success" })
//	public String edit(HttpServletRequest request, @Param("id") Integer id) throws DAOException {
//		Pet tPet = this.service.getOne(id);
//		request.setAttribute("pet", tPet);
//		return "success";
//	}
//
//	@RequestMapping("/添加")
//	@Result(location = { "add.jsp" }, name = { "success" })
//	public String addPet() {
//		return "success";
//	}
//
//	@RequestMapping(value="/",method="POST")
//	@Result(location = { "分页?p=1&n=10" }, name = { "success" }, type = { "redirect" })
//	@Validator(value = { "int", "length" })
//	@ValField(index = { 0, 0 }, message = { "年龄必须是数字", "类型长度不能超过10" }, name = {
//			"age", "type" })
//	@FieldParam(index = { 1 }, name = { "maxLength" }, value = { "10" })
//	public String post(PrintWriter out, Pet aPet) throws DAOException {
//		String error = this.service.create(aPet);
//		if (error == null) {
//			return "success";
//		} else {
//			out.print(String.format(
//					"<script>alert('%s');history.go(-1)</script>", error));
//		}
//		return "ajax";
//	}

}
