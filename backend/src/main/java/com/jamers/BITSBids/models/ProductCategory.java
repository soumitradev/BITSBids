package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("category_products")
public record ProductCategory(@Id Integer id, int productId, int categoryId) {
}
