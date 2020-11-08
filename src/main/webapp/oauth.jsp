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
	<script src="https://code.jquery.com/jquery-3.5.0.js"></script>
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
	 	        url : 'https://kauth.kakao.com/oauth/token', //'/test.jsp,
	 	        dataType : 'json',
	 	        data : $("#tokenForm").serialize() ,
	 	        beforeSend : function(xhr){
	 	            xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	 	        },
	 	        error: function(xhr, status, error){
		 	       alert("error");
	 	           alert(error);
	 	        },
	 	        success : function(msg){
		 	       alert("success");
	 	           alert("access_token " + msg.access_token);
	 	           window.location.href = "kakaoMe.jsp?access_token="+msg.access_token;
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
	
	<div class="row ">
		<div class=" col-md-12">
		<form id="tokenForm" name="tokenForm" action="https://kauth.kakao.com/oauth/token" method="POST" onSubmit="return false;">
			<table class="table">
			<thead><h1>사용자 토큰 받기</h1></thead>
    		<tbody>
    			<tr>
		           <td><label>grant_type</label></td>
		           <td><input type="text" class="form-control" name="grant_type" id="grant_type" value="authorization_code" /></td>
		       	</tr>
		       	<tr>
		           <td><label>client_id</label></td>
		           <td><input type="text" class="form-control" name="client_id" id="client_id" value="8f095856da45f0270ab3ec45fe0057ce" /></td>
		       	</tr>
		       	<tr>
		           <td><label>redirect_uri</label></td>
		           <td><input type="text" class="form-control" name="redirect_uri" id="redirect_uri" value="http://localhost:7070/oauth" /></td>
		       	</tr>
		       	<tr>
		           <td><label>code</label></td>
		           <td><input type="text" class="form-control" name="code" id="code" value="<%= request.getParameter("code")%>" /></td>
		       	</tr>
		       	<tr>
		           <td><label>client_secret</label></td>
		           <td><input type="text" class="form-control" name="client_secret" id="client_secret" placeholder="N4I5IHXZpLCL95wfvIqIdnmXfH9vQGAD" /></td>
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