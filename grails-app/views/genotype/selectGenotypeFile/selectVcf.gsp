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
  		<div class="col-md-4">
  			<p class="lead">VCF</p>
  			<g:uploadForm role="form" name="chooseInput" method="post" controller="genotype" action="selectGenotypeFile" >
  				<div class="form-group">
  					<label>Please provide a name for these files</label>	
				    <input type="text" name="identifier" id="identifier" class="form-control"/>		    
  				</div>
  				<div class="form-group">
  					<label for="exampleInputFile">VCF File (.vcf)</label>
  					<input type="file" name="vcf" accept=".vcf" />
  					<p class="help-block">Browse to a VCF file and then click submit</p>
  				</div>
  				<g:submitButton name="submit" value="Upload" class="btn btn-primary" />
  			</g:uploadForm>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>