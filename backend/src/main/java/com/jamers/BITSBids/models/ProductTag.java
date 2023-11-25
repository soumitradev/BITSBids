package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("product_tags")
public record ProductTag(@Id int id, int productId, int tagId) {
}
