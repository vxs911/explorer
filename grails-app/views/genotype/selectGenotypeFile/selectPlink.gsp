<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Welcome to Outcomes Explorer!</title>
<g:javascript>
	$(document).ready(function() {
		
	
	});
	
	function onSelectBim(form) {
		var bim = $(form).find("input[name='bim']")[0];
		var tfam = $(form).find("input[name='tfam']")[0];
		var bed = $(form).find("input[name='bed']")[0];
		var filename = bim.value;
		
		if(bim.value.indexOf("\\") > -1) {
			var names = bim.value.split("\\");
			filename = names[names.length - 1];
		}
		
		filename = filename.split(".")[0];
		alert(filename);

		tfam.value = filename + ".tfam";
		bed.value = filename + ".bed";
		
	}
</g:javascript>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">Upload PLINK files</p>
  			<g:uploadForm role="form" name="chooseInput" method="post" controller="genotype" action="selectGenotypeFile" >
  				<div class="control-group">
  					<label for="bim">PLINK Files (.bim, .tfam, .bed) </label>
  					<div class="controls">
  						<input type="file" name="plink" accept=".bim, .tfam, .bed" multiple />
  					</div>
  					<!-- <label for="tfam">TFAM File <input type="file" name="tfam" /> </label><br>
  					<label for="bed">BED File <input type="file" name="bed" /> </label> -->
  					
  					<span class="help-block">Browse to directory containing the files above click submit</span>
  				</div>
  				<g:submitButton name="submit" value="Upload" class="btn btn-primary" />
  			</g:uploadForm>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>