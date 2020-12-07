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
    String ip = request.getServerName() ;
	int port = request.getServerPort() ;
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

    <title>AWS OAuth Server</title>
 	
 	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
	<link href="/starter-template.css" rel="stylesheet" /> 
		<link href="/assets/css/font-awesome.min.css" rel="stylesheet" /> 
		<link href="/main.css" rel="stylesheet" /> 
	
	
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
    </header>

	<!-- Begin page content -->
    <main role="main" class="container">
      <h1 class="mt-5">AWS OAuth Server </h1>
      <p class="lead">
	     1. CXF OAuth Server <a href="http://<%=ip%>:7070/main.jsp">http://<%=ip%>:7070/main.jsp</a>
      </p>
      <p class="lead">
	     2. Authorization Code Grant Type OAuth <a href="http://<%=ip%>:8000/oauth2client">http://<%=ip%>:8000/oauth2client</a>
      </p>
      <p class="lead">
	     3. Implicit Grant Type OAuth <a href="http://<%=ip%>:8000/oauth2client_agentflow">http://<%=ip%>:8000/oauth2client_agentflow</a>
      </p>
      
      
    </main>


	
</body>
</html>