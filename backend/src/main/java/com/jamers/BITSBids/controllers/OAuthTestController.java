package com.jamers.BITSBids.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class OAuthTestController {
	@GetMapping("/api/auth/test")
	public Map<String, String> test(
					@AuthenticationPrincipal
					OAuth2User principal) {
		return Collections.singletonMap("name", "Hello, " + principal.getAttribute("name") + "!");
	}
}
