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
  		<div class="col-md-12">
  			<h2 class="page-header">Upload files</h2>
  		</div>
  		<div class="col-md-4">
  			<p class="lead" style="padding:0px">PLINK</p>
  			<g:uploadForm role="form" name="chooseInput" method="post" controller="genotype" action="selectGenotypeFile" >
  				<div class="form-group">
  					<label>Please provide a name for these files</label>	
				    <input type="text" name="identifier" id="identifier" class="form-control"/>		    
  				</div>
  				<div class="form-group">
  					<label class="control-label" for="plink">PLINK Files (.bim, .fam, .bed) </label>
  					<input type="file" name="plink" id="plink" accept=".bim, .fam, .bed" multiple />	
  				</div>
  				<div class="form-group">
  					<p class="help-block"><small>Browse to directory containing the files above click submit</small></p>					
  					<g:submitButton name="submit" value="Upload" class="btn btn-primary" />
  				</div>
  			</g:uploadForm>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>