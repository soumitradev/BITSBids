package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;
import java.util.ArrayList;

class Products {
	@Id
	private int id;
	private String name;
	private String description;
	private int seller_id;
	private ArrayList<String> media;
	private int base_price;
	private int auto_sell_price;
	private int price;
	private boolean sold;
	private ZonedDateTime created_at;
	private ZonedDateTime closed_at;
	private int current_bid_id;

	public ZonedDateTime getClosed_at() {
		return closed_at;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public ArrayList<String> getMedia() {
		return media;
	}

	public void addMedia(String media) {
		this.media.add(media);
	}

	public int getBase_price() {
		return base_price;
	}

	public void setBase_price(int base_price) {
		this.base_price = base_price;
	}

	public int getAuto_sell_price() {
		return auto_sell_price;
	}

	public void setAuto_sell_price(int auto_sell_price) {
		this.auto_sell_price = auto_sell_price;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public ZonedDateTime getCreated_at() {
		return created_at;
	}


	public int getCurrent_bid_id() {
		return current_bid_id;
	}
}