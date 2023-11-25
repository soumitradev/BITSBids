package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("conversations")
public record Conversation(@Id int id, int productId, int buyerId, int lastReadBySellerId, int lastReadByBuyerId) {
}
