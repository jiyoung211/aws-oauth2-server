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
	
	function callapi()
    {
    	 $.ajax({
 	        type : 'post',
 	        url : $("#kakaoLogoutUrl").val(),
 	        dataType : 'text',
 	        data : $("#logoutForm").serialize(),
 	        beforeSend : function(xhr){
 	            xhr.setRequestHeader("Authorization", "Bearer "+"<%= request.getParameter("access_token")%>" );
 	        },
 	        error: function(xhr, status, error){
 	            alert(error);
 	        },
 	        success : function(msg){
 	            alert(msg);
 	        }
     	 }); 
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
		<form id="logoutForm" name="logoutForm" action="https://kapi.kakao.com/v1/user/logout" method="POST" onSubmit="return false;">
			<table class="table">
			<thead><h1>카카오계정 로그아웃</h1></thead>
			<tbody>
				<tr>
		           <td><label>kakaoLogoutUrl</label></td>
		           <td><input type="text" class="form-control" name="kakaoLogoutUrl" id="kakaoLogoutUrl" value="https://kapi.kakao.com/v1/user/logout" /></td>
		       	</tr>
		       	<tr>
		           <td><label>Authorization</label></td>
		           <td><input type="text" class="form-control" name="Authorization" id="Authorization" value="Bearer <%= request.getParameter("access_token")%>" /></td>
		       	</tr>
		       	<tr>
		           <td><label>target_id</label></td>
		           <td><input type="text" class="form-control" name="target_id" id="target_id" value="Bearer <%= request.getParameter("access_token")%>" /></td>
		       	</tr>
		       	<tr>
		           <td><label>target_id_type</label></td>
		           <td><input type="text" class="form-control" name="target_id_type" id="target_id_type" value="user_id" /></td>
		       	</tr>
		       	<tr>
		           <td><input type="image" name="button" src="/img/kakao_login_medium_narrow.png" onclick="callapi();"></td>
		       	</tr>
				</tbody>
			</table>
		</form>
		</div>
	</div>
	
	
	
	
</div>
</body>
</html>