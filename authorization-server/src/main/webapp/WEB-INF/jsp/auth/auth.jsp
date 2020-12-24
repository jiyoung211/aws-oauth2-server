<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="net.oauth.v2.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.multi.oauth2.provider.vo.*" %>
<%
	boolean isloggined = (Boolean)request.getAttribute("isloginned");
	RequestAuthVO rVO = (RequestAuthVO)request.getAttribute("requestAuthVO");
	ClientVO cVO = (ClientVO)request.getAttribute("clientVO");
	//csv를 배열로...
	String[] scopes = rVO.getScope().split(",");
	String currentUrl = "auth?" + request.getQueryString();
%>
<%@ include file="/WEB-INF/include-header.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Authorization</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script type="text/javascript">
function allow() {
	document.getElementById("f1").submit();
}

function deny() {
	document.getElementById("isallow").value = "false";
	document.getElementById("f1").submit();
}
</script>
</head>
<body>
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
			<h1>Consumer 승인 페이지</h1>
			<hr>
				<b>승인하려는 클라이언트 : <%=cVO.getClient_name() %></b>
			<hr>
			<h3>사용하려는 권한</h3>
			<ul>
		<% for (int i=0; i < scopes.length; i++) { %>
				<li><%=OAuth2Scope.getScopeMsg(scopes[i]) %></li>		
		<% } %>
			</ul>
			<h1>위 권한을 승인하시겠습니까?</h1>
			<hr>
			<form id="f1" method="post" action="<%=currentUrl %>">
				<input type="hidden" id="isallow" name="isallow" value="true" />
		<% if (!isloggined) { 	%>
				계정 : <input type="text" id="userid" name="userid" value="gdhong" /><br>
				암호 : <input type="text" id="password" name="password" value="gdhong" /><br>
		<% } else { %>
				<h3>이미 로그인하셨습니다. 승인하시겠습니까?</h3>
		<% } %>
				<input type="button" value="승인" onclick="allow()" />
				<input type="button" value="거부" onclick="deny()" />
			</form>
		</main>
</body>
</html>