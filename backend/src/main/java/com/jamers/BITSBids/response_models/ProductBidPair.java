package com.jamers.BITSBids.response_models;

import com.jamers.BITSBids.models.Bid;
import com.jamers.BITSBids.models.Product;

import java.util.ArrayList;
import java.util.List;

public record ProductBidPair(Product product, Bid bid) {
	public static List<ProductBidPair> zip(List<Product> products, List<Bid> bids) {
		ArrayList<ProductBidPair> pairs = new ArrayList<>();
		for (int i = 0; i < products.size(); i++) {
			pairs.add(new ProductBidPair(products.get(i), bids.get(i)));
		}
		return pairs;
	}
}
