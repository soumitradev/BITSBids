package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Bid;
import com.jamers.BITSBids.models.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BidRepository extends ReactiveCrudRepository<Bid, String>, ReactiveQueryByExampleExecutor<Bid> {
	@Query("SELECT product.* FROM bitsbids.bids AS bid INNER JOIN bitsbids.products AS product ON bid.bidder_id = :id AND product.sold = false AND bid.product_id = product.id INNER JOIN (SELECT bid.product_id, MAX(bid.price) AS maxPrice FROM bitsbids.bids AS bid GROUP BY bid.product_id) AS maxBid ON bid.product_id = maxBid.product_id AND bid.price = maxBid.maxPrice")
	Flux<Product> findActiveBidProductsByUserId(int id);

	@Query("SELECT bid.* FROM bitsbids.bids AS bid INNER JOIN bitsbids.products AS product ON bid.bidder_id = :id AND product.sold = false AND bid.product_id = product.id INNER JOIN (SELECT bid.product_id, MAX(bid.price) AS maxPrice FROM bitsbids.bids AS bid GROUP BY bid.product_id) AS maxBid ON bid.product_id = maxBid.product_id AND bid.price = maxBid.maxPrice")
	Flux<Bid> findActiveBidsByUserId(int id);

	@Query("SELECT product.* FROM bitsbids.bids AS bid INNER JOIN bitsbids.products AS product ON bid.bidder_id = :id AND product.sold = true AND bid.product_id = product.id INNER JOIN (SELECT bid.product_id, MAX(bid.price) AS maxPrice FROM bitsbids.bids AS bid GROUP BY bid.product_id) AS maxBid ON bid.product_id = maxBid.product_id AND bid.price = maxBid.maxPrice")
	Flux<Product> findInactiveBidProductsByUserId(int id);

	@Query("SELECT product.* FROM bitsbids.bids AS bid INNER JOIN bitsbids.products AS product ON bid.bidder_id = :id AND product.sold = true AND bid.product_id = product.id INNER JOIN (SELECT bid.product_id, MAX(bid.price) AS maxPrice FROM bitsbids.bids AS bid GROUP BY bid.product_id) AS maxBid ON bid.product_id = maxBid.product_id AND bid.price = maxBid.maxPrice")
	Flux<Bid> findInactiveBidsByUserId(int id);
}
