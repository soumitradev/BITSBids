package com.jamers.BITSBids.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.Map;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@RequiredArgsConstructor
class ChatWebSocketHandler extends TextWebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
	private final Map<String, WebSocketSession> sessions;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Principal principal = session.getPrincipal();
		if (principal == null || principal.getName() == null) {
			session.close(SERVER_ERROR.withReason("Unauthorised"));
			return;
		}

		sessions.put(principal.getName(), session);
		logger.info("Server connection {} from principal {} opened", session.getId(), principal.getName());

		TextMessage message = new TextMessage(String.format("Connected to server from principal %s with id %s", principal.getName(), session.getId()));
		logger.info("Server sends: {}", message);
		session.sendMessage(message);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,
	                                  @NotNull
	                                  CloseStatus status) {
		Principal principal = session.getPrincipal();
		if (principal == null || principal.getName() == null) {
			return;
		}

		logger.info("Server connection {} from principal {} closed with status {}", session.getId(), principal.getName(), status);
		sessions.remove(principal.getName());
	}

	@Override
	public void handleTransportError(
					@NotNull
					WebSocketSession session, Throwable exception) {
		logger.info("Server transport error: {}", exception.getMessage());
	}
}