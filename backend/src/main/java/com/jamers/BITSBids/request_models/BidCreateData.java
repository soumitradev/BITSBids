package com.jamers.BITSBids.request_models;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
public record BidCreateData(@NotNull @Min(1) int price) {
}
