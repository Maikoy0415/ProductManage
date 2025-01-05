package model.entity;

import java.io.Serializable;

public class CategoryBean implements Serializable {

	private String name;

	public CategoryBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String[] args) {
		CategoryBean categoryBean = new CategoryBean();
		categoryBean.setName("Car Brand");
		System.out.println(categoryBean.getName());
	}
}
