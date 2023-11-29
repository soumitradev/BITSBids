package com.jamers.BITSBids.request_models;

import java.util.ArrayList;

public record MessageCreateData(String text, ArrayList<String> media) {
}
