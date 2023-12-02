package com.jamers.BITSBids.response_models;

import java.math.BigInteger;

public record UserDetails(String name, String email, BigInteger phoneNumber, String room, int batch) {
}
