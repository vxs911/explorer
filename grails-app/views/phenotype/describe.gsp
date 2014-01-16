<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Choose survival phenotype</title>
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
  			<p class="lead">Here are the phenotypes in your file</p>
  			<p>Select the phenotype which describes your clinical endpoint</p>
  		</div>
  	</div>
  	<g:form controller="phenotype" action="setSurvivalPhenotype" >
  		<div class="row">
  		<div class="span3">
					
				<div class="control-group" style="height: 300px; width: 100%; float: left; overflow-y: auto; display: block">
					<g:each in="${headers}">
						<div class="controls">
							<label class="radio">
								<input type="radio" name="phenotype-survival" value="${it}">${it}
							</label>
						</div>
					</g:each>					
				</div>			
		</div>		
  		</div>
  		<div class="row">
			<div class="control-group">
				<input class="btn btn-primary" type="submit" value="Submit">
			</div>  		
  		</div>
	</g:form>
  </div>

</body>
</html>