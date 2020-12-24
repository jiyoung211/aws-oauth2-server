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
	
		<script type="text/javascript">
			function aaaaaaaa()
			{
				alert("Server migration is in progress due to overload ... ");
	
			}
			//aaaaaaaa();
		</script>
	<style>
	.masthead {
    margin-bottom: 50px;
    background: no-repeat center center;
    background-color: #868e96;
    background-attachment: scroll;
    position: relative;
    background-size: 100%;
    color: #fff;
}
	</style>
	

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

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron masthead"  style="background-image: linear-gradient( rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.3) ),url('http://<%=ip%>:<%=port%>/<%=basePath%>/img/home-bg.jpg');">
      <div class="container">
        <h1>AWS OAuth Server</h1>
        <p>oauth2.0 authentication demo server</p>
        <!-- <p><a class="btn btn-primary btn" href="#" role="button">Learn more &raquo;</a></p> -->
      </div>
    </div>

    <div class="container ">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-md-6">
              <h2>Authorization Code Grant Type</h2>
          <p>Request Oauth authentication as an implicit authorization type to Oauth2 Provider</p>
          <p><a class="btn btn-default" href="http://<%=ip%>:8000/oauth2client" role="button">View details &raquo;</a></p>
        </div>
        <div class="col-md-6">
          <h2>Implicit Grant Type</h2>
          <p>Request Oauth authentication as an implicit authorization type to Oauth2 Provider</p>
          <p><a class="btn btn-default" href="http://<%=ip%>:8000/oauth2client_agentflow" role="button">View details &raquo;</a></p>
       </div>
      </div>
      <div class="row">
      	<div class="col-md-6">
          <h2>CXF Server</h2>
          <p>Implementing Oauth server using Apache CXF</p>
          <p><a class="btn btn-default" href="http://<%=ip%>:7070/oauthServer.jsp" role="button">View details &raquo;</a></p>
        </div>
        <div class="col-md-6">
          <h2>Swagger Server</h2>
          <p>Shows API specification in CXF server</p>
          <p><a class="btn btn-default" href="http://<%=ip%>:7070/swagger/index.html" role="button">View details &raquo;</a></p>
        </div>
      </div>

      <hr>

      <footer >
        <p>&copy; Company 2014</p>
      </footer>
    </div> <!-- /container -->

	
	
	
	



	
</body>
</html>