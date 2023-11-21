package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

class ProductCategories {
	@Id
	private int id;
	private int product_id;
	private int category_id;

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getId() {
		return id;
	}

}