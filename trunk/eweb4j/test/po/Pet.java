package test.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cfuture08.eweb4j.orm.config.annotation.ManyMany;
import com.cfuture08.eweb4j.orm.config.annotation.One;
import com.cfuture08.eweb4j.orm.config.annotation.Table;

/**
 * just a po
 * 约定：
 * 1类名以PO、POJO、Entity、Model结尾，取其前面的单词作为数据库表名。
 * 例如类名PetPO，那么映射到的数据表名为Pet
 * 2属性名默认映射到数据表字段名。
 * 但是要注意，被@ManyMany、@Many、@One其中一个注解并且没有@Column注解的属性，
 * 是不会映射到数据库表字段中去的。
 * 3.ID属性，类属性名为id（忽略大小写）自动认为它是数据表的自增长字段并且是主键。
 * 
 * 上一个版本中的pojo中，
 * 类名上面会有@Table注解，
 * 属性名上面会有@Column注解
 * id属性上会有@Id注解。
 * 代码看起来会很不简洁。
 * 很明显，这个版本看起来十分简洁。
 * 
 * @author weiwei
 * 
 */
@Table("t_pet")
public class Pet {
	private Integer id = 0;
	private String name;
	private String type;
	private Date birthday;
	private int age;
	private Date createTime;
	@One(column = "masterId")
	private Master master;
	@ManyMany(target = Master.class, relTable = "t_master_pet", from = "petId", to = "masterId")
	private List<Master> masters = new ArrayList<Master>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Master getMaster() {
		return master;
	}

	public void setMaster(Master master) {
		this.master = master;
	}

	public List<Master> getMasters() {
		return masters;
	}

	public void setMasters(List<Master> masters) {
		this.masters = masters;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String toString() {
		return "[Pet id:" + this.id + ", name:" + this.name + ", age:"
				+ this.age + ", type:" + this.type + ", birthday:"
				+ this.birthday + ", createTime:" + this.createTime + "]\n";
	}
}
