package com.jamers.BITSBids.dataclasses;

import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;
import java.util.ArrayList;

class Messages {
	@Id
	private int id;
	private int conversation_id;
	private boolean from_buyer;
	private String text;
	private ArrayList<String> media;
	private ZonedDateTime sent_at;

	public int getId() {
		return id;
	}

	public int getConversation_id() {
		return conversation_id;
	}

	public void setConversation_id(int conversation_id) {
		this.conversation_id = conversation_id;
	}

	public boolean isFrom_buyer() {
		return from_buyer;
	}

	public void setFrom_buyer(boolean from_buyer) {
		this.from_buyer = from_buyer;
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

	public ZonedDateTime getSent_at() {
		return sent_at;
	}

}