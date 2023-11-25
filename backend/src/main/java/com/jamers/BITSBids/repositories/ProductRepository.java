package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Product;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<Product, String>, ReactiveQueryByExampleExecutor<Product> {
}
