<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<g:javascript src="jquery-1.10.2.min.js" />
		<g:javascript src="jquery-ui.js" />
		<g:javascript src="bootstrap.min.js" />
		<g:javascript src="bootstrap-select.min.js" />
		<g:javascript src="bootbox.min.js" />
		<g:javascript src="html5plots.js" />
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-theme.min.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-select.min.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'common.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css/cupertino', file: 'jquery-ui-1.10.3.custom.css')}" type="text/css">
		<link rel="apple-touch-icon" href="${resource(dir: 'img', file: 'glyphicons-halflings-white.png')}">
		<link rel="apple-touch-icon" href="${resource(dir: 'img', file: 'glyphicons-halflings.png')}">
		<style type="text/css">
		
			/* Sticky footer styles
			-------------------------------------------------- */
			
			html,
			body {
			  height: 100%;
			  /* The html and body elements cannot have any padding or margin. */
			}
			
			/* Wrapper for page content to push down footer */
			#wrap {
			  min-height: 100%;
			  height: auto;
			  /* Negative indent footer by its height */
			  margin: 0 auto -120px;
			  /* Pad bottom by footer height */
			  padding: 0 0 60px;
			}
			
			/* Set the fixed height of the footer here */
			#footer {
			  height: 80px;
			  background-color: #18bc9c;
			  margin-top:40px;
			}
			
			
			/* Custom page CSS
			-------------------------------------------------- */
			/* Not required for template or sticky footer method. */
			
			#wrap > .container {
			  padding: 20px 15px 30px;
			}
			
			#wrap > .container > .row {
				padding-top:20px;
			}
			.container .text-muted {
			  margin: 20px 0;
			}
			
			#footer > .container {
			padding-top: 20px;
			  padding-left: 15px;
			  padding-right: 15px;
			}
			
			code {
			  font-size: 80%;
			}

		</style>
		<g:javascript>
			<sec:ifLoggedIn> var myPing = setInterval(query, 10 * 1000);</sec:ifLoggedIn>
			$("#navbar-messages").popover({html:true});
			
			$(document).ready(function(){
				var currPage = "${controllerName}";
				$("#main-navbar .active").removeClass("active");
				$("#navbar-"+currPage).addClass("active");
				$("#navbar-cohorts").click(function(){
					<g:if test="${!session["reader"]}">
						bootbox.alert("Please select an existing session or upload files to create a new session");
						return false;
					</g:if>
				});
				
				$("#navbar-km").click(function(){
					<g:if test="${!session["phenotypeFileReader"]}">
						bootbox.alert("Please select an existing session or upload files to create a new session");
						return false;
					</g:if>
				});				

			});
			
			function query() {
				$.ajax("<g:createLink controller="messages" action="query" />", { dataType:"json",
					success:function(messages) {
						//alert("number of messages: "+messages.length);
						serverMessages = messages;
						showAllMessages(messages);
						$("#navbar-messages").find("span").html(messages.length);											
						//$(".alert.alert-success").html(data).show();
					}
				});
			}
			
			function showAllMessages(messages) {
				//alert("showing messages");
				var html = "Here are your messages: ";
				html += "&lt;ol&gt;";
				for(var i = 0; i < messages.length; i++) {
					html += "&lt;li&gt;"+messages[i].content+"&lt;/li&gt;";
				}
				html += "&lt;/ol&gt;";
				$("#navbar-messages").attr("data-content", html);
			}
			
			function acknowledge() {
				$.ajax("<g:createLink controller="home" action="acknowledge" />", { dataType:"text",
					success:function(data) {
						
					}
				});			
			}
		</g:javascript>
		<g:layoutHead/>
		<r:layoutResources />
	</head>
	<body>
    <!-- Wrap all page content here -->
    <div id="wrap">

      <!-- Fixed navbar -->
		    <div class="navbar navbar-inverse navbar-fixed-top" id="main-navbar">
		      <div class="container">
		        <div class="navbar-header">
		          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		          </button>
		          <a class="navbar-brand" href="#">Outcomes Explorer</a>
		        </div>
			   <sec:ifLoggedIn>
				<div class="collapse navbar-collapse">
			  		<ul class="nav navbar-nav">
			    		<li id="navbar-home"><g:link controller="home" action="index">Home</g:link></li>
			    		<li id="navbar-cohorts"><g:link controller="cohorts" action="index">Cohort Builder</g:link></li>
			    		<li id="navbar-km" class="dropdown">
			    			<a href="#" class="dropdown-toggle" data-toggle="dropdown">KM Plots<b class="caret"></b></a>
			    			<ul class="dropdown-menu">
			    				<li><g:link controller="km" action="explore">Quickstart</g:link></li>
			    				<li><g:link controller="km" action="index">Cohort based</g:link></li>
			    			</ul>
			    		</li>
			    		<li id="navbar-messages" data-container="body" data-toggle="popover" data-placement="bottom" data-content="No messages found" >
			    			<a href="#">Notifications <span class="badge">0</span></a>
			    		</li>
			  		</ul>
			  		<ul class="nav navbar-nav" style="float:right">
			  			<li class="dropdown">
			  				<a href="#" class="dropdown-toggle" data-toggle="dropdown"><sec:loggedInUserInfo field="username" /><b class="caret"></b></a>
			  				<ul class="dropdown-menu">
			  					<li><a href="#">Change Password</a></li>
			  					<li><a href="#">Admin</a></li>
			  				</ul>
			  			</li>
			  			<li><a href="#about">About</a></li>
						<li><a href="#contact">Contact</a></li>	
						<li><g:link controller="logout" action="index">Log Out</g:link></li>
			  		</ul>
				</div><!--/.navbar-collapse -->
			      </sec:ifLoggedIn>	        
		      </div>
		    </div>

			<!-- Begin page content -->
			<g:layoutBody/>
			</div>

		    <div id="footer">
		      <div class="container">
		      	<div class="row">
					<div class="col-md-4"></div>
					<div class="col-md-4"><img alt="" src="<g:resource dir="images" file="GU_logo.png" />"></div>
					<!-- <div class="col-md-4"><img alt="" src="<g:resource dir="images" file="gumc.png" />"></div>  -->
		      	</div>        
		      </div>
		    </div>
	<r:layoutResources/>
	</body>
</html>