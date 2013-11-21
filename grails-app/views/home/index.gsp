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
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">To start, upload a VCF file</p>
  			<g:uploadForm role="form" name="chooseInput" method="post" controller="home" action="upload" >
  				<div class="form-group">
  					<label for="exampleInputFile">VCF File</label>
  					<input type="file" name="vcfFile" />
  					<p class="help-block">Browse to a VCF file and then click submit</p>
  				</div>
  				<button type="submit" class="btn btn-default">Submit</button>
  			</g:uploadForm>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>