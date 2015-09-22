<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Start creating cohorts here</title>
<g:javascript>
	
	$(document).ready(function(){
		$("input[type=radio]").click(function(){
			if($("input[type=radio]:checked").size() > 0) $("input[type=submit]").removeAttr("disabled");
		});
	});

</g:javascript>
</head>
<body>
	<div class="container">
	 	<div class="row">
	 		<p>&nbsp;</p>
	 		<div class="jumbotron">
	 			<h2>Create cohorts for KM Plots.....</h2>
	 			<p>In this page, you can start create cohorts based upon a genotype marker or phenotype/clinical variable</p>
	 		</div>
		</div>
		
		<div class="row">
			<!-- Nav tabs -->
			<div class="col-md-12">
				<p class="lead">Where do you want to start?</p>
			</div>
			
			<!-- Tab panes -->
			<div class="col-md-8">
				<g:form controller="cohorts" action="index">
					<div class="radio">
						<label>Start with an HGNC Gene Symbol</label><input type="radio" name="choice" value="symbol" />
					</div>
					<div class="radio">
						<label>Start with an RSID</label><input type="radio" name="choice" value="rsid" />
					</div>
					<div class="radio">
						<label>Start with a chromosome and chromosome position</label><input type="radio" name="choice" value="chromosome" />
					</div>
					<div class="radio">
						<label>Start with a phenotype/clinical variable</label><input type="radio" name="choice" value="phenotype" />
					</div>
					<input type="submit" class="btn btn-primary" disabled value="Continue" />			
				</g:form>
			</div> <!-- col-md-8 -->
		</div>
		
	</div>

</body>
</html>