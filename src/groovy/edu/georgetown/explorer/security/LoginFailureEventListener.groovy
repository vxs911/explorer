package edu.georgetown.explorer.security

import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.core.Authentication
import edu.georgetown.explorer.security.User

class LoginFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
		User.withTransaction {
			Authentication auth = event.getAuthentication()
			if(!auth.isAuthenticated()) {
				User user = User.findByUsername(event.getAuthentication().getPrincipal()) // principal consists only of the username (String)
				if(user) {
					user.lastLoginDate = user.lastLoginDate?: new java.sql.Timestamp(0)
					user.lastFailedLoginDate = new java.util.Date()
					user.failedLoginCount += 1
					if(user.failedLoginCount == 3) {
						user.accountLocked = true
					}
					user.save(flush: true)
				}
			}
		}
	}
}