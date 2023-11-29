package com.jamers.BITSBids.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.ArrayList;

@Table("messages")
public record Message(@Id Integer id, int conversationId, boolean fromBuyer, String text, ArrayList<String> media,
                      ZonedDateTime sentAt) {
}
