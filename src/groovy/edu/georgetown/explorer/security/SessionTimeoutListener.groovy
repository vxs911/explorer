package edu.georgetown.explorer.security

import org.rosuda.REngine.Rserve.RConnection
import org.springframework.context.ApplicationListener
import org.springframework.security.web.session.HttpSessionDestroyedEvent

class SessionTimeoutListener implements ApplicationListener<HttpSessionDestroyedEvent> {

	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent arg0) {
		log.debug "inside session timeout"
		RConnection conn = (RConnection) arg0.getSession().getAttribute("rConnection");
		if(conn) {
			log.debug "R Connection closed: "+conn.close();
		}		
	}

}