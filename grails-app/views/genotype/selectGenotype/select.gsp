<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Select Genotype</title>
<g:javascript>
	$(document).ready(function(){

		$("#chromosome").val(${chromosome});

		$("#position").val(${position});
		
		$("#rangeIndex").val(${rangeIndex});

		$("#reference").change(function(){
			$("#genotype").val($(this).val());
			$("#alternate").val("");
		});
		$("#alternate").change(function(){
			$("#genotype").val($(this).val());
			$("#reference").val("");
		});
		
		$('input[name=genotype_type][value="${params.genotype_type}"]').first().attr('checked','checked');
		
		
		
		$("#rangeIndex").change(function(){
			$("#position").attr("disabled", "disabled");
			$("#position").selectpicker('val',"");
			$("#position").selectpicker('refresh');
		});
		
		$("select").selectpicker();
		
		$("input[type=radio]").click(function() {
			if($("input:checked").length > 0) {
				$("#save-cohort-button").removeAttr("disabled");
			}
			
			else {
				$("#save-cohort-button").attr("disabled","true");
			}
		});
	});
	
	function promptForName() {
		var name = prompt("Enter a name for this sample set", "Sample Name");
		
		if(name != null && name != "") {
			$("input[name='cohort-name']").val(name);
			element.form.submit();
		}
		
		else return false;
	}
	
	function saveCohort() {
		var cohortName = $("input[name=modal-input-value]").val();
		alert(cohortName);
		$("input[name='cohort-name']").val(cohortName);
		document.forms[0].submit();
		$("#myModal").hide();
	}
</g:javascript>
</head>
<body>

<!-- Modal -->
<div class="modal fade bs-modal-sm in" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Save Cohort</h4>
      </div>
      <div class="modal-body">
        <div class="form-group">
	       	<label>Cohort Name: </label>
	       	<input type="text" class="form-control" name="modal-input-value">       
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" onclick="saveCohort();">Save changes</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

  <div class="container">
  	<div class="row">
  		<p>&nbsp;</p>
  		<div class="jumbotron">
  			<h2>Create genotype cohorts here.....</h2>
  			<p>In this page, you can create cohorts based upon a dbSNP RS ID or a chromosome location. You can also simply create a KM Plot!</p>
  		</div>
  		<div class="col-md-3">
  			<g:form controller="genotype" action="selectGenotype" role="form" method="get">
  				<%--<input type="hidden" name="mode" value="${mode}"> --%>
  			
				<p class="lead">Pick genotype</p>	
				<g:if test="${chromosomes}">
					<div class="form-group">		
						<label for="chromosome">Chromosome</label>							
	 					<select name="chromosome" class="form-control" id="chromosome" <g:if test="${chromosome}">disabled</g:if> >
	 						<g:each in="${chromosomes}">
	 							<option value="${it}">${it}
	 						</g:each>
	 					</select>	
	 					<g:if test="${chromosome}">
	 						<g:submitButton name="changeChromosome" class="btn btn-link" value="Select another chromosome" />		
	 					</g:if>
					</div> 
				</g:if>
				<g:else>
					<div class="form-group">
						<input type="text" name="rs_id" class="form-control" value="${rsId}" placeholder="Enter an RSID" <g:if test="${rsId}">disabled</g:if> >
						<g:if test="${!rsId}"><g:submitButton name="doNotKnow" class="btn btn-link" value="I don't have an RSID" /></g:if>
						<g:if test="${rsId}">
							<g:submitButton name="changeRsId" class="btn btn-link" value="Change RSID" />
						</g:if>
					</div>				
				</g:else>
								
				<g:if test="${groups && mode && (mode == "range")}">  																			
					<div class="form-group">
						<label for="rangeIndex">Range</label>	 					
	 					<select name="rangeIndex" class="form-control" id="rangeIndex" <g:if test="${rangeIndex}">disabled</g:if> >
	 							<option value="-1"></option>
	 						<g:each in="${groups.keySet()}">
	 							<option value="${it}">${groups[it].get(0)} - ${groups[it].get(groups[it].size() - 1)}</option>
	 						</g:each>
	 					</select>	
	 					<g:if test="${rangeIndex}">
	 						<g:submitButton name="changeRangeIndex" class="btn btn-link" value="Select another range" />	
	 					</g:if>	 														
					</div>	
					
					<g:if test="${!positions}">
						<div class="form-group">
							<g:submitButton name="changeMode" class="btn btn-link" value="Click to enter a chromosome position" />			
						</div>	
					</g:if>									
				</g:if>
				
				<g:if test="${mode && (mode == "type")}">
					<div class="form-group">					
						<label for="position">Position</label>	 					
						<input type="text" name="position" class="form-control" id="position" placeholder="Type a position" value="${position}" <g:if test="${position}">disabled</g:if>>
	 					<g:if test="${position}">
	 						<g:submitButton name="changePosition" class="btn btn-link" value="Select another position" />
	 					</g:if>																
					</div>					
					
					<g:if test="${!(reference || alternate)}">
						<div class="form-group">
							<g:submitButton name="changeMode" class="btn btn-link" value="Help me pick a position" />
							<input type="hidden" name="help" value="false" disabled />				
						</div>	
					</g:if>			
				</g:if>
								
				<g:if test="${positions}">  										
					<div class="form-group">
						<label for="position">Position</label>	 					
	 					<select name="position" class="form-control" id="position" <g:if test="${position}">disabled</g:if> >
	 							<option value="-1"></option>
	 						<g:each in="${positions}">
	 							<option value="${it}">${it}
	 						</g:each>
	 					</select>
	 					<g:if test="${position}">
	 						<g:submitButton name="changePositionWithRange" class="btn btn-link" value="Select another position" />	
	 					</g:if>	 
					</div>	
				</g:if>
			
				<g:if test="${reference || alternate}"> 
					<div class="form-group">
						<label for="reference">Genotype</label>
						<br>			
							Reference: ${reference} &nbsp;&nbsp;&nbsp; Alternate: ${alternate}
							<g:set var="genotype_disabled" value="${genotype_type? "disabled":""}"></g:set>
							<br>
							<div class="radio"><label><input type="radio" name="genotype_type" value="het" ${genotype_disabled}> Heterogenous (${count["het"]} samples)</label></div>
							<div class="radio"><label><input type="radio" name="genotype_type" value="hom_ref" ${genotype_disabled}> Homogenous Reference (${count["hom_ref"]} samples)</label></div>
							<div class="radio"><label><input type="radio" name="genotype_type" value="hom_alt" ${genotype_disabled}> Homogenous Alternate (${count["hom_alt"]} samples)</label></div>		
							<g:if test="${genotype_type}"><g:submitButton name="changeGenotype" class="btn btn-link" value="Select another genotype" /></g:if>
							<input type="hidden" name="cohort-name" />
							<input type="hidden" name="_eventId_saveSamples" value="Save Cohort">
						<br>										
					</div>
				</g:if>
				
				<g:if test="${individuals}">
					<div class="form-group">
						<label for="reference">Cohort Size</label>
						${individuals.length}
						<input type="hidden" name="cohort-name" />
					</div>				
				</g:if>						
  				
  				<div class="form-group">					
  						<g:if test="${individuals}">
  							<g:submitButton name="saveSamples" value="Save Cohort" class="btn btn-primary" onclick="return promptForName(this);" />
  							<g:submitButton name="startOver" value="Start Over" class="btn btn-primary" />
  						</g:if>
  						<g:elseif test="${reference || alternate}">
  							<g:submitButton name="createKmPlot" value="Create KM Plots" class="btn btn-primary" />
  							<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal" disabled id="save-cohort-button">Save Cohort</button>
  						</g:elseif>
  						<g:else>
  							<g:submitButton name="submit" value="Continue" class="btn btn-primary" />
  						</g:else>
  					
  				</div>
  			</g:form>
  		</div>
  		<div class="col-md-3"></div>
  		<div class="col-md-6">
  			<div id="ChartDiv" style="height:500px" class="row"></div>
  		</div>
  	</div>
	  
  </div>
</body>
</html>