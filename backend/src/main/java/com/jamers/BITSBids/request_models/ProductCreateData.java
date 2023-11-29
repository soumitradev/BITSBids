package com.jamers.BITSBids.request_models;

import jakarta.annotation.Nullable;

import javax.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import static com.jamers.BITSBids.common.Constants.MAX_MEDIA_SIZE;

public record ProductCreateData(@NotEmpty @NotBlank String name, @NotEmpty @NotBlank String description, @Size(
				min = 1,
				max = MAX_MEDIA_SIZE
) ArrayList<String> media, @Size(min = 1) ArrayList<String> category, @Min(1) int basePrice,
                                @Nullable Integer autoSellPrice, @NotNull ZonedDateTime closedAt) {
}
