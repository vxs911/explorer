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
  		<div class="col-md-12">
  			<h2 class="page-header">Summary</h2> 			
  			<p class="lead">Okay, here is a summary of the files loaded in this session</p>
  			<b>Genotype Files</b>
  			<ol>
  				<g:each in="${genotypeFiles}">
  					<li>${it.getName()}</li>
  				</g:each>
  			</ol>  			
   			<b>Phenotype Files</b>
  			<ol>
  				<g:each in="${phenotypeFiles}">
  					<li>${it.getName()}</li>
  				</g:each>
  			</ol>

  			<g:if test="${!session.reader}">
	  			<p>
		  			Click the button below to configure the phenotype file for reading.
	  			</p>
	  			<br>
	  			<g:link controller="phenotype" action="describe" class="btn btn-primary">Configure</g:link>
	  			<br>
 			</g:if>
  			<g:else>
  				<p>You can now start creating cohorts
  				or you can quickstart survival analysis</p>
  				<g:link controller="cohorts" action="index" class="btn btn-primary">Create cohorts</g:link>
  				<g:link controller="km" action="explore" class="btn btn-primary">Quickstart KM</g:link>
  			</g:else>
  		</div>
  	</div>	  
  </div>
</body>
</html>