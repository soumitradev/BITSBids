package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

class ProductTags {
	@Id
	private int id;
	private int product_id;
	private int tag_id;

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public int getId() {
		return id;
	}

}