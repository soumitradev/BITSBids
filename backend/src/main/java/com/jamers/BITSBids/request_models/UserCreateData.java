package com.jamers.BITSBids.request_models;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;

public record UserCreateData(@NotEmpty @NotBlank String room, @Digits(
				integer = 10,
				fraction = 0
) BigInteger phoneNumber) {
//	TODO: Check null phone number
}
