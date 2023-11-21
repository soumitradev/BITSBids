package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;
import java.util.ArrayList;

class Products {
	@Id
	private int id;
	private String name;
	private String description;
	private int sellerId;
	private ArrayList<String> media;
	private int basePrice;
	private int autoSellPrice;
	private int price;
	private boolean sold;
	private ZonedDateTime createdAt;
	private ZonedDateTime closedAt;
	private int currentBidId;

	public ZonedDateTime getClosedAt() {
		return closedAt;
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

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public ArrayList<String> getMedia() {
		return media;
	}

	public void addMedia(String media) {
		this.media.add(media);
	}

	public int getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}

	public int getAutoSellPrice() {
		return autoSellPrice;
	}

	public void setAutoSellPrice(int autoSellPrice) {
		this.autoSellPrice = autoSellPrice;
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

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}


	public int getCurrentBidId() {
		return currentBidId;
	}
}
