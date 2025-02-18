package model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class CategoryBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	// Default constructor
	public CategoryBean() {
	}

	// Constructor with parameters
	public CategoryBean(int id, String name, Timestamp createdAt, Timestamp updatedAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "CategoryBean{id=" + id + ", name='" + name + "', createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ "}";
	}
}
