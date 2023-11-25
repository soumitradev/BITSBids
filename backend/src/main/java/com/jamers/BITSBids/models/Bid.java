package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Table("bids")
public record Bid(@Id int id, int productId, int bidderId, int price, ZonedDateTime placedAt) {
}
