package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Message;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MessageRepository extends ReactiveCrudRepository<Message, String>, ReactiveQueryByExampleExecutor<Message> {
}
