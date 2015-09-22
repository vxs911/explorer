<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Explore Genotype</title>
<g:javascript>	
	$(document).ready(function(){
		var hashLocation = window.location.hash;
		$(".selectpicker").selectpicker();
		$('#myTab a[href="'+hashLocation+'"]').tab('show');
		$("input[type=radio]").hide();
	});
	
	function saveCohort(form) {
		bootbox.prompt("Enter a name for this cohort: (RS ID: "+$("#rs-id").val()+")", function(result){
			$(form).find("input[name=cohort_name]").val(result);
			if(result)
				$.ajax(form.action, { data: $(form).serialize(), success:function(response) {
						$("form").siblings("p.well").html("Cohort saved successfully").fadeIn().delay(5000).fadeOut();
					}		
				});
		});
		
		return false;
	}
	
	function loadPhenotypeFilter(form) {		
		var genotypeType = $(form).find("input[name=genotype_type]:checked").val();
		$("#genotype-type-hidden").val(genotypeType);
		$('#myTab a[href="#phenotypeDiv"]').tab('show');
		return false;
	}
	
	function submitPhenotypeName(form) {
		$.ajax(form.action, { dataType:"json", data: $(form).serialize(), 
			success:function(response) {
				if((response.minimum != null) && (response.maximum != null)) {
					$("#numeric-phenotype-div").show();
					$("#range").html(response.minimum+" - " + response.maximum);
				}
				
				else {
					$("#numeric-phenotype-div").hide();
				}
				var options = [];
				options.push("<option value=\"\"></option>");
				for(var i = 0; i < response.uniqueValues.length; i++) {
					options.push("<option value=\""+response.uniqueValues[i]+"\">"+response.uniqueValues[i]+"</option>");
				}
				$("#phenotype-unique-values").html(options.join(''));
				$("#phenotype-unique-values").selectpicker('refresh');
				$("#phenotype-name-hidden").val(response.phenotypeName);
				$('#myTab a[href="#phenotypeRangeDiv"]').tab('show');
			}
		
		});
		
		return false;
	}
	
	function submitPhenotypeParams(form) {
		$.ajax(form.action, { dataType:"json", data: $(form).serialize(), 
			success:function(response) {
				$("#cohorts").html("There are "+response.count+" samples in your cohort");
				$("#cohort-key").val(response.key);
				$('#myTab a[href="#saveCohortDiv"]').tab('show');
			}
		});	
		
		return false;
	}

</g:javascript>
</head>
<body>
	<g:render template="/common/modals" />
	<g:render template="/common/explore" />
	<g:render template="/common/jfunctions" />
	<div class="container">
	 	<div class="row">
	 		<p>&nbsp;</p>
	 		<div class="jumbotron">
	 			<h2>Create cohorts for KM Plots.....</h2>
	 			<p>In this page, you can create cohorts based upon a genotype marker</p>
	 		</div>
		</div>
		
		<div class="row">
			<!-- Nav tabs -->
			<div class="col-md-12">
				<ul class="nav nav-pills" id="myTab">
				  <li class="active"><a href="#geneDiv" data-toggle="tab">HGNC Gene Symbol</a></li>
				  <li><a href="#rsidDiv" data-toggle="tab">DbSNP RS ID</a></li>
				  <li><a href="#chromosomeDiv" data-toggle="tab">Chromosome</a></li>
				  <li><a href="#positionDiv" data-toggle="tab">Position</a></li>
				  <li><a href="#genotypeDiv" data-toggle="tab">Cohort</a></li>
				  <li><a href="#phenotypeDiv" data-toggle="tab">Phenotype/Clinical parameter</a></li>
				  <li><a href="#phenotypeRangeDiv" data-toggle="tab">Phenotype range/values</a></li>
				  <li><a href="#saveCohortDiv" data-toggle="tab">Cohort</a></li>
				</ul>
			</div>
			
			<!-- Tab panes -->
			<div class="col-md-3">
				<div class="tab-content">
				  <div class="tab-pane active" id="geneDiv">
				  	<p>&nbsp;</p>
				  	<g:form controller="genotype" action="ajaxGetRsIdsByGeneSym" onsubmit="return submitGeneSymbol(this);">
				  		<div class="form-group">
					  		<input type="text" name="sym" class="form-control" placeholder="Enter an HGNC Gene Symbol" >
					  	</div>
					  	<div class="form-group">
					  		<p id="rsids-display-toggle" style="display:none"></p>
					  		<div id="rsids-found" style="display:none"></div>
					  	</div>
					  	<input type="submit" class="btn btn-success" value="Submit">
				  	</g:form>				  	
				  </div>
				  <div class="tab-pane" id="rsidDiv">
				  	<p>&nbsp;</p>
				  	<g:form controller="genotype" action="ajaxGetGenotypeByRsId" onsubmit="return submitRsId(this);">
				  		<div class="form-group">
					  		<input type="text" name="rs_id" id="rs-id" class="form-control" placeholder="Enter an RSID" >
					  	</div>
					  	<div class="form-group">
					  		<p class="help-block"><small>* If you don't have an RSID, you can instead start by choosing a chromosome first.</small></p>
					  	</div>
					  	<input type="submit" class="btn btn-success" value="Submit">
				  	</g:form>
				  </div>
				  <div class="tab-pane" id="chromosomeDiv">
				  	<p>&nbsp;</p>
				  	<g:form controller="genotype" action="#" onsubmit="return submitChromosome(this);">
					  	<div class="form-group">
					  		<g:select name="chromosome" id="chromosome" from="${chromosomes}" class="form-control selectpicker" />
					  	</div>
					  	<input type="submit" class="btn btn-success" value="Submit">
				  	</g:form>
				  </div>
				  <div class="tab-pane" id="positionDiv">
				  	<p>&nbsp;</p>
				  	<g:form controller="genotype" action="ajaxGetGenotypeByChromosomeAndPosition" onsubmit="return submitChromosomeAndPosition(this);">
					  	<div class="form-group">
					  		<input type="hidden" name="chromosome" id="chromosome-hidden" />
					  		<input name="position" id="position" class="form-control" />
					  	</div>
					  	<input type="submit" class="btn btn-success" value="Submit">
				  	</g:form>	  
				  </div>
				  <div class="tab-pane" id="genotypeDiv">
				  	<p class="well well-sm" style="font-style:italic; display: none">&nbsp;</p>
				  	<g:form controller="genotype" action="ajaxSaveCohort">
				  		<div class="form-group">
				  			<p id="ref-alt-values"></p>
							<div class="radio" id="hetDiv"><label></label><input type="radio" name="genotype_type" value="het" ></div>
							<div class="radio" id="homRefDiv"><label></label><input type="radio" name="genotype_type" value="hom_ref" ></div>
							<div class="radio" id="homAltDiv"><label></label><input type="radio" name="genotype_type" value="hom_alt" ></div>						  			
				  		</div>
				  		<div class="form-group">
				  			<input type="hidden" name="cohort_name" />
				  			<input type="submit" class="btn btn-success" value="Save Cohort" onclick="return saveCohort(this.form);">
				  			<input type="submit" class="btn btn-success" onclick="return loadPhenotypeFilter(this.form);" value="Phenotype filter">
				  		</div>
				  	</g:form>				  
				  </div>

				  <div class="tab-pane" id="phenotypeDiv">
				  	<p>&nbsp;</p>
				  	<g:form controller="phenotype" action="ajaxGetPhenotypeDescriptionFromName" onsubmit="return submitPhenotypeName(this);">
				  		<div class="form-group">
					  		<g:select name="phenotype_name" from="${phenotypeNames}" class="form-control selectpicker" />
					  	</div>
					  	<input type="submit" class="btn btn-success" value="Submit">
				  	</g:form>
				  </div>
				  <div class="tab-pane" id="phenotypeRangeDiv">
				  	<p>&nbsp;</p>
				  	<g:form controller="phenotype" action="ajaxGetCohortBasedOnPhenotype" onsubmit="return submitPhenotypeParams(this);">
				  		<div id="numeric-phenotype-div">
							<p>
							  <label for="amount">Range:</label>
							  <span style="border:0; color:#f6931f; font-weight:bold;" id="range"></span>
							</p>
							<div class="form-group">
								<label for="minimum">Minimum value</label>
								<input type="text" name="minimum" id="minimum" class="form-control" />
							</div>
							<div class="form-group">
								<label for="maximum">Maximum value</label>
								<input type="text" name="maximum" id="maximum" class="form-control" />
							</div>
							<div class="form-group"><p class="text-danger">OR</p></div>
						</div>
						<div class="form-group">
							<label>Select a value</label>
							<select name="phenotype_value" id="phenotype-unique-values"></select>
						</div>
						<input type="hidden" name="phenotype_name" id="phenotype-name-hidden" />
						<input type="hidden" name="genotype_type" id="genotype-type-hidden" />
						<g:submitButton name="submit" value="Continue" class="btn btn-success" />	
				  	</g:form>
				  </div>
				  <div class="tab-pane" id="saveCohortDiv">
				  	<p class="well well-sm" style="font-style:italic; display: none">&nbsp;</p>
				  	<p id="cohorts">&nbsp;</p>
				  	<g:form controller="phenotype" action="ajaxSaveCohort" onsubmit="return saveCohort(this);">
				  		<input type="hidden" name="cohort_key" value="" id="cohort-key" />
				  		<input type="hidden" name="cohort_name" value="" />
				  		<input type="submit" value="Save Cohort" class="btn btn-success" />
				  	</g:form>
				  </div>


				</div> <!--  tab-content  -->
			</div> <!-- col-md-3 -->
			<div class="col-md-1">&nbsp;</div>
			<div class="col-md-8">
				<div id="ChartDiv" style="height:200px" class="row"></div>
			</div>
		</div>
		
	</div>

</body>
</html>