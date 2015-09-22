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
  		<div class="col-md-12">
  			<p class="lead">To start, upload a tab-separated phenotype file</p>
  			<g:uploadForm role="form" name="chooseInput" method="post" controller="phenotype" action="upload" >
  				<div class="form-group">
  					<label for="exampleInputFile">Phenotype File</label>
  					<input type="file" name="phenotypeFile" id="phenotypeFile" />
  					<p class="help-block"><small>Browse to a tab-separated phenotype file and then click submit</small></p>
  				</div>
  				<button type="submit" class="btn btn-primary">Submit</button>
  			</g:uploadForm>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>