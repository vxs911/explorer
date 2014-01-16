<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<g:javascript src="jquery-1.10.2.min.js" />
		<g:javascript src="jquery-ui.js" />
		<g:javascript src="bootstrap.min.js" />
		<g:javascript src="bootstrap-select.min.js" />
		<g:javascript src="html5plots.js" />
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-select.min.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css/cupertino', file: 'jquery-ui-1.10.3.custom.css')}" type="text/css">
		<link rel="apple-touch-icon" href="${resource(dir: 'img', file: 'glyphicons-halflings-white.png')}">
		<link rel="apple-touch-icon" href="${resource(dir: 'img', file: 'glyphicons-halflings.png')}">
		<style type="text/css">
			body {
			  padding-top: 50px;
			}			
		</style>
		<g:layoutHead/>
		<r:layoutResources />
	</head>
	<body>
	    <div class="navbar navbar-inverse navbar-fixed-top">
	      <div class="navbar-inner">
	        <div class="container">
	          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
	          <a class="brand" href="#">Outcomes Explorer</a>
	          <sec:ifLoggedIn>
		      <div class="nav-collapse collapse">
		        <ul class="nav">
		          <li class="active"><g:link controller="home" action="index">Home</g:link></li>
		          <li><a href="#about">About</a></li>
		          <li><a href="#contact">Contact</a></li>		        
			      <g:if test="${session.savedSampleSets}">
				  <li class="dropdown">
				  <a href="#" class="dropdown-toggle" data-toggle="dropdown">Saved Sample Sets<b class="caret"></b></a>
			        <ul class="dropdown-menu">
			        	<g:each in="${session.savedSampleCohorts}">
			        		<li><a href="#">${it.name}</a></li>
			        	</g:each>
			        </ul>				  
				  </li>
			      </g:if>
		        </ul>
		      </div><!--/.nav-collapse -->
		      <ul class="nav" style="float:right"><li><g:link controller="logout" action="index">Log Out</g:link></li></ul>
		      </sec:ifLoggedIn>
	        </div>
	      </div>
	    </div>	
		<g:layoutBody/>
		<r:layoutResources />
	</body>
</html>
