<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Phenotype File</title>
<g:javascript>
	$(document).ready(function() {
		$("#draggable-headers").find("span").draggable({
			appendTo: "body",
			helper: "clone"		
		});
		
		$("#droppable-sample").droppable({
			drop: function( event, ui ) {
				$( this ).find( "span" ).remove();				
				$( "<span class=\"btn btn-default\">"+ui.draggable.text()+"&nbsp;&nbsp;<button type=\"button\" class=\"close\" onclick=\"$(this).parent().html('Sample column')\" aria-hidden=\"true\">&times;</button></span>" ).appendTo( this );
			}		
		});
		
		$("#droppable-survival").droppable({
			drop: function( event, ui ) {
				var defaultSpan = $( this ).find( "span" );
				var defaultSpanText = defaultSpan.html();
				defaultSpan.remove();				
				$( "<span class=\"btn btn-default\">"+ui.draggable.text()+"&nbsp;&nbsp;<button type=\"button\" class=\"close\" onclick=\"$(this).parent().html('Sample survival')\" aria-hidden=\"true\">&times;</button></span>" ).appendTo( this );
			}		
		});
	
	});

	function process(form) {
		var sample = $("input[name='column-sample']");
		var survival = $("input[name='column-survival']");
		sample.val(sample.siblings('span')[0].childNodes[0].nodeValue.trim());
		survival.val(survival.siblings('span')[0].childNodes[0].nodeValue.trim());
		//alert(sample.val());
		//alert(survival.val());
		return true;
	}

</g:javascript>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">Here are the headers in your file</p>
  			<p>Drag and drop columns from left to the right so as we know how to read your phenotype file</p>
  		</div>
  	</div>
  	
  	<div class="row">
  		<div class="col-md-3">
			<div class="btn-group-vertical" id="draggable-headers">
				<g:each in="${headers}">
					<span class="btn btn-default">${it}</span>
				</g:each>								
			</div>
		</div>
		<div class="col-md-1" style="height: 200px; padding-top: 100px"><img src="${resource(dir: 'images', file: 'arrow_full_right_48x48.png')}" /></div>
		
		<g:form controller="phenotype" action="setDataColumns" onsubmit="return process(this);">		
			<div class="col-md-offset-1 col-md-3">			
				<div class="btn-group-vertical" >					
					<div class="panel panel-default">
					  <div class="panel-heading"><h3 class="panel-title">Drop column representing sample here</h3></div>
					  <div class="panel-body" id="droppable-sample">
					    <span class="btn btn-default">Sample column</span>
					    <input type="hidden" name="column-sample" value="">
					  </div>
					</div>				
				</div>
							
				<div class="btn-group-vertical" >
					<div class="panel panel-default">
					  <div class="panel-heading"><h3 class="panel-title">Drop endpoint column here</h3></div>
					  <div class="panel-body" id="droppable-survival">
					    <span class="btn btn-default">Survival column</span>
					    <input type="hidden" name="column-survival" value="">
					  </div>
					</div>				
				</div>	
				
				<input class="btn btn-primary" type="submit" value="Submit">									
				
			</div>		
		</g:form>
		
  	</div>
	  
  </div>

</body>
</html>