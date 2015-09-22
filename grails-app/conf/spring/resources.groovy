// Place your Spring DSL code here
import edu.georgetown.explorer.security.LoginSuccessEventListener
import edu.georgetown.explorer.security.LoginFailureEventListener
import edu.georgetown.explorer.security.RedirectLogoutSuccessHandler
import edu.georgetown.explorer.security.LogoutSuccessEventListener
import edu.georgetown.explorer.security.SessionTimeoutListener

beans = {
	loginSuccessEventListener(LoginSuccessEventListener)
	loginFailureEventListener(LoginFailureEventListener)
	logoutSuccessHandler(RedirectLogoutSuccessHandler)
	myLogoutEventListener(LogoutSuccessEventListener)
	sessionTimeoutListener(SessionTimeoutListener)
}