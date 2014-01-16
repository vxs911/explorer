package edu.georgetown.explorer.security

import grails.plugins.springsecurity.Secured
import grails.converters.JSON
import grails.util.GrailsNameUtils

import org.codehaus.groovy.grails.plugins.springsecurity.NullSaltSource
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.dao.DataIntegrityViolationException

@Secured(['ROLE_ADMIN'])
class UserController extends grails.plugins.springsecurity.ui.UserController {
	def springSecurityService, springSecurityUiService
	
	
	def edit() {
		super.edit()
	}
	
	@Secured(['ROLE_USER'])
	def changePassword() {
		String usernameFieldName = springSecurityService.principal.username
		log.debug("username is: "+usernameFieldName)
		
			def user = lookupUserClass().findByUsername(usernameFieldName)
			log.debug("user is: "+user)
			render view:'changePassword', model: [user:user]
			[user:user]
	}
	
	@Secured(['ROLE_USER'])
	def updatePassword() {
		String username = springSecurityService.principal.username
		log.debug("username is: "+username)
		String salt = saltSource instanceof NullSaltSource ? null : username
		
		def password = params.password
		
		def confirm = params.confirm
		
		if(password != confirm) {
			flash.message = "Password and confirm password didn't match"
			redirect action: 'changePassword'
			return
		}
		
		def oldPassword = springSecurityUiService.encodePassword(params.oldPassword, salt)
		
		def user = lookupUserClass().findByUsernameAndPassword(username, oldPassword)
		
		if(!user) {
			flash.message = "Please enter your current password correctly"
			redirect action: 'changePassword'
		}
		
		else {		
			user.password = springSecurityUiService.encodePassword(password, salt)
			if(!user.save(flush: true)) {
				flash.message = "Password update failed"
			}
			
			else {
				flash.message = "Password successfully updated"
			}
			
			redirect action: 'changePassword'
		}
		

		
	}
	
}
