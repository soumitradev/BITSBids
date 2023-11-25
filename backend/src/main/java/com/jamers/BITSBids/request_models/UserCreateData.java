package com.jamers.BITSBids.request_models;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;

public record UserCreateData(@NotEmpty String room, @Digits(
				integer = 10,
				fraction = 0
) BigInteger phoneNumber) {
}
