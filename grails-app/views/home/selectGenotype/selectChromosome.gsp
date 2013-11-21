<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Select Genotype</title>
<g:javascript>
	$(document).ready(function(){

		$("#chromosome").val(${params.chromosome});

		$("#position").val(${params.position});
		
		$("#rangeIndex").val(${params.rangeIndex});

		$("#reference").change(function(){
			$("#genotype").val($(this).val());
			$("#alternate").val("");
		});
		$("#alternate").change(function(){
			$("#genotype").val($(this).val());
			$("#reference").val("");
		});
		
		$('input[name=genotype_type][value="${params.genotype_type}"]').first().attr('checked','checked');
		
		$("select").selectpicker();
		
		$("#rangeIndex").change(function(){
			$("#position").attr("disabled", "disabled");
			$("#position").selectpicker('val',"");
			$("#position").selectpicker('refresh');
		});		
	});
</g:javascript>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<g:form controller="home" action="selectGenotype" role="form" class="form-horizontal" method="get">	
  			
				<p class="lead">Pick genotype</p>	
				<div class="form-group">		
					<label for="chromosome" class="col-md-2 control-label">Chromosome</label>							
 					<select name="chromosome" id="chromosome">
 						<g:each in="${chromosomes}">
 							<option value="${it}">${it}
 						</g:each>
 					</select>				
				</div>  
				
				<div class="form-group">					
					<label for="position" class="col-md-2 control-label">Position</label>	 					
					<input type="text" name="position" class="form-control" style="width: 220px;" placeholder="Type a position">
															
				</div>					
				
				<div class="form-group">
					<div class="col-md-offset-2"><span style="cursor: pointer; color: blue" onclick="$('#help').removeAttr('disabled'); document.forms[0].submit();">Help me pick a position</span></div>
					<input type="hidden" name="help" value="false" disabled />				
				</div>
				
				<g:if test="${groups}">  																			
					<div class="form-group">
						<label for="rangeIndex" class="col-md-2 control-label">Range</label>	 					
	 					<select name="rangeIndex" id="rangeIndex">
	 							<option value="-1"></option>
	 						<g:each in="${groups.keySet()}">
	 							<option value="${it}">${groups[it].get(0)} - ${groups[it].get(groups[it].size() - 1)}</option>
	 						</g:each>
	 					</select>										
					</div>											
				</g:if>
								
				<g:if test="${positions}">  										
					<div class="form-group">
						<label for="position" class="col-md-2 control-label">Position</label>	 					
	 					<select name="position" id="position">
	 							<option value="-1"></option>
	 						<g:each in="${positions}">
	 							<option value="${it}">${it}
	 						</g:each>
	 					</select>
					</div>	
				</g:if>
			
				<g:if test="${reference || alternate}"> 
					<div class="form-group">
						<label for="reference" class="col-sm-2 control-label">Genotype</label>
						<div class="col-md-10">	
							Reference: ${reference} &nbsp;&nbsp;&nbsp; Alternate: ${alternate}
							<br>
							<label><input type="radio" name="genotype_type" value="het"> Heterogenous</label>
							<label><input type="radio" name="genotype_type" value="hom_ref"> Homogenous Reference</label> 
							<label><input type="radio" name="genotype_type" value="hom_alt"> Homogenous Alternate</label>
							<input type="hidden" name="genotype" id="genotype" value="">					
						</div>
					</div>		
				</g:if>
				
				<g:if test="${samples}">
					<div class="form-group">
						<label for="reference" class="col-sm-2 control-label">Samples</label>
						<div class="col-sm-10">
							<input type="checkbox" name="samples" value="het"> ${count} samples
						</div>
					</div>				
				</g:if>						
  				
  				<div class="form-group">
  					<div class="col-sm-10"><input type="submit" name="submit" value="Continue" class="btn btn-primary"></div>
  				</div>
  			</g:form>
  		</div>
  	
  	</div>
	  
  </div>
</body>
</html>