<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Describe Phenotype file</title>
<g:javascript>
	$(document).ready(function(){
		$("#sample-column-header").selectpicker();
		$("#sample-column-noheader").selectpicker("hide");
	});
	
	function toggleHeaders(radio) {
		if(radio.value == "false") {
			$("#sample-column-noheader").removeAttr("disabled").selectpicker("show");
			$("#sample-column-header").hide().attr("disabled","true").selectpicker("hide");
		}
		
		else if(radio.value == 'true') {
			$("#sample-column-noheader").hide().attr("disabled","true").selectpicker("hide");
			$("#sample-column-header").removeAttr("disabled").selectpicker("show");		
		}
		$("select").selectpicker('refresh');
	}
	
	function process(form) {
	
	}
</g:javascript>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<h2 class="page-header">Enter phenotype parameters here</h2>  	
  	</div>
  	<div class="row">
  		<div class="col-md-4">
  			<g:form controller="phenotype" action="process" onsubmit="return process(this);">
				<div class="form-group">
					<label>Does the phenotype file have a header line?</label>
					<br>	
					<input type="radio" name="contains_header" value="true" onclick="toggleHeaders(this);" /> Yes
					<input type="radio" name="contains_header" value="false" onclick="toggleHeaders(this);" /> No
				</div> 	
				<div class="form-group">
					<label>Which column contains the samples?</label>
					<g:select from="${headers}" keys="${1..headers.length}" name="sample_column" class="form-control" id="sample-column-header" disabled="true" />
					<g:select from="${1..headers.length}" name="sample_column" class="form-control" id="sample-column-noheader" style="display:none"/>		
				</div>
				<!-- 
				<div class="form-group">
					<label>What is the unknown value marker?</label>
					<input type="text" class="form-control">
				</div>
				 -->
				<input type="submit" value="Submit" class="btn btn-primary">
			</g:form>	
  		</div>
  	</div>
  </div>
</body>
</html>