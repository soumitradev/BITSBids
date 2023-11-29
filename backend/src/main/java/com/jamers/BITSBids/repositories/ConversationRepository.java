package com.jamers.BITSBids.repositories;

import com.jamers.BITSBids.models.Conversation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ConversationRepository extends ReactiveCrudRepository<Conversation, String>,
				ReactiveQueryByExampleExecutor<Conversation> {
	@Query("SELECT * FROM bitsbids.conversations c WHERE :id IN (c.buyer_id,c.seller_id)")
	Flux<Conversation> findUserConversations(int id);
}
