package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

class Conversations {
	@Id
	private int id;
	private int product_id;
	private int buyer_id;
	private int last_read_by_seller_id;
	private int last_read_by_buyer_id;

	public int getId() {
		return id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(int buyer_id) {
		this.buyer_id = buyer_id;
	}

	public int getLast_read_by_seller_id() {
		return last_read_by_seller_id;
	}

	public void setLast_read_by_seller_id(int last_read_by_seller_id) {
		this.last_read_by_seller_id = last_read_by_seller_id;
	}

	public int getLast_read_by_buyer_id() {
		return last_read_by_buyer_id;
	}

	public void setLast_read_by_buyer_id(int last_read_by_buyer_id) {
		this.last_read_by_buyer_id = last_read_by_buyer_id;
	}
}
