package com.jamers.BITSBids.response_models;

import com.jamers.BITSBids.models.Bid;
import com.jamers.BITSBids.models.Product;

import java.util.List;

public record CategorizedProduct(Product product, List<String>
				Categories, List<Bid> Bids
) {
}
