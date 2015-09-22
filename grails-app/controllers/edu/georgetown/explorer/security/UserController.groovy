package edu.georgetown.explorer.security

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON
import grails.util.GrailsNameUtils

import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.dao.DataIntegrityViolationException

@Secured(['ROLE_ADMIN'])
class UserController extends grails.plugin.springsecurity.ui.UserController {
	def springSecurityService, springSecurityUiService
	def saltSource
	
	
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
	
	def userSearch() {
		
						boolean useOffset = params.containsKey('offset')
						setIfMissing 'max', 10, 100
						setIfMissing 'offset', 0
		
						def hql = new StringBuilder('FROM ').append(lookupUserClassName()).append(' u WHERE 1=1 ')
						def queryParams = [:]
		
						def userLookup = SpringSecurityUtils.securityConfig.userLookup
						String usernameFieldName = userLookup.usernamePropertyName
		
						for (name in [username: usernameFieldName]) {
								if (params[name.key]) {
										hql.append " AND LOWER(u.${name.value}) LIKE :${name.key}"
										queryParams[name.key] = params[name.key].toLowerCase() + '%'
								}
						}
		
						String enabledPropertyName = userLookup.enabledPropertyName
						String accountExpiredPropertyName = userLookup.accountExpiredPropertyName
						String accountLockedPropertyName = userLookup.accountLockedPropertyName
						String passwordExpiredPropertyName = userLookup.passwordExpiredPropertyName
		
						for (name in [enabled: enabledPropertyName,
									  accountExpired: accountExpiredPropertyName,
									  accountLocked: accountLockedPropertyName,
									  passwordExpired: passwordExpiredPropertyName]) {
								Integer value = params.int(name.key)
								if (value) {
										hql.append " AND u.${name.value}=:${name.key}"
										queryParams[name.key] = value == 1
								}
						}
		
						int totalCount = User.executeQuery("SELECT COUNT(DISTINCT u) $hql", queryParams)[0]
		
						Integer max = params.int('max')
						Integer offset = params.int('offset')
		
						String orderBy = ''
						if (params.sort) {
								orderBy = " ORDER BY u.$params.sort ${params.order ?: 'ASC'}"
						}
		
						def results = User.executeQuery(
										"SELECT DISTINCT u $hql $orderBy",
										queryParams, [max: max, offset: offset])
						def model = [results: results, totalCount: totalCount, searched: true]
		
						// add query params to model for paging
						for (name in ['username', 'enabled', 'accountExpired', 'accountLocked',
									  'passwordExpired', 'sort', 'order']) {
								 model[name] = params[name]
						}
		
						render view: 'search', model: model
				}
	
	def createNewAdminUser() {
		User user = new User();
		user.username = "me";
		user.password = "password";
		user.enabled = true;
		user.dateCreated = new Date();
		user.lastUpdated = new Date();
		
		
		String salt = saltSource instanceof NullSaltSource ? null : user.username
		user.password = springSecurityUiService.encodePassword(user.password, salt)
		
		
		user.save(flush: true);
		render "User Created"
	}
	
	
	def update() {
		String passwordFieldName = SpringSecurityUtils.securityConfig.userLookup.passwordPropertyName

		def user = findById()
		if (!user) return
		if (!versionCheck('user.label', 'User', user, [user: user])) {
				return
		}

		def oldPassword = user."$passwordFieldName"
		user.properties = params
		if (params.password && !params.password.equals(oldPassword)) {
				String salt = saltSource instanceof NullSaltSource ? null : params.username
				user."$passwordFieldName" = springSecurityUiService.encodePassword(params.password, salt)
		}
		log.debug("before save")
		if (!user.save()) {
			user.errors.each {
				log.debug it
			}
			log.debug("didn't save")
				render view: 'edit', model: buildUserModel(user)
				return
		}
		log.debug("saved");
		String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName

		lookupUserRoleClass().removeAll user
		addRoles user
		userCache.removeUserFromCache user[usernameFieldName]
		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.id])}"
		redirect action: 'edit', id: user.id
}
	
}
