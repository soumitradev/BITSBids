package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Bid;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BidRepository extends ReactiveCrudRepository<Bid, String>, ReactiveQueryByExampleExecutor<Bid> {
	@Query("SELECT * FROM bitsbids.bids b WHERE b.product_id = :id ORDER BY b.placed_at DESC LIMIT 10")
	Flux<Bid> getLastTenBids(int id);
}
