<g:javascript>

	$.fn.showOrHide = function(toggledElement) { //toggledElement is a DOM element or jQuery object
		var self = $(this);
		var plusMinus = jQuery(document.createElement("span"));
		plusMinus.html("[+]&nbsp;");
		var element = jQuery(toggledElement);
		var text = self.html();
		self.prepend(plusMinus);
		self.attr("class","clickSpan");
		
		var toggleFunction = function() {
			if(plusMinus.html().indexOf("+") > -1) {
				element.show();
				var replaceValue = plusMinus.html().replace("+","-");
				plusMinus.html(replaceValue);
			}
			
			else if(plusMinus.html().indexOf("-") > -1){
				element.hide();
				var replaceValue = plusMinus.html().replace("-","+");
				plusMinus.html(replaceValue);
			}
		};
		self.click(toggleFunction);		
		
		return self;
	};

	function createLoadingDialog(loadUrl) {
		$('#pleaseWaitDialog').on('hidden.bs.modal', function (e) {
		  window.location = loadUrl;
		})
		$("#pleaseWaitDialog").modal('show');
	}
	
	function drawKMPlot(params, config) {
		$("#pleaseWaitDialog").modal('show');
		var datatable = new google.visualization.DataTable();
		datatable.addColumn("number", "survival");
		
		$.ajax("<g:createLink controller="km" action="ajaxGetKmPoints" />", {dataType:"json", data: params,
			success:function(kmdata) {
				var count = 0;
				for(var cohort_name in kmdata.kmpoints) {
					datatable.addColumn("number", "Series "+cohort_name);
					datatable.addColumn({type:'string', role:'annotation'});
					count++;
				}
				count--;
				
				var i = 0;
				for(var cohort_name in kmdata.kmpoints) {
					var cohort = kmdata.kmpoints[cohort_name];
					for(var j = 0; j < cohort.length; j++) {
						var point = cohort[j];
						var row = new Array((2 * count + 3));
						row[0] = point.x;
						row[i + 1] = point.y;
						var censored = point.censored;
						row[i + 2] = (censored > 0) ? " " : null;
						datatable.addRow(row);
					}
					i += 2;
				}
				
				var options = {
		          title: (config != null && config.title != null) ? config.title : 'KM Plot',
		          legend: { position: 'top', alignment:'center'},
		          hAxis: {title:'Survival in Months', allowContainerBoundaryTextCufoff: false},
		          axisTitlesPosition: 'out',
		          vAxis: {title: 'Probability of Event'},
		          chartArea: {width: '80%', height: '80%'},
		          explorer: {actions:['dragToZoom', 'rightClickToReset']},
		          aggregationTarget: 'category'
		        };
				
				if(kmdata.pValue != null) options["title"] += ", p Value: "+kmdata.pValue;
				
		        var chart = new google.visualization.LineChart(document.getElementById('ChartDiv'));
		        chart.clearChart();
		        chart.draw(datatable, options);
		        $("#pleaseWaitDialog").modal('hide');
			}			
		});		
	}

</g:javascript>