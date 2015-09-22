package edu.georgetown.explorer.security;

import org.rosuda.REngine.Rserve.RConnection
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import grails.plugin.springsecurity.web.SecurityRequestHolder;
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import edu.georgetown.explorer.security.User
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class LoginSuccessEventListener implements ApplicationContextAware, ApplicationListener<InteractiveAuthenticationSuccessEvent> {
	
	private ApplicationContext ctx;

	void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		User.withTransaction {
			Authentication auth = event.getAuthentication()
			if(auth.isAuthenticated()) {
				User user = User.get(auth.getPrincipal().id)
				user.lastLoginDate = new java.util.Date()
				user.lastFailedLoginDate = user.lastFailedLoginDate ?: new java.sql.Timestamp(0)
				user.failedLoginCount = 0
				user.save(flush: true)
			}
		}
		log.debug "Inside LoginSuccessEventListener"
		def grailsApplication = ctx.getBean('grailsApplication')
		
		RConnection re = new RConnection(grailsApplication.config.r.host, Integer.parseInt(grailsApplication.config.r.port));
		re.eval("library(survival)");
		SecurityRequestHolder.request.session.setAttribute("rConnection", re);
	}	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
				ctx = applicationContext		
	}
}