package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

class ProductTags {
	@Id
	private int id;
	private int productId;
	private int tagId;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public int getId() {
		return id;
	}

}
