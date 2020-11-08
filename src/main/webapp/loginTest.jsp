<%@ page import="org.apache.cxf.rs.security.oauth2.common.Client"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.apache.cxf.rs.security.oauth2.common.ClientAccessToken"%>

<% 
    String basePath = request.getContextPath();
    if (!basePath.endsWith("/")) {
        basePath += "/";
    }
   ClientAccessToken data = (ClientAccessToken)request.getAttribute("clientaccesstoken");
   if(data == null)
   {
     data = new ClientAccessToken();
   }
   
%>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta charset="utf-8">

    <title>API Client Information</title>
 	
 	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
	<link href="/starter-template.css" rel="stylesheet" /> 
    <script  type="text/javascript">
    function callapi()
    {
    	 $.ajax({
 	        type : 'get',
 	        url : $("#apiurl").val(), //'/test.jsp,
 	        dataType : 'text',
 	        beforeSend : function(xhr){
 	            xhr.setRequestHeader("Authorization", "Bearer "+"<%=data.getTokenKey()%>" );
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
	<div class="row">
		<div class=" col-md-12">
		<form name="authForm" action="<%=basePath%>services/login" method="POST">
			<table class="table">
			<thead><h1>Login</h1></thead>
    		<tbody>
    			<tr>
				   <td><label>clientId</label></td>
		           <td><input type="text" class="form-control" name="clientId" id="clientId" /></td>
		       	<tr>
		       	<tr>
				   <td><label>password</label></td>
		           <td><input type="password" class="form-control" name="password" id="password" /></td>
		       	<tr>
		       	<tr>
		           <td><button name="submit_login_button" class=" btn btn-primary">Submit</button></td>
		       	</tr>
    		</tbody>
    		</table>
		</form>
		</div>
	</div>
	
	
	<div class="row ">
		<div class=" col-md-12">
		<form name="grantForm" action="<%=basePath%>services/idp/authorize" method="GET">
			<table class="table">
			<thead><h1>Issue Authorization Code</h1></thead>
			<tbody>
				<tr>
				   <td><label>response_type</label></td>
		           <td><input type="text" class="form-control" name="response_type" id="response_type" value="code" /></td>
		       	<tr>
		           <td><label>client_id</label></td>
		           <td><input type="text" class="form-control" name="client_id" id="client_id" value="29352915982374239857" /></td>
		       	</tr>
		       	<tr>
		           <td><label>redirect_uri</label></td>
		           <td><input type="text" class="form-control" name="redirect_uri" id="redirect_uri1" value="http://localhost:7070/login.jsp" /></td>
		       	</tr>
		       	<tr>
		           <td><label>state</label></td>
		           <td><input type="text" class="form-control" name="state" id="state" value="xcoiv98y2kd22vusuye3kch" /></td>
		       	</tr>
		       	<tr>
		           <td><button name="submit_login_button" class=" btn btn-primary">Submit</button></td>
		       	</tr>
				</tbody>
			</table>
		</form>
		</div>
	</div>
	
	
	<div class="row ">
		<div class=" col-md-12">
		<form name="tokenForm" action="<%=basePath%>services/idp/token1" method="POST">
			<table class="table">
			<thead><h1>Issue Access Token</h1></thead>
    		<tbody>
    			<tr>
		           <td><label>code</label></td>
		           <td><input type="text" class="form-control" name="code" id="code" value="<%= request.getParameter("code")%>" /></td>
		       	</tr>
		       	<tr>
		           <td><label>client_id</label></td>
		           <td><input type="text" class="form-control" name="client_id" id="client_id" value="29352915982374239857" /></td>
		       	</tr>
		       	<tr>
		           <td><label>client_secret</label></td>
		           <td><input type="text" class="form-control" name="client_secret" id="client_secret" value="29352915982374239857" /></td>
		       	</tr>
		       	<tr>
		           <td><label>redirect_uri</label></td>
		           <td><input type="text" class="form-control" name="redirect_uri" id="redirect_uri" value="http://localhost:7070/login.jsp" /></td>
		       	</tr>
		       	<tr>
		           <td><label>grant_type</label></td>
		           <td><input type="text" class="form-control" name="grant_type" id="grant_type" value="authorization_code" /></td>
		       	</tr>
		       	<tr>
		           <td><button name="submit_login_button" class=" btn btn-primary">Submit</button></td>
		       	</tr>
				</tbody>
			</table>
			
		</form>
		</div>
	</div>
	
	<div class="row ">
		<div class=" col-md-12">
		<form name="apiForm" method="GET" >
			<table class="table">
			<thead> <h1>Call Api</h1></thead>
    		<tbody>
    			<tr>
		           <td><label>apiurl</label></td>
		           <td><input type="text" class="form-control" name="apiurl" id="apiurl" value="<%= basePath %>services/api/input" /></td>
		       	</tr>
		       	<tr>
		           <td><label>Authorization</label></td>
		           <td><input type="text" class="form-control" name="Authorization" id="Authorization" value="Bearer <%= data.getTokenKey()%>" /></td>
		       	</tr>
		       	<tr>
		           <td><button type="button" class="btn btn-primary" onclick="callapi();">Submit</button></td>
		       	</tr>
				</tbody>
			</table>
		</form>
		</div>
	</div>
</div>
</body>
</html>