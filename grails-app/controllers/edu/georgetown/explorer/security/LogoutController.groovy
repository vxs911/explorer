package edu.georgetown.explorer.security
import grails.plugin.springsecurity.SpringSecurityUtils

class LogoutController {

	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	
	def index = {
		//flash.message = "You have successfully logged out"
		redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
	}
}