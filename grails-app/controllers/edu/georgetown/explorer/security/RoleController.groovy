package edu.georgetown.explorer.security
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class RoleController extends grails.plugin.springsecurity.ui.RoleController {
	
	def save() {
		def role = new Role(params)
		role.dateCreated = new Date()
		role.lastUpdated = new Date();
		
		if(role.save(flush:true)) {
			redirect(controller:'role', action:'roleSearch')
		}
	}
}