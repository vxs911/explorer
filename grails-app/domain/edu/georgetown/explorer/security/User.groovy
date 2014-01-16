package edu.georgetown.explorer.security

class User {

	transient springSecurityService

	int id
	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	Date dateCreated
	Date lastUpdated
	Date lastLoginDate
	Date lastFailedLoginDate
	int failedLoginCount

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
		table 'USER'
		version false
		autoTimestamp true
		datasource 'vaers'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		//encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			//encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
