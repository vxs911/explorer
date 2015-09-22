package edu.georgetown.explorer

import grails.transaction.Transactional
import javax.servlet.http.HttpSession;

@Transactional
class MessagesService {

    def create(HttpSession session) {
		Map message = ["available":"yes", "ready":"no", "acknowledged":"no"];
	}
}