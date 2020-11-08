<%@ page import="org.apache.cxf.rs.security.oauth2.common.Client"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
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
    String requestSoap ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.cxfexample.exampledriven.org/\"><soapenv:Header/><soapenv:Body><web:readAllLocations/></soapenv:Body></soapenv:Envelope>";
    String requestSoap1 = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:web='ahttp://webservice.cxfexample.exampledriven.org/'><soapenv:Header/><soapenv:Body><web:readAllLocations/></soapenv:Body></soapenv:Envelope>";

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
    $( document ).ready(
    	    //("#body").val() ="";
    		/* <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://webservice.cxfexample.exampledriven.org/">
    		   <soapenv:Header/>
    		   <soapenv:Body>
    		      <web:readAllLocations/>
    		   </soapenv:Body>
    		</soapenv:Envelope> */
    );
    
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
    
    function callsoapapi()
    {
    	 var xmlString = $("#body").val();
    	 $.ajax({
 	        type : 'post',
 	        url : $("#soapapiurl").val(), //'/test.jsp,
 	        dataType : 'text',
 	        data: xmlString,
 	        beforeSend : function(xhr){
 	            xhr.setRequestHeader("Authorization", "Bearer "+"<%=data.getTokenKey()%>" );
 	            xhr.setRequestHeader("Content-Type", "text/xml" );
 	        },
 	        error: function(xhr, status, error){
 	            alert(error);
 	        },
 	        success : function(msg){
 	            alert(msg);
 	        }
     	 }); 
    }
   /*  function ConvertSystemSourcetoHtml(str){
   	 str = str.replace(/</g,"&lt;");
   	 str = str.replace(/>/g,"&gt;");
   	 str = str.replace(/\"/g,"&quot;");
   	 str = str.replace(/\'/g,"&#39;");
   	 str = str.replace(/\n/g,"<br />");
   	 return str;
   	} */
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
				   <td><label>clientSecret</label></td>
		           <td><input type="password" class="form-control" name="clientSecret" id="clientSecret" /></td>
		       	<tr>
		       	<tr>
		           <td><button name="submit_login_button" class=" btn btn-primary">Submit</button></td>
		       	</tr>
    		</tbody>
    		</table>
		</form>
		</div>
	</div>
</div>
</body>
</html>