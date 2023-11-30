package com.jamers.BITSBids.request_models;

import javax.validation.constraints.NotNull;

public record MessageReadData(@NotNull int messageId) {
}
