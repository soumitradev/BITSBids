package com.jamers.BITSBids.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class TestController {
	@GetMapping("/api/test")
	public Map<String, String> test() {
		return Collections.singletonMap("message", "Hello, World!");
	}
}
