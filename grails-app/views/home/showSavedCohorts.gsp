<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Welcome to Outcomes Explorer!</title>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">You have the following sample cohorts saved in your session.</p>
  			<g:form role="form" controller="home" action="selectGenotypeFile" method="GET">
	   			<ol>
	  				<g:each in="${session.savedSampleSets}">
	  					<li>${it.name}</li>
	  				</g:each>
	  			</ol>		 				
  				<button type="submit" class="btn btn-primary">Submit</button>
  			</g:form>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>