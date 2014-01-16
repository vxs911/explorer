<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Review of uploaded file</title>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">Your sample cohort has been saved</p>

  			<p>You can now proceed to create another cohort or just create a KM Plot</p>
  			<br> <br> 			
  			<g:link controller="phenotype" action="selectPhenotype" class="btn btn-primary">Select phenotype-based cohort</g:link>
  			<g:link controller="genotype" action="selectGenotype" class="btn btn-primary">Select genotype-based cohort</g:link>
  			<g:link controller="genotype" action="selectGenotype" class="btn btn-primary">Create KM Plot</g:link>
  		</div>
  	
  	</div>	  
  </div>
</body>
</html>