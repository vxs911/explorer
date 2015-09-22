<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Cohort Saved Successfully</title>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">Cohort ${cohortName} saved successfully!</p>

  			<br> <br> 			
  			<g:link controller="home" action="selectGenotype" class="btn btn-primary">Start over</g:link>
  			<g:link controller="km" action="index" params="${ ['cohort-name':cohortName] }" class="btn btn-primary">Create KM Plot</g:link>

  		</div>
  	
  	</div>
  	<br>
	  
  </div>
</body>
</html>