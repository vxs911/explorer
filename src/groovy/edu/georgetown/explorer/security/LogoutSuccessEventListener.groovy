package edu.georgetown.explorer.security

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rosuda.REngine.Rserve.RConnection
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AbstractAuthenticationEvent
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

class LogoutSuccessEventListener implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest arg0, HttpServletResponse arg1,
			Authentication arg2) {
		RConnection conn = (RConnection) arg0.getSession().getAttribute("rConnection");
		if(conn) {
			log.debug "R Connection closed: "+conn.close();
		}	
	}

}