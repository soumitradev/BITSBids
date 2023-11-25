package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<User, String>, ReactiveQueryByExampleExecutor<User> {
	@Query("SELECT DISTINCT * FROM bitsbids.users WHERE email = :email")
	Flux<User> findByEmail(String email);
}
