package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;

@Table("users")
public record User(@Id Integer id, String email, String name, int batch, String room, BigInteger phoneNumber,
                   float balance) {
}
