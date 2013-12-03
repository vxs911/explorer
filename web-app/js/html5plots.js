function KMPlot(canvas) {

	var MARGIN_X = 50;
	var MARGIN_Y = 50;
	var PADDING_X = 20;
	var PADDING_Y = 20;
	var INC_X = 10;
	var INC_Y = 0.05;
	var ORIGIN_X = MARGIN_X;
	var ORIGIN_Y = canvas.height - MARGIN_Y;
	
	this.canvas = canvas;
	this.MARGIN_X = MARGIN_X;
	this.MARGIN_Y = MARGIN_Y;
	this.PADDING_X = PADDING_X;
	this.PADDING_Y = PADDING_Y;
	this.INC_X = INC_X;
	this.INC_Y = INC_Y;
	this.ORIGIN_X = ORIGIN_X;
	this.ORIGIN_Y = ORIGIN_Y;
	this.xmax = 0;
	this.xmin = 0;
	this.ymax = 1.0;
	this.ymin = 0.0;
	this.xlabel = "Overall Survival";
	this.ylabel = "Cumulative Survival";
	this.series = new Array();
	this.getX = getX;
	this.getY = getY;
	this.drawAxes = drawAxes;
	this.addSeries = addSeries;
	this.calculateParameters = calculateParameters;
	this.plot = plot;
	this.label = label;
	this.legend = legend;
	this.colors = ["black", "purple", "red", "#99CCFF"];
	
	function getX(x) {
		var coordinate = Math.floor(this.ORIGIN_X + this.PADDING_X + this.SCALE_X * x) + 0.5;
		return coordinate;
	}
	
	function getY(y) {
		var coordinate = Math.floor(this.ORIGIN_Y - this.PADDING_Y - this.SCALE_Y * y) + 0.5;
		return coordinate;		
	}
	
	function label(labelText) {
		var canvas = this.canvas;
		var ctx = canvas.getContext('2d');
		var LABEL_X_COORD = Math.ceil(canvas.width * 0.75);
		var LABEL_Y_COORD = Math.ceil(canvas.height * 0.1);
		ctx.font = '20pt Calibri';
		ctx.fillStyle = '#000';
		ctx.fillText(labelText, LABEL_X_COORD, LABEL_Y_COORD); 
	}
	
	function legend() {
		var canvas = this.canvas;
		var ctx = canvas.getContext('2d');
		var LEGEND_X_COORD = Math.ceil(canvas.width * 0.75);
		var LEGEND_Y_COORD = Math.ceil(canvas.height * 0.1);
		ctx.font = '9pt Arial';
		for(var i = 0; i < this.series.length; i++) {
			ctx.beginPath();
			ctx.fillStyle = this.series[i].color;
			ctx.fillRect(LEGEND_X_COORD, LEGEND_Y_COORD - 5, 10, 10);
			ctx.stroke();
			
			ctx.beginPath();
			ctx.textAlign = "start";
			ctx.fillStyle = "#000";
			ctx.textBaseline = "middle";
			ctx.fillText(this.series[i].name, LEGEND_X_COORD + 15, LEGEND_Y_COORD);
			var lineHeight = ctx.measureText('M').width;
			ctx.stroke();
			LEGEND_Y_COORD += (lineHeight + 10);
		}
	}
	
	function calculateParameters() {
		var max = new Array();
		for(var i = 0; i < this.series.length; i++) {
			var data = this.series[i].data;
			max.push(data[data.length - 1].days);
		}
		
		max.sort();
		this.xmax = max[max.length - 1];
		var order_of_magnitude = Math.floor(Math.LOG10E * Math.log(this.xmax));
		this.INC_X = Math.ceil(this.xmax / Math.pow(10, order_of_magnitude)) * Math.pow(10, order_of_magnitude - 1);
		//this.INC_X *= Math.pow(10, order_of_magnitude - 1);
		this.xmax = this.INC_X * 10;
		this.SCALE_X = (canvas.width - MARGIN_X - 2 * PADDING_X) / (this.xmax - this.xmin);
		this.SCALE_Y = (canvas.height - MARGIN_Y - 2 * PADDING_Y) / (this.ymax - this.ymin);
	}
	
	function addSeries(data, name) {
		var series = new Object();
		series.data = data;
		series.name = name;
		series.color = this.colors.pop();
		this.series.push(series);
	}
	
	function plot() {
		var canvas = this.canvas;
		var ctx = canvas.getContext('2d');
		ctx.beginPath();
		ctx.fillStyle = "#FFFFFF";
		ctx.fillRect(0, 0, canvas.width, canvas.height);
		ctx.stroke();
		
		this.calculateParameters();
		this.drawAxes();
		ctx.lineWidth = 2;
		
		for(var i = 0; i < this.series.length; i++) {
			ctx.beginPath();
			ctx.strokeStyle = this.series[i].color;
			var data = this.series[i].data;
			
			for(var j = 0; j < data.length; j++) {
			
				if(j == 0) {
					ctx.moveTo(this.getX(0), this.getY(1.0));
				}
	
				ctx.lineTo(this.getX(data[j].days), this.getY(data[j].probability));
				if(data[j+1]) {
					ctx.lineTo(this.getX(data[j].days), this.getY(data[j+1].probability));
				}
				
			
			}
			ctx.stroke();			
		}		
	}
}

function kmpoint(x, y) {
	this.days = x;
	this.probability = y;
}
	
function drawAxes() {
	var canvas = this.canvas;
	var ctx = canvas.getContext('2d');
	ctx.beginPath();
	ctx.moveTo(this.ORIGIN_X + 0.5, this.ORIGIN_Y + 0.5);
	ctx.lineTo(canvas.width + 0.5, this.ORIGIN_Y + 0.5);
	ctx.lineTo(canvas.width - 5.5, this.ORIGIN_Y + 5.5);
	ctx.moveTo(canvas.width + 0.5, this.ORIGIN_Y + 0.5);
	ctx.lineTo(canvas.width - 5.5, this.ORIGIN_Y - 5.5);
	
	ctx.moveTo(this.ORIGIN_X + 0.5, this.ORIGIN_Y + 0.5);
	ctx.lineTo(this.ORIGIN_X + 0.5, 0.5);
	ctx.lineTo(this.ORIGIN_X + 5.5, 5.5);
	ctx.moveTo(this.ORIGIN_X + 0.5, 0.5);
	ctx.lineTo(this.ORIGIN_X - 5.5, 5.5);	
	ctx.stroke();
	
	ctx.textAlign = "center";
	var NUM_X_TICKS = (this.xmax - this.xmin) / this.INC_X + 1;
	var NUM_Y_TICKS = (this.ymax - this.ymin) / this.INC_Y + 1;	
	ctx.font = '10pt Arial';
	ctx.fillStyle = 'black';
	
	ctx.beginPath();	
	for(var i = 0; i < NUM_X_TICKS; i++) {
		var TICK_X = this.getX(this.INC_X * i);
		var TICK_Y0 = this.ORIGIN_Y;
		var TICK_Y1 = TICK_Y0 + 5;
		var coordinate = this.INC_X * i; 
		ctx.moveTo(TICK_X, TICK_Y0);
		ctx.lineTo(TICK_X, TICK_Y1);
		ctx.fillText(""+coordinate, TICK_X, TICK_Y0 + 14.5);
	}
	ctx.stroke();
	
	for(var i = 0; i < NUM_Y_TICKS; i++) {
		ctx.beginPath();
		ctx.strokeStyle = "#000";
		var TICK_Y = this.getY(this.INC_Y * i);
		var TICK_X0 = this.ORIGIN_X;
		var TICK_X1 = TICK_X0 - 5;
		var coordinate = (this.INC_Y * i).toFixed(2);
		ctx.moveTo(TICK_X0, TICK_Y);
		ctx.lineTo(TICK_X1, TICK_Y);
		ctx.stroke();
		
		ctx.beginPath();
		ctx.moveTo(TICK_X0, TICK_Y);
		ctx.lineTo(canvas.width, TICK_Y);
		ctx.strokeStyle = "#F8F8F8";
		ctx.stroke();
		
		ctx.fillText(""+coordinate, TICK_X1 - 14.5, TICK_Y);
	}
	var X_LABEL_XCOORD = (canvas.width - this.MARGIN_X) / 2 + this.MARGIN_X;
	var Y_LABEL_YCOORD = (canvas.height - this.MARGIN_Y) / 2 + this.MARGIN_Y;
	ctx.fillText(this.xlabel, X_LABEL_XCOORD, this.ORIGIN_Y + 30);
	ctx.save();
	ctx.translate(0, canvas.height);
	ctx.rotate(-Math.PI/2);
	ctx.fillText(this.ylabel, Y_LABEL_YCOORD, this.ORIGIN_X - 40);
	ctx.restore();
	ctx.stroke();
}
