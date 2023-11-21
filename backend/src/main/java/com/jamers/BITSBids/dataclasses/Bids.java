package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

class Bids {
	@Id
	private int id;
	private int product_id;
	private int bidder_id;
	private int price;
	private ZonedDateTime placed_at;

	public int getId() {
		return id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getBidder_id() {
		return bidder_id;
	}

	public void setBidder_id(int bidder_id) {
		this.bidder_id = bidder_id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public ZonedDateTime getPlaced_at() {
		return placed_at;
	}

}