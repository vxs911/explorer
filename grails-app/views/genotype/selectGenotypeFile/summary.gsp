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
  		<div class="col-md-8">
  			<h2>Summary</h2>
  			<p class="lead">You have successfully uploaded ${type} genotype files</p>
  			<p>Please proceed to upload a phenotype file containing your clinical and/or survival data</p>
  			<br> <br> 			
  			<g:link controller="phenotype" action="index" class="btn btn-primary">Upload phenotype file</g:link>
  		</div>
  	
  	</div>	  
  </div>
</body>
</html>