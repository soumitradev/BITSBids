package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.ArrayList;

@Table("products")
public record Product(@Id Integer id, String name, String description, int sellerId, ArrayList<String> media,
                      int basePrice, Integer autoSellPrice, int price, boolean sold, ZonedDateTime createdAt,
                      ZonedDateTime closedAt, Integer currentBidId) {
}
