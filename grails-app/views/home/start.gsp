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
  		<div class="col-md-12">
  			<h2 class="page-header">Upload files</h2>
  		</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">To start, tell us what kind of genotype and phenotype files you have</p>
  			<g:form role="form" controller="genotype" action="selectGenotypeFile" method="GET" >
  				<div class="radio">
  					<label><input type="radio" name="inputFileType" value="VCF">VCF</label>
  				</div>
  				<div class="radio">
  					<label><input type="radio" name="inputFileType" value="PLINK">Plink .bim, .fam &amp; .bed</label>
  				</div>  				
  				<button type="submit" class="btn btn-primary">Submit</button>
  			</g:form>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>