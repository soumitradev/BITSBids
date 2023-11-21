package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

class Categories {
	@Id
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}