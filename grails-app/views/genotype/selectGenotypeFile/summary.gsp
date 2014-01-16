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
  			<p class="lead">Summary</p>
  			<h3>You have successfully uploaded ${session.type} genotype files</h3>
  			<p>If you have a separate phenotype file you can proceed to upload it now</p>
  			<!-- <a onclick="$('#samples').toggle()" href="#">click here to view</a> -->
  			<br> <br> 			
  			<g:link controller="phenotype" action="index" class="btn">Upload phenotype file</g:link>
  			<g:link controller="genotype" action="selectGenotype" class="btn btn-primary">Select genotype</g:link>
  		</div>
  	
  	</div>
  	<br>
  	<%--<div class="row">
		<div class="col-md-2" id="samples" style="display: none">
			<g:each in="${samples}">
				${it}<br>
			</g:each>
		</div>  	
  	</div> --%>
	  
  </div>
</body>
</html>