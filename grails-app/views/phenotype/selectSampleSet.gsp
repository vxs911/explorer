<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Summary</title>
<g:javascript>
	$(document).ready(function(){
		$("select").selectpicker();	
	});

</g:javascript>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">Select a sample set</p>
			<br/><br/>
			<g:form controller="phenotype" action="selectSampleSet">
				<div class="form-group">
					<label for="sample-set-name" class="col-md-2 control-label">Sample Set</label>	
					<select name="sample-set-name" id="sample-set-name">
						<g:each in="${session.savedSamples.keySet()}">
							<option value="${it}">${it}</option>
						</g:each>
					</select>
				</div>
				<input type="hidden" name="column-survival" value="${params['column-survival']}">
				<div class="form-group">
					<input type="submit" value="Submit" class="btn btn-primary">
				</div>				
			</g:form>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>