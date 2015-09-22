<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
	<g:javascript>
		$(document).ready(function(){

		});
	
	</g:javascript>
</head>

<body>
<div class="container" style="padding-top: 100px;">
	<div class="row">
		<div class="col-md-4"></div>
		<div class="col-md-4">
			<div class="jumbotron img-rounded">
				<div class='lead'><g:message code="springSecurity.login.header"/></div>
		
				<g:if test='${flash.message}'>
					<div class='text-danger'>${flash.message}</div>
				</g:if>
				<g:elseif test='${params.logout}'>
					<div class="text-success">You have successfully logged out</div>
				</g:elseif>
		
				<form action='${postUrl}' method='POST' id='loginForm' autocomplete='off'>
					<div class="form-group">
						<label for='username'><g:message code="springSecurity.login.username.label"/>:</label>
						<input type='text' name='j_username' id='username' class="form-control"/>
					</div>
		
					<div class="form-group">
						<label for='password'><g:message code="springSecurity.login.password.label"/>:</label>
						<input type='password' name='j_password' id='password' class="form-control"/>
					</div>
		
					<%--<p id="remember_me_holder">
						<input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
						<label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label>
					</p> --%>
		
					<div class="form-group">
						<input type='submit' class="btn btn-lg btn-primary" id="submit" value='${message(code: "springSecurity.login.button")}'/>
					</div>
				</form>
			</div>
		</div>
		<div class="col-md-4"></div>
	</div>
</div>
<script type='text/javascript'>
	<!--
	(function() {
		document.forms['loginForm'].elements['j_username'].focus();
	})();
	// -->
</script>
</body>
</html>
