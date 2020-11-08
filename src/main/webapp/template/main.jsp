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
	
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<link rel="stylesheet" href="assets/css/main.css" />
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
	<section id="one" class="wrapper style2">
	    <div class="inner">
		    <div class="box">
		 	<div class="content">
			      <h1 class="align-center">Example</h1>
			      <hr />
			      <form action="#" method="post">
				      <div class="field">
					      <label for="name">1. OAuth2 Server </label>
						  <a href="/oauthServer.jsp" class="button special small">/oauthServer.jsp</a>
				      </div>
				      <div class="field">
					      <label for="name">2. SAAJ  </label>
					      <a href="/saaj.jsp" class="button special small">/saaj.jsp</a>
				      </div>
				      <div class="field">
					      <label for="name">3. Swagger  </label> 
					      <a href="http://localhost:7071/swagger-ui.html" class="button special small">http://localhost:7071/swagger-ui.html</a>
					 </div>
				      <div class="field">
					      <label for="name">4. JAXB  </label>
					      <a href="http://localhost:7072" class="button special small">http://localhost:7072</a>
					  </div>
				      <div class="field">
					      <label for="name">5. WS-Security </label> 
					      <a href="/wssecurity.jsp" class="button special small">/wssecurity.jsp</a>
					  </div>
			      </form>
		        </div>
		    </div>
		</div>
	</section>
	
</body>
</html>