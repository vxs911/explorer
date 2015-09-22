<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Select Phenotype Range</title>
<g:javascript>
	$(document).ready(function() {
      	
	});

</g:javascript>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">We have retrieved samples matching your criteria</p>
  		</div>
  	</div>
  	
  	<div class="row">
  		<div class="col-md-4">
  			<g:form controller="phenotype" action="selectPhenotype">
				<p>${individuals.length} samples matched your criteria</hp>
				<!-- <div id="slider-range"></div> -->
				<div class="form-group">
					<label></label>
					<input type="text" name="cohort-name" class="form-control" placeholder="Enter a name for this cohort">
				</div>
				<g:submitButton name="submit" value="Save Cohort" class="btn btn-primary" />	
			</g:form>
		</div>		
  	</div>	  
  </div>

</body>
</html>