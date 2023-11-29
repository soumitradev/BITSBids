package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Category;
import com.jamers.BITSBids.models.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CategoryRepository extends ReactiveCrudRepository<Category, String>,
				ReactiveQueryByExampleExecutor<Category> {
	@Query("SELECT DISTINCT * FROM bitsbids.categories")
	Flux<Category> listCategories();

	@Query(
					"SELECT DISTINCT * FROM bitsbids.products p JOIN bitsbids.category_products pc ON pc.product_id = p.id where" +
									" pc.category_id = :id "
	)
	Flux<Product> listProductByCategory(int id);

	@Query("SELECT * FROM bitsbids.categories WHERE name = :name")
	Flux<Category> getCategoryByName(String name);

	@Query(
					"SELECT DISTINCT c.name FROM bitsbids.categories c JOIN bitsbids.category_products pc ON pc.category_id= c" +
									".id " +
									"WHERE pc" +
									".product_id = :id "
	)
	Flux<String> listProductCategories(int id);
}
