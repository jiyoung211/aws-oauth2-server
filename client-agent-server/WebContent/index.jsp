<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<%@ page import="com.multi.oauth2client.*" %>
<%@ page import="java.util.*" %>
    <%@ include file="/WEB-INF/include-header.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<script type="text/javascript" src="js/oauth2util.js"></script>
<script type="text/javascript">
$(function() {
	var param = {
		client_id : "e2e1c234-aa02-4bd2-ae75-329edac4c1bb",
		client_secret : "566d730e9a37fb1edad370f7f93ac1ce24b99644",
		redirect_uri : "http://<%= ip %>:<%= port %>/oauth2client_agentflow/callback.jsp",
		response_type : "token",
		scope : "calendar,personalinfo,readboard",
		state : window.createNonce(5)
	};
	
	sessionStorage.setItem('state', param.state);
	
	var authorizeUrl = "http://<%= ip %>:<%= port %>/oauth2provider/oauth2/auth";
	
	$("#authorize").click(function() {
		location.href=authorizeUrl + "?" + $.param(param);		
	});
});
</script>
</head>
<body>
<header>
	   <nav class="navbar navbar-inverse navbar-fixed-top">
	      <div class="container">
	        <div class="navbar-header">
	          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
	            <span class="sr-only">Toggle navigation</span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
	          <a class="navbar-brand" href="http://<%=ip%>:7070">OAuth</a>
	        </div>
	        <div id="navbar" class="collapse navbar-collapse">
	          <ul class="nav navbar-nav">
	            <li class="active"><a href="#">Home</a></li>
	            <li><a href="#about">About</a></li>
	            <li><a href="#contact">Contact</a></li>
	          </ul>
	        </div>
	      </div>
	    </nav>
    </header>
    <!-- Begin page content -->
    <main role="main" class="container">
	    <br/>
		<p>Client ID : e2e1c234-aa02-4bd2-ae75-329edac4c1bb<br>                                       </p>
		<p>Client Secret : 566d730e9a37fb1edad370f7f93ac1ce24b99644<br>                               </p>
		<p>Client Name : UserAgent Test App<br>                                                       </p>
		<p>Description : UserAgent Test App 입니다.<br>                                                 </p>
		<p>Client Type : M<br>                                                                        </p>
		<p>Client URL : http://<%= ip %>:<%= port %><br>                                              </p>
		<p>Redirect URI : http://<%= ip %>:<%= port %>/oauth2client_agentflow/callback.jsp<br>        </p>
		<p>Scope : calendar,personalinfo,readboard<br>                                                </p>
		<p>Registration Date : Tue Aug 06 19:23:41 KST 2013<br><br>                                   </p>
		<button id="authorize">Authorize!!</button>
	</main>
</body>
</html>