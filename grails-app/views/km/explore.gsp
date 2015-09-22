<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Explore Genotype</title>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<g:javascript>
	google.load("visualization", "1", {packages:["corechart"]});
	
	$(document).ready(function(){
		$(".selectpicker").selectpicker();
		$("input[type=radio]").hide();
	});
	
	
	function setSurvivalPhenotype() {
		var survivalParamsForm = document.forms[0];
		$.ajax("<g:createLink controller="km" action="setSurvivalPhenotype" />",{ data: $(survivalParamsForm).serialize(), success:function(data) {
				$("#set-survival-params-modal").modal('hide');
				drawChart();		
			}		
		});
	}
	
	function checkSurvivalAndPlot() {
		$.ajax("<g:createLink controller="km" action="ajaxcheckSurvivalPhenotypeSet" />",{ 
			success:function(response) {
				if(response == "no") $("#set-survival-params-modal").modal('show');				
				else drawChart();
			}
		});	
	}
	
	function drawChart() {
		var config = {title: 'KM Plot for dbSNP RS ID: '+$("input[name=rs_id]").val()};
		drawKMPlot($("#set-survival-form").serialize()+"&cohort-type=temp", config);
		return false;	
	}

</g:javascript>
</head>
<body>

	<g:render template="/common/modals" />
	<g:render template="/common/explore" />
	<g:render template="/common/jfunctions" />
	<div class="modal fade bs-modal-sm in" id="set-survival-params-modal" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="set-survival-params-modal-label" aria-hidden="true">
	  <div class="modal-dialog modal-sm">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="set-survival-params-modal-label">Select your survival parameters</h4>
	      </div>
	      <div class="modal-body">
	      	<g:render template="/km/describe" />
	      </div>
	      <div class="modal-footer">
	      	<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" onclick="setSurvivalPhenotype();">Select and Plot</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	
	<div class="container">
	 	<div class="row">
	 		<p>&nbsp;</p>
	 		<div class="jumbotron">
	 			<h2>Quickstart KM Plots.....</h2>
	 			<p>In this page, you can search for genotypes based upon RSID or chromosome and position and then quickly plot them</p>
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
				  <li><a href="#genotypeDiv" data-toggle="tab">Genotype</a></li>
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
				  	<g:form controller="genotype" name="rsid-submit-form" action="ajaxGetGenotypeByRsId" onsubmit="return submitRsId(this);">
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
				  	<p>&nbsp;</p>
				  	<g:form>
				  		<div class="form-group">
				  			<p id="ref-alt-values"></p>
				  			<ul>
				  				<li id="hetDiv"><label></label></li>
				  				<li id="homRefDiv"><label></label></li>
				  				<li id="homAltDiv"><label></label></li>
				  			</ul>					  			
				  		</div>
				  		<div class="form-group">
				  			<a href="#" onclick="checkSurvivalAndPlot();" class="btn btn-success">Create KM Plots</a>
				  		</div>
				  	</g:form>				  
				  </div>
				</div>
			</div> <!-- col-md-4 -->
			<div class="col-md-1">&nbsp;</div>
			<div class="col-md-8">
				<div id="ChartDiv" style="height:500px" class="row"></div>
			</div>
		</div>
		
	</div>

</body>
</html>