package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

class Conversations {
	@Id
	private int id;
	private int productId;
	private int buyerId;
	private int lastReadBySellerId;
	private int lastReadByBuyerId;

	public int getId() {
		return id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}

	public int getLastReadBySellerId() {
		return lastReadBySellerId;
	}

	public void setLastReadBySellerId(int lastReadBySellerId) {
		this.lastReadBySellerId = lastReadBySellerId;
	}

	public int getLastReadByBuyerId() {
		return lastReadByBuyerId;
	}

	public void setLastReadByBuyerId(int lastReadByBuyerId) {
		this.lastReadByBuyerId = lastReadByBuyerId;
	}
}
