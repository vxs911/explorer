package edu.georgetown.explorer.security

import java.util.Date;

class Role {

	int id
	String authority
	Date dateCreated
	Date lastUpdated

	static mapping = {
		cache true
		version false
		table 'ROLE'
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
