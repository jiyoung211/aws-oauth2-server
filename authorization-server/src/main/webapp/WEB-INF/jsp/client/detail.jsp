<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.multi.oauth2.provider.vo.*" %>
<%@ include file="/WEB-INF/include-header.jsp"%>
<%
	ClientVO vo = (ClientVO)request.getAttribute("clientVO");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript">
window.onload = function() {
	document.getElementById("golist").onclick = function() {
		location.href="clientlist.do";
	};
};
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
		<h1>Client Detail</h1><hr>
		<div>
			Client ID : <%=vo.getClient_id() %><br>
			Client Secret : <%=vo.getClient_secret() %><br>
			Client Name : <%=vo.getClient_name() %><br>
			Description : <%=vo.getDescription() %><br>
			Client Type : <%=vo.getClient_type() %><br>
			Client URL : <%=vo.getClient_url() %><br>
			Redirect URI : <%=vo.getRedirect_uri() %><br>
			Scope : <%=vo.getScope() %><br>
			Registration Date : <%=vo.getRegdate() %><br><br>
		</div>
		<input type="button" id="golist" value="Go Client List">
	</main>
</body>
</html>