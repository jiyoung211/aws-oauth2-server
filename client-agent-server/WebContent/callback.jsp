<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
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
<h2>다음의 리소스 서버로 요청합니다.</h2>
http://localhost:8000/oauth2provider/resource/myinfo.do
<br>
 <br>
 console log를 열어서 수신한 데이터를 확인합니다. 
</body>
</html>