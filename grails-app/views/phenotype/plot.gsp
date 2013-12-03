<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>KM Plot</title>
<g:javascript>
	$(document).ready(function() {
	
		var canvas = document.getElementsByTagName('canvas')[0];
		var ctx = canvas.getContext('2d');	
		var data = [];
		var data1 = [];
		data1.push(new kmpoint(30, 1.0));
		data1.push(new kmpoint(65, 0.82));
		data1.push(new kmpoint(130, 0.55));
		data1.push(new kmpoint(200, 0.4));
		
		<g:each in="${session.kmpoints}">
			data.push(new kmpoint(${it.x}, ${it.y}));
		</g:each> 
		
		var kmplot = new KMPlot(canvas);
		kmplot.xlabel = "Survival in months";
		kmplot.ylabel = "Probability of event";
		kmplot.addSeries(data, "series 1");
		kmplot.plot();
		kmplot.legend();
	
	});
</g:javascript>
</head>
<body>
	<div class="container">
		<div class="row">
			<p class="lead">KM Plot</p>
			<div class="canvas-wrap">
				<!-- <menu type="context" id="mymenu">
					<menuitem label="Refresh Post" onclick="window.location.reload();"></menuitem>
				</menu> -->
				<canvas id="myCanvas" width="500" height="500" style="background-color:#FFFFFF;" contextmenu="mymenu"></canvas>
				<img src="" id="canvasImg"/>
			
			</div>  		
		</div>
	
	</div>
</body>
</html>