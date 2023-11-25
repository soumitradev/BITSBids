package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;
import java.util.ArrayList;

class Messages {
	@Id
	private int id;
	private int conversationId;
	private boolean fromBuyer;
	private String text;
	private ArrayList<String> media;
	private ZonedDateTime sentAt;

	public int getId() {
		return id;
	}

	public int getConversationId() {
		return conversationId;
	}

	public void setConversationId(int conversationId) {
		this.conversationId = conversationId;
	}

	public boolean isFromBuyer() {
		return fromBuyer;
	}

	public void setFromBuyer(boolean fromBuyer) {
		this.fromBuyer = fromBuyer;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<String> getMedia() {
		return media;
	}

	public void addMedia(String media) {
		this.media.add(media);
	}

	public ZonedDateTime getSentAt() {
		return sentAt;
	}

}
