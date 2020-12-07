<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="com.multi.oauth2client.*" %>
<%@ page import="java.util.*" %>
<%

	
	HashMap<String,String> map = new HashMap<String, String>();
	map.put("client_id", Settings.CLIENT_ID);
	map.put("client_secret", Settings.CLIENT_KEY);
	map.put("redirect_uri", Settings.REDIRECT_URI);
	map.put("response_type", "code");
	map.put("scope", Settings.SCOPE);
	map.put("state", OAuth2ClientUtil.generateRandomState());
	//callback쪽에서 값의 일치 여부 확인
	session.setAttribute("state", map.get("state"));
	
	String url = Settings.AUTHORIZE_URL + "?" + Settings.getParamString(map, false);
%>    
<%@ include file="/WEB-INF/include-header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>oauth2provider test</title>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
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
         <br/>
		<p>Client ID : 9980228a-1fd8-4501-be77-ce8e98eed18c<br>                            </p>
		<p>Client Secret : 8117a5d75e9909eb7858b5638803d72c707fb744<br>                    </p>
		<p>Client Name : TestApp1<br>                                                      </p>
		<p>Description : TestApp1 입니다.<br>                                              </p>
		<p>Client Type : W<br>                                                             </p>
		<p>Client URL : http://<%= ip %>:<%= port %><br>                                   </p>
		<p>Redirect URI : http://<%= ip %>:<%= port %>/oauth2client/callback.jsp<br>       </p>
		<p>Scope : reademail,sendemail,readboard,personalinfo,calendar<br>                 </p>
		
		<a href="<%=url %>">Authorize!!</a>
	</main>
</body>
</html>