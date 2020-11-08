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
	<!-- <script src="//developers.kakao.com/sdk/js/kakao.min.js"></script> -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
	<link href="/starter-template.css" rel="stylesheet" /> 
<script >
	Kakao.init('4c4cece2bf6216917eff4c49bc6da926');
	console.log(Kakao.isInitialized());
	
	function callapi()
    {
<%--     	 $.ajax({
 	        type : 'get',
 	        url : $("#kakaoApiUrl").val(),
 	        dataType : 'json',
// 	        data : $("#requestForm").serialize(),
 	        beforeSend : function(xhr){
 	            xhr.setRequestHeader("Authorization", "Bearer "+"<%= request.getParameter("access_token")%>" );
 	        },
 	        error: function(xhr, status, error){
 	            alert(error);
 	        },
 	        success : function(msg){
 	            console.log(msg);
 	        }
     	 }); 
    	  --%>
    	  
    	 Kakao.API.request({
    		  url: '/v2/user/me',
    		  success: function(res) {
    		    alert(JSON.stringify(res));
    		  },
    		  fail: function(error) {
    		    alert(JSON.stringify(error));
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
			 <a id="kakao-login-btn"></a>
		</div>
	</div>
	<div class="row ">
		<div class=" col-md-12">
		<form id="requestForm" name="requestForm" action="https://kapi.kakao.com/v2/user/me" method="GET" onSubmit="return false;">
			<table class="table">
			<thead><h1>카카오 사용자 로그아웃</h1></thead>
			<tbody>
				<tr>
		           <td><label>userID</label></td>
		           <td><input type="text" class="form-control" name="userID" id="userID" value="" /></td>
		       	</tr>
		       	<tr>
		           <td><label>userEmail</label></td>
		           <td><input type="text" class="form-control" name="userEmail" id="userEmail" value="" /></td>
		       	</tr>
		       	<tr>
		           <td><label>userNickName</label></td>
		           <td><input type="text" class="form-control" name="userNickName" id="userNickName" value="" /></td>
		       	</tr>
		       	<tr>
		           <td><button type="button" class="btn btn-primary" onclick="logoutapi();">Submit</button><input type="image" name="button" src="/img/kakao_login_medium_narrow.png" onclick="logoutapi();"></td>
		       	</tr>
		       	<tr>
		           <td><button type="button" class="btn btn-primary" onclick="callapi();">Submit</button><input type="image" name="button" src="/img/kakao_login_medium_narrow.png" onclick="logoutapi();"></td>
		       	</tr>
				</tbody>
			</table>
		</form>
		</div>
	</div>
	<!-- console.log(userID);
    console.log(userEmail);
    console.log(userNickName); -->
	
	
	
</div>

<script>
Kakao.Auth.createLoginButton({
    container: '#kakao-login-btn',
    success: function(authObj) {
     
     // 로그인 성공시, API를 호출합니다.
     Kakao.API.request({
      url: '/v2/user/me',
      success: function(res) {
       console.log(res);
       
       var userID = res.id;      //유저의 카카오톡 고유 id
       var userEmail = res.kaccount_email;   //유저의 이메일
       var userNickName = res.properties.nickname; //유저가 등록한 별명
       
       console.log(userID);
       console.log(userEmail);
       console.log(userNickName);
       
       document.getElementById('userID').value = userID;
       document.getElementById('userEmail').value = userEmail;
       document.getElementById('userNickName').value = userNickName;
       
      },
      fail: function(error) {
       alert(JSON.stringify(error));
      }
     });
    },
    fail: function(err) {
     alert(JSON.stringify(err));
    }
   });
   
	function logoutapi()
	{
		   Kakao.API.request({
			      url: '/v2/user/me',
			      success: function(res) {
			       console.log(res);
			       
			       /* var userID = res.id;      //유저의 카카오톡 고유 id
			       var userEmail = res.kaccount_email;   //유저의 이메일
			       var userNickName = res.properties.nickname; //유저가 등록한 별명
			       
			       console.log(userID);
			       console.log(userEmail);
			       console.log(userNickName); */
			      },
			      fail: function(error) {
			       alert(JSON.stringify(error));
			      }
			     });
	}
   
   function callapi()
   {
	   Kakao.API.request({
		      url: '/v1/user/logout',
		      success: function(res) {
		       console.log(res);
		       
		       /* var userID = res.id;      //유저의 카카오톡 고유 id
		       var userEmail = res.kaccount_email;   //유저의 이메일
		       var userNickName = res.properties.nickname; //유저가 등록한 별명
		       
		       console.log(userID);
		       console.log(userEmail);
		       console.log(userNickName); */
		      },
		      fail: function(error) {
		       alert(JSON.stringify(error));
		      }
		     });
   }
</script>
</body>
</html>