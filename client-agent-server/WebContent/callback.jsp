<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<%@ page import="com.multi.oauth2client.*" %>
<%@ page import="java.util.*" %>
    <%@ include file="/WEB-INF/include-header.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>111</title>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<script type="text/javascript" src="js/oauth2util.js"></script>
<script type="text/javascript">
$(function() {
	var queryStr = location.href.split("#")[1];
	//생성한 토큰은 webapp일 경우는 SessionStorage와 같은 곳에 저장한다.
	//Mobile은 안전한 저장소에 저장한다.
	var token = getObjectFromParam(queryStr);
	var prevState = sessionStorage.getItem("state");
	sessionStorage.removeItem("state");
	
	if (token.state != prevState) {
		console.log("CSRF 공격이 의심됨");
	} 
	
	console.log(token);
	
	var resourceUrl = "http://<%= ip %>:<%= port %>/oauth2provider/resource/myinfo.do";
	$.ajax({
		type : "GET",
		url : resourceUrl,
		headers : { "Authorization" : "Bearer " + token.access_token },
		success : function(json) {
			console.log(json);
		}
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
		<h2>다음의 리소스 서버로 요청합니다.</h2>
		http://<%=ip%>:<%=port%>/resource/myinfo.do
		<br>
		 <br>
		 console log를 열어서 수신한 데이터를 확인합니다. 
	 </main>
</body>
</html>