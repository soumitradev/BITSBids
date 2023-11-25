package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.User;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, String>, ReactiveQueryByExampleExecutor<User> {
}
