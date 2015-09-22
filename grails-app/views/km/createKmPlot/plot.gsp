<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>KM Plot</title>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<g:javascript>
	google.load("visualization", "1", {packages:["corechart"]});
	
	$(document).ready(function() {
		google.setOnLoadCallback(drawRegularChart);
		$(".selectpicker").selectpicker();
		$("#set-survival-params-modal").modal('show');
	});
	
	function drawRegularChart() {
		var datatable = new google.visualization.DataTable();
		datatable.addColumn("number", "survival");
		
		<g:if test="${kmpoints}">
			<g:each in="${kmpoints.keySet()}" var="cohortName">
				datatable.addColumn("number", "Series ${cohortName}");
			</g:each>
			
			<g:each in="${kmpoints.keySet()}" var="cohortName" status="i">
				<g:each in="${kmpoints.get(cohortName)}" var="point">
					var row = new Array(${kmpoints.keySet().size()+1});
					row[0] = ${point.x};
					row[${i+1}] = ${point.y};
					datatable.addRow(row);
				</g:each>
			</g:each>
		</g:if>
		
		drawChart(datatable);		
	}
	
	function drawAjaxChart(form) {
		var datatable = new google.visualization.DataTable();
		datatable.addColumn("number", "survival");	
		$.ajax("<g:createLink controller="km" action="ajaxGetKmPoints" />", {dataType:"json", data: $(form).serialize(),
			success:function(cohorts) {
				var count = 0;
				for(var cohort_name in cohorts) {
					datatable.addColumn("number", "Series "+cohort_name);
					count++;
				}
				
				var i = 0;
				for(var cohort_name in cohorts) {
					var cohort = cohorts[cohort_name];
					i++;
					for(var j = 0; j < cohort.length; j++) {
						var point = cohort[j];
						var row = new Array(count + 1);
						row[0] = point.x;
						row[i] = point.y;
						datatable.addRow(row);
					}
					
				}
				drawChart(datatable);
			}
			
			});
			
		return false;	
	}	
	
	function drawChart(datatable) {	
		var options = {
          title: 'KM Plot',
          legend: { position: 'top' },
          hAxis: {title:'Survival in Months'},
          vAxis: {title: 'Probability of Event'}
        };
		
        var chart = new google.visualization.LineChart(document.getElementById('ChartDiv'));
        chart.clearChart();
        chart.draw(datatable, options);
	}
	
	function setSurvivalPhenotype() {
		var form = $("form[name=set-survival-form]");
		$.ajax("<g:createLink controller="km" action="setSurvivalPhenotype" />",{ dataType:"json", data: $(form).serialize(),
			success:function(data) {
				$("#set-survival-params-modal").modal('hide');			
			}
		
		});
	}
</g:javascript>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-5">
				<g:render template="/km/plot" /> 
			</div>		
		</div>
	
	</div>
</body>
</html>