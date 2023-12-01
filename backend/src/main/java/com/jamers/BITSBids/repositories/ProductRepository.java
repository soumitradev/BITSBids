package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveCrudRepository<Product, String>, ReactiveQueryByExampleExecutor<Product> {
	@Query("SELECT * FROM bitsbids.products WHERE seller_id = :id AND sold = true")
	Flux<Product> findSoldProductsById(int id);

	@Query("SELECT * FROM bitsbids.products WHERE seller_id = :id AND sold = false")
	Flux<Product> findActiveProductsById(int id);

	@Query("SELECT * FROM bitsbids.products ORDER BY created_at DESC LIMIT 20")
	Flux<Product> findLatestProducts();
}
