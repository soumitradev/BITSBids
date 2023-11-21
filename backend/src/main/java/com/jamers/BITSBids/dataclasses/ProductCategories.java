package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

class ProductCategories {
	@Id
	private int id;
	private int productId;
	private int categoryId;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getId() {
		return id;
	}

}
