package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Message;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveCrudRepository<Message, String>, ReactiveQueryByExampleExecutor<Message> {
	@Query("SELECT DISTINCT * FROM bitsbids.messages WHERE conversation_id = :conversationId ORDER BY sent_at ASC")
	Flux<Message> findByConversationId(int conversationId);
}
