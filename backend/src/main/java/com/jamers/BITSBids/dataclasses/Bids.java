package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

class Bids {
	@Id
	private int id;
	private int productId;
	private int bidderId;
	private int price;
	private ZonedDateTime placedAt;

	public int getId() {
		return id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getBidderId() {
		return bidderId;
	}

	public void setBidderId(int bidderId) {
		this.bidderId = bidderId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public ZonedDateTime getPlacedAt() {
		return placedAt;
	}

}
