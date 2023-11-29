package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.ProductCategory;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductCategoryRepository extends ReactiveCrudRepository<ProductCategory, String>, ReactiveQueryByExampleExecutor<ProductCategory> {
}
