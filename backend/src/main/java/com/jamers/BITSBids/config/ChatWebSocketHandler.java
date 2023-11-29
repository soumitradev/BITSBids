package com.jamers.BITSBids.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@RequiredArgsConstructor
class ChatWebSocketHandler extends TextWebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
	private final Map<String, WebSocketSession> sessions;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		OAuth2AuthenticationToken principal = ((OAuth2AuthenticationToken) session.getPrincipal());
		if (principal == null || principal.getName() == null) {
			session.close(SERVER_ERROR.withReason("Unauthorised"));
			return;
		}

		// TODO: think about multiple sessions per user
		String email = principal.getPrincipal().getAttribute("email");
		sessions.put(email, session);
		logger.info("Server connection {} from principal {} opened", session.getId(), principal.getPrincipal().getAttribute("email"));

		TextMessage message = new TextMessage(String.format("Connected to server from principal %s with id %s", principal.getName(), session.getId()));
		logger.info("Server sends: {}", message);
		session.sendMessage(message);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		logger.info("Server received: {}", message);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,
	                                  @NotNull
	                                  CloseStatus status) {
		OAuth2AuthenticationToken principal = ((OAuth2AuthenticationToken) session.getPrincipal());
		if (principal == null || principal.getName() == null) {
			return;
		}
		String email = principal.getPrincipal().getAttribute("email");
		logger.info("Server connection {} from principal {} closed with status {}", session.getId(), principal.getName(), status);
		sessions.remove(email);
	}

	@Override
	public void handleTransportError(
					@NotNull
					WebSocketSession session, Throwable exception) {
		logger.info("Server transport error: {}", exception.getMessage());
	}
}