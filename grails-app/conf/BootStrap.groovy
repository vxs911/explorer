import edu.georgetown.explorer.security.User
import edu.georgetown.explorer.security.Role
import edu.georgetown.explorer.security.UserRole
import org.rosuda.JRI.Rengine

class BootStrap {

    def init = { servletContext ->
		//def adminRole = Role.findByAuthority("ROLE_ADMIN")
		// def userRole = Role.findByAuthority("ROLE_USER")
		  
		//Date time = new java.util.Date();
		//User testUser = new User(username: 'me', enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, password: 'password', dateCreated: time, lastUpdated: time)
		//testUser.save(flush: true)
   
		//UserRole.create testUser, adminRole, true
		//System.loadLibrary("jri");
		//String path = System.getProperty("java.library.path");
		//System.setProperty("java.library.path", path+":/Library/Frameworks/R.framework/Versions/3.0/Resources/library/rJava/jri");
		//log.debug "System property value: "+System.getProperty("java.library.path")
		//log.debug "R_HOME: "+System.getenv("R_HOME")
		//Runtime.getRuntime().load0(groovy.lang.GroovyClassLoader.class, "/Library/Frameworks/R.framework/Versions/3.0/Resources/library/rJava/jri/libjri")
		//System.loadLibrary("libjdns_sddsd");
		//System.loadLibrary("libjri");
		//String[] rParams = ["--vanilla"]
		//Rengine re = new Rengine(rParams, false, null);
    }
    def destroy = {
    }
}