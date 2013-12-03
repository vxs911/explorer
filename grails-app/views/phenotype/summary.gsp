<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Summary</title>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">Summary of survival data for KM Plot</p>
  			<p><g:link controller="phenotype" action="plot">Plot KM plot</g:link>
  			<table class="table">
  				<tr>
  					<th>Survival Time</th>
  					<th>Probability</th>
  				</tr>
  				<g:each in="${kmpoints}">
  					<tr>
  						<td>${it.x}</td>
  						<td>${it.y}</td>
  					</tr>
  				</g:each>
  			</table>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>