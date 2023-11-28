package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Bid;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BidRepository extends ReactiveCrudRepository<Bid, String>, ReactiveQueryByExampleExecutor<Bid> {
}
