<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>KM Plot</title>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<g:javascript>
	google.load("visualization", "1", {packages:["corechart"]});
	google.setOnLoadCallback(function() {
	
		//var dropdown = document.getElementsByTagName("select")[0];
		//if(dropdown.length == 0) $(dropdown).attr("disabled","true");
		//$("select").selectpicker();
		$("#survival-time-variable-name").selectpicker();
		$("#survival-event-variable-name").selectpicker();
		
		<g:if test="${session["phenotypeFileReader"]}">		
			if($("select[name=cohort-name] option").size() == 0) $("select[name=cohort-name]").selectpicker({title:"No cohorts created"});
			
			else $("select[name=cohort-name]").selectpicker({title:"Select a cohort"});
			
			
			
			<g:if test="${params["cohort-type"] && params["cohort-type"] == "temp"}">
				plotTempPlots();
			</g:if>
			
			<g:elseif test="${params["cohort-name"]}">
				plotSavedPlots();
			</g:elseif>
		</g:if>
		
	});
	
	function setSurvivalPhenotype() {
		var survivalParamsForm = document.forms[0];
		$.ajax("<g:createLink controller="km" action="setSurvivalPhenotype" />",{ data: $(survivalParamsForm).serialize(), success:function(data) {
				$("#set-survival-params-modal").modal('hide');
				drawChart();		
			}		
		});
	}
	
	function plotTempPlots() {
		var cohortSelectionform = document.forms[1];
		$(cohortSelectionform).find("input[name=cohort-type]").val("temp");
		checkSurvivalAndPlot();	
		return false;
	}
	
	function plotSavedPlots() {
		var cohortSelectionform = document.forms[1];
		$(cohortSelectionform).find("input[name=cohort-type]").val("saved");
		checkSurvivalAndPlot();		
		return false;
	}
	
	function drawChart() {
		var survivalParamsForm = document.forms[0];
		var cohortSelectionform = document.forms[1];
		var datatable = new google.visualization.DataTable();
		datatable.addColumn("number", "survival");
		
		drawKMPlot($(survivalParamsForm).serialize()+"&"+$(cohortSelectionform).serialize());			
		return false;	
	}
	
	function checkSurvivalAndPlot() {
		$.ajax("<g:createLink controller="km" action="ajaxcheckSurvivalPhenotypeSet" />",{ 
			success:function(response) {
				if(response == "no") $("#set-survival-params-modal").modal('show');				
				else drawChart();
			}
		});	
	}
	
</g:javascript>
</head>
<body>

	<g:render template="/common/jfunctions" />
	<div class="modal fade bs-modal-sm in" id="set-survival-params-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-sm">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title" id="myModalLabel">Select your survival parameters</h4>
	      </div>
	      <div class="modal-body">
	      	<g:render template="/km/describe" />
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary" onclick="setSurvivalPhenotype();">Save changes</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->


	<div class="container">
		<div class="row">	
			<div class="col-md-12">&nbsp;</div>
			<p class="lead">Select cohorts and plot KM Plots
			<g:render template="/km/plot" />					
		</div>
	
	</div>
</body>
</html>