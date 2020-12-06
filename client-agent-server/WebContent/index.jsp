<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<%@ page import="com.multi.oauth2client.*" %>
<%@ page import="java.util.*" %>
<%
	String ip = request.getServerName() ;
	int port = request.getServerPort() ;
%>
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
Client ID : e2e1c234-aa02-4bd2-ae75-329edac4c1bb<br>
Client Secret : 566d730e9a37fb1edad370f7f93ac1ce24b99644<br>
Client Name : UserAgent Test App<br>
Description : UserAgent Test App �Դϴ�.<br>
Client Type : M<br>
Client URL : http://<%= ip %>:<%= port %><br>
Redirect URI : http://<%= ip %>:<%= port %>/oauth2client_agentflow/callback.jsp<br>
Scope : calendar,personalinfo,readboard<br>
Registration Date : Tue Aug 06 19:23:41 KST 2013<br><br>
<button id="authorize">Authorize!!</button>
</body>
</html>