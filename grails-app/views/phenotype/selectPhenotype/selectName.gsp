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
		//var sample = $("input[name='column-sample']");
		var survival = $("input[name='column-survival']");
		//sample.val(sample.siblings('span')[0].childNodes[0].nodeValue.trim());
		survival.val(survival.siblings('span')[0].childNodes[0].nodeValue.trim());
		//alert(sample.val());
		alert(survival.val());
		return true;
	}
	
	function toggle(element) {
		var spans = $(".btn.input");
		//alert(spans.length);
		for(var i = 0; i < spans.length; i++) {
			$(spans[i]).removeClass("btn-primary");
			$(spans[i]).addClass("btn-default");
		}
		$(element).addClass("btn-primary");
		$(element).removeClass("btn-default");
		$("input[name='phenotype']").val($(element).html());
	}

</g:javascript>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">&nbsp;</div>
  		<div class="col-xs-12 col-md-8">
  			<p class="lead">Here are the different phenotypes we have found in your data</p>
  			<p>Click on the one you are interested in</p>
  		</div>
  	</div>
  	
  	<div class="row">
  		<div class="col-md-3">
  			<g:form controller="phenotype" action="selectPhenotype" method="get">
				<div class="btn-group-horizontal" id="draggable-headers">
					<g:each in="${phenotypeNames}">
						<span class="btn btn-default input" onclick="toggle(this);">${it}</span>
					</g:each>								
				</div>
				<br>
				<input type="hidden" name="phenotype" value="">
				<g:submitButton name="submit" value="Continue" class="btn btn-primary" />
			</g:form>
		</div>		
  	</div>
	  
  </div>

</body>
</html>