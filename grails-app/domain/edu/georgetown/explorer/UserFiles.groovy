package edu.georgetown.explorer

import edu.georgetown.explorer.security.User

class UserFiles {
	int id;
	User user;
	String dir;
	String type = "PLINK";
	String identifier;
	String phenotypeFileDesc;
	Date dateCreated;
	Date lastUpdated;
	
	static constraints = {
		dir blank: false, nullable: false
		type blank: false
		identifier nullable: true
		phenotypeFileDesc nullable: true
		dateCreated nullable: false
		lastUpdated nullable: false 
	}
	
	static mapping = {
		version false
		table 'USER_FILES'
	}
}