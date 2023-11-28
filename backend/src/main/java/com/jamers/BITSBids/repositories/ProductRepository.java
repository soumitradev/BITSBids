package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

public interface ProductRepository extends ReactiveCrudRepository<Product, String>, ReactiveQueryByExampleExecutor<Product> {
	@Query("SELECT * FROM bitsbids.products WHERE seller_id = :id AND sold = true")
	Flux<ArrayList<Product>> findSoldProductsById(int id);

	@Query("SELECT * FROM bitsbids.products WHERE seller_id = :id AND sold = false")
	Flux<ArrayList<Product>> findActiveProductsById(int id);
}
