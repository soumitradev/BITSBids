package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("categories")
public record Category(@Id int id, String name) {
}
