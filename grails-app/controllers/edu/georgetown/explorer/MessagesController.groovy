package edu.georgetown.explorer

import grails.converters.JSON
import javax.servlet.http.HttpServletResponse;

class MessagesController {
	
	def query() {
		Messages messages = session["messages"];
		render messages.getAllMessages() as JSON;
	}
	
	def acknowledge() {
		Messages messages = session["messages"];
		messages.markAsRead(params.int("message_id", -1));
		render (status:HttpServletResponse.SC_OK);
	}

}