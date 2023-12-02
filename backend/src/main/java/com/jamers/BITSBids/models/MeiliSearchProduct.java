package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("products")
public record MeiliSearchProduct(@Id Integer id, String name, String description) {
}
