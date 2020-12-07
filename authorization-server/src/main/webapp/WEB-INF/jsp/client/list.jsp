<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.multi.oauth2.provider.vo.*" %>
<%
	List<ClientVO> list = (List<ClientVO>)request.getAttribute("list");
%>   
<%@ include file="/WEB-INF/include-header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
		<h1>Client App list</h1><hr>
		<a href="insertclient.do">Add Client!</a>
		<hr>
		<div>
	<% for (ClientVO vo : list) { %>
			<div style="border-bottom:solid 1px black; font-size:9pt; padding:10px 10px 10px 10px;">
				Client Name : <%=vo.getClient_name() %><br>
				Description : <%=vo.getDescription() %><br>
				Client Type : <%=vo.getClient_type() %><br>
				Client URL : <%=vo.getClient_url() %><br>
				Redirect URI : <%=vo.getRedirect_uri() %><br><br>
				<a href="detailclient.do?client_id=<%=vo.getClient_id() %>">View Detail</a>
				&nbsp; 
				<a href="deleteclient.do?client_id=<%=vo.getClient_id() %>">Delete Client</a>
			</div>
	<% } %>
		</div>
	</main>
</body>
</html>