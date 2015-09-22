package edu.georgetown.explorer.security

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

class RedirectLogoutSuccessHandler extends
		AbstractAuthenticationTargetUrlRequestHandler implements
		LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth) throws IOException,
			ServletException {
				//flash.message = "You have successfully logged out"
				response.sendRedirect("login/auth?logout=true")
	}

}