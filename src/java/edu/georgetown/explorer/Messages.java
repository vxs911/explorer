package edu.georgetown.explorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

public class Messages {

	public static final String MESSAGE_READ = "read";
	public static final String MESSAGE_UNREAD = "unread";
	
	private List<Map<String, String>> messages = new ArrayList<Map<String, String>>();
	
	public Map<String, String> getMessage(String messageId) {
		return this.messages.get(NumberUtils.toInt(messageId));
	}
	
	public List<Map<String, String>> getAllMessages() {
		return this.messages;
	}
	
	public int addMessage(String message) {
		Map<String, String> msg = new HashMap<String, String>();
		msg.put("message_id", (this.messages.size() - 1) + "");
		msg.put("content", message);
		msg.put("status", MESSAGE_UNREAD);
		this.messages.add(msg);
		return this.messages.size() - 1;
	}
	
	public void markAsRead(String messageId) {
		Map<String, String> message = getMessage(messageId);
		message.put("status", MESSAGE_READ);
	}
	
	public void markAsRead(int messageId) {
		Map<String, String> message = this.messages.get(messageId);
		message.put("status", MESSAGE_READ);
	}
	
}