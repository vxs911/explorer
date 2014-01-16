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
  			<p class="lead">For the phenotype you selected, here is the range of values</p>
  		</div>
  	</div>
  	
  	<div class="row">
  		<div class="col-md-3">
  			<g:form controller="phenotype" action="selectPhenotype">
				<p>
				  <label for="amount">Range:</label>
				  <span style="border:0; color:#f6931f; font-weight:bold;">${minimum} - ${maximum}</span>
				</p>
				<h3>Enter the range of values you want to filter your samples by:</h3>
				<!-- <div id="slider-range"></div> -->
				<div class="control-group">
					<label><input type="text" name="minimum"> Minimum value</label>
				</div>
				<div class="control-group">
					<label><input type="text" name="maximum"> Maximum value</label>
				</div>
				<g:submitButton name="submit" value="Continue" class="btn btn-primary" />	
			</g:form>
		</div>		
  	</div>	  
  </div>

</body>
</html>