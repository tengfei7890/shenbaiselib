package test.po;

import java.util.ArrayList;
import java.util.List;

import com.cfuture08.eweb4j.orm.config.annotation.Column;
import com.cfuture08.eweb4j.orm.config.annotation.Id;
import com.cfuture08.eweb4j.orm.config.annotation.Many;
import com.cfuture08.eweb4j.orm.config.annotation.Table;

@Table("t_master")
public class Master {
	@Id
	@Column
	private Integer id = 0;
	@Column
	private String name;
	@Column
	private String gender;
//	@ManyMany(target = Pet.class, relTable = "t_masterpetrel", from = "masterId", to = "petId")
	@Many(target=Pet.class,column="masterId")
	private List<Pet> pets = new ArrayList<Pet>();

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void addPet(Pet pet) {
		this.pets.add(pet);
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public String toString() {
		return "[Master id:" + this.id + ",name:" + this.name + ",gender:"
				+ this.gender + "]";
	}

}
