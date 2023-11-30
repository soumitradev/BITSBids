package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("conversations")
public record Conversation(@Id Integer id, int sellerId, int buyerId, int productId, Integer lastReadBySellerId,
                           Integer lastReadByBuyerId) {
}
