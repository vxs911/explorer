<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Choose survival phenotype</title>
<g:javascript>
	$(document).ready(function() {
		$(".selectpicker").selectpicker();
	});

</g:javascript>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">Select Clinical Endpoint</p>
  			<p>Select the variable names which describes your clinical endpoint</p>
  		</div>
  	</div>
  	<div class="row">
	  	<div class="col-md-12" ><g:render template="/km/describe" /></div>
	</div>
  </div>

</body>
</html>