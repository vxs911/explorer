package edu.georgetown.explorer.security

class User {

	//transient springSecurityService

	int id
	String username
	String password
	boolean enabled = true
	boolean accountExpired = false
	boolean accountLocked = false
	boolean passwordExpired = false
	Date dateCreated
	Date lastUpdated
	Date lastLoginDate
	Date lastFailedLoginDate
	int failedLoginCount = 0

	static constraints = {
		username blank: false, unique: true
		password blank: false
		lastLoginDate nullable: true
		lastFailedLoginDate nullable: true
	}

	static mapping = {
		password column: '`password`'
		table 'USER'
		version false
		autoTimestamp true
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
		//password = springSecurityService.encodePassword(password)
	}
}
