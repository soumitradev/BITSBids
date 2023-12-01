package com.jamers.BITSBids.response_models;

import com.jamers.BITSBids.models.Bid;
import com.jamers.BITSBids.models.Category;
import com.jamers.BITSBids.models.Product;

import java.util.List;

public record CategorizedProduct(Product product, List<Category> Categories, List<Bid> Bids) {
}
