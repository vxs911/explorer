<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII"/>
<meta name="layout" content="main"/>
<title>Welcome to Outcomes Explorer!</title>
<g:javascript>
	$(document).ready(function(){
		$(".alert").hide();
		<g:if test="${flash.error}">
			$(".alert-danger").html("${flash.error}");
			$(".alert-danger").show();
		</g:if>
		
		<g:if test="${flash.message}">
			$(".alert-success").html("${flash.message}");
			$(".alert-success").show();		
		</g:if>

	});
	
	function deleteSession(element, dir) {
		bootbox.confirm("Are you sure you want to delete?", function(result) {
			if(result == true)
				$.ajax("<g:createLink controller="home" action="deleteSession" />"+"?dir="+dir, {
					success:function(){
						$(element).parents("tr").remove();
						var alertDiv = $.find(".alert.alert-success");
						$(alertDiv[0]).html("User session has been successfully deleted");
						$(alertDiv[0]).show();
					}
				});
		});
	}
	
	function showWait() {
		$("#pleaseWaitDialog").modal('show');
		return false;
	}
	
	function renameSession(element, dir) {
		bootbox.prompt("Enter a new name:", function(result){
			if(result != null)
				$.ajax("<g:createLink controller="home" action="renameSession" />"+"?dir="+dir+"&new_name="+result, {
					success:function(){
						var link = $(element).parents("tr").find(".identifier").html(result);			
						var alertDiv = $.find(".alert.alert-success");
						$(alertDiv[0]).html("User session has been successfully renamed");
						$(alertDiv[0]).show();
					}
				});		
		});
	}
	
</g:javascript>
</head>
<body>

<g:render template="/common/modals" />
<g:render template="/common/jfunctions" />
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">Rename Session</h4>
      </div>
      <div class="modal-body">
        <div class="form-group">
	       	<label>New Name: </label>
	       	<input type="text" class="form-control">       
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>

  <div class="container">
  	<div class="row">
  		<p>&nbsp;</p>
  		<div class="jumbotron">
  			<h2>Welcome to Outcomes Explorer!</h2>
  			<p>If this is your first time with this tool, click "Start new analysis" to start uploading your genotype and phenotype files for analysis.
  			Otherwise select from one of your previously saved files.
  			</p>
  		</div>
  		<div class="col-xs-12 col-md-8">
  			<div class="alert alert-success"></div>
  			<div class="alert alert-danger"></div>
  			<g:form role="form" controller="home" action="start" method="GET">
  				<p>Here is the list of previously saved files for analysis *:</p>
  				<div class="table-responsive">
  				<table class="table table-hover">
  					<tr class="active">
  						<th>Session name</th>
  						<th>Type</th>
  						<th>File types</th>
  						<th>Actions</th>
  					</tr>
  					<g:each in="${userFiles}">
  					<tr >
  						<td>
  							<g:if test="${session.dir != it.dir }">
  								<g:link controller="home" action="loadSession" class="identifier" mapping="loadSession" params="[dir:it.dir]" onclick="createLoadingDialog(this.href);">${it.identifier}</g:link>
  							</g:if>
  							<g:else>
  								<g:if test="${session.reader}">
  									${it.identifier}&nbsp;<span class="glyphicon glyphicon-ok"></span>
  								</g:if>
  								<g:else>
  									<g:link controller="phenotype" action="describe" class="identifier">${it.identifier}</g:link>
  									<span style="font-size: 9px; color: blue; font-style:italic">[?]</span>
  								</g:else>
  							</g:else>				
  						</td>
  						<td>${it.type}
  						<td>
  							<span class="label label-success">G</span>
  							<span class="label label-warning">P</span>
  						</td>
  						<td>
						  <div class="btn-group">
						    <a href="#" data-toggle="dropdown">
						      Select
						      <span class="caret"></span>
						    </a>
						    <ul class="dropdown-menu">
						      <li><a href="#" onclick="deleteSession(this, '${it.dir}');">Delete</a></li>
						      <li><a href="#" onclick="renameSession(this, '${it.dir}');" >Rename</a></li>
						    </ul>
						  </div>		  
						
					</td>
  					</tr>
  					</g:each>
  				</table>
  				</div>
  				<p class="help-block">
  					<small>
	  					<span class="label label-success">G</span>&nbsp;Genotype files
	  					<span class="label label-warning">P</span>&nbsp;Phenotype files
  					</small>
  				</p>
  				<p class="help-block"><small>*You can click on any of the above to reload an analysis session or click below to 
  				upload files</small></p>
  				<g:link controller="home" action="start" class="btn btn-primary">Start new analysis</g:link>
  			</g:form>
  		</div>
  	
  	</div> <!-- row -->
	  
  </div> <!-- container -->
</body>
</html>