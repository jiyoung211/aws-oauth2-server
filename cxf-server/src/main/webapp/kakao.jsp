<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String basePath = request.getContextPath();
if (!basePath.endsWith("/")) {
    basePath += "/";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Kakao API</title>
<script src="/js/kakao.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
	<link href="/starter-template.css" rel="stylesheet" /> 
<script >
	Kakao.init('4c4cece2bf6216917eff4c49bc6da926');
	console.log(Kakao.isInitialized());
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
          <a class="navbar-brand" href="/">OAuth</a>
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

	<div class="container">
	<!-- 
	<div class="row">
		<div class=" col-md-12">
			 <input type="image" name="button" src="/img/kakao_login_medium_narrow.png">
		</div>
	</div>
	 -->
	
	<div class="row ">
		<div class=" col-md-12">
		<form name="loginForm" action="https://kauth.kakao.com/oauth/authorize" method="GET">
			<table class="table">
			<thead><h1>카카오계정 로그인</h1></thead>
			<tbody>
				<tr>
				   <td><label>client_id</label></td>
		           <td><input type="text" class="form-control" name="client_id" id="client_id" value="8f095856da45f0270ab3ec45fe0057ce" /></td>
		       	<tr>
				<tr>
				   <td><label>redirect_uri</label></td>
		           <td><input type="text" class="form-control" name="redirect_uri" id="redirect_uri" value="http://localhost:7070/oauth" /></td>
		       	<tr>
				<tr>
				   <td><label>response_type</label></td>
		           <td><input type="text" class="form-control" name="response_type" id="response_type" value="code" /></td>
		       	<tr>
		       	<tr>
				   <td><label>state</label></td>
		           <td><input type="text" class="form-control" name="state" id="state" placeholder="Cross-site Request Forgery 공격을 보호하기 위해 활용 가능" /></td>
		       	<tr>
				<!-- <tr>
				   <td><label>이메일 또는 전화번호</label></td>
		           <td><input type="text" class="form-control" name="id" id="id" value="" /></td>
		       	<tr>
		           <td><label>비밀번호</label></td>
		           <td><input type="text" class="form-control" name="pw" id="pw" value="pw" /></td>
		       	</tr> -->
		       	<tr>
		           <td><input type="image" name="button" src="/img/kakao_login_medium_narrow.png" onclick="$('#loginForm').val();"></td>
		       	</tr>
				</tbody>
			</table>
		</form>
		</div>
	</div>
	
	
	
	
</div>
</body>
</html>