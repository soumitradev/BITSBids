package com.jamers.BITSBids.request_models;

import jakarta.annotation.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public record ProductCreateData(@NotEmpty String name, @NotEmpty String description, @Size(
				min = 1,
				max = 8
) ArrayList<String> media, @Min(0) int basePrice, @Nullable Integer autoSellPrice, @NotNull ZonedDateTime closedAt) {
}
