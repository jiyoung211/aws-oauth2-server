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
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/docs/4.0/assets/img/favicons/favicon.ico">

    <title>API Client Information</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/pricing/">

    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/css/pricing.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
	
	
    <script>
    function callapi(operation, requestParam)
    {
    	console.log(operation);
   		console.log(requestParam);
   		
    	 <%--  $.ajax({
 	        type : 'get',
 	        url : '/app/soapClient/main', //'/test.jsp,
 	        dataType : 'text',
	        data: 'requestParam='+requestParam,
 	        beforeSend : function(xhr){
 	           // xhr.setRequestHeader("Authorization", "Bearer "+"<%=data.getTokenKey()%>" );
 	        },
 	        error: function(xhr, status, error){
 	            alert(error);
 	        },
 	        success : function(msg){
 	            alert(msg);
 	        }
     	 });   --%>
    	  window.location.href =  '/app/soapClient/main?'+'operation='+operation+'&requestParam='+requestParam;
    }
    
    </script>
  </head>

  <body>

     <div class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom box-shadow">
      <h5 class="my-0 mr-md-auto font-weight-normal"><a class="p-2 text-dark" href="/">CXF</a></h5>
      <nav class="my-2 my-md-0 mr-md-3">
        <a class="p-2 text-dark" href="#">Features</a>
        <a class="p-2 text-dark" href="#">Enterprise</a>
        <a class="p-2 text-dark" href="#">Support</a>
        <a class="p-2 text-dark" href="#">Pricing</a>
      </nav>
      <!-- <a class="btn btn-outline-primary" href="#">Sign up</a> -->
    </div> 
    
    <!-- <div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
      <h1 class="display-4">Pricing</h1>
      <p class="lead">Quickly build an effective pricing table for your potential customers with this Bootstrap example. It's built with default Bootstrap components and utilities with little customization.</p>
    </div> -->

    <div class="container">
      <div class="row">
			<div class=" col-md-12">
			<form id="createForm" name="createForm" action="<%=basePath%>app/main1" method="GET" >
				<table class="table">
				<thead><h1>Request WebService Client  </h1></thead>
	    		<tbody>
	 		    	<tr>
					   <td><label>greetMe</label></td>
			           <td><input type="text" class="form-control" name="requestParam" id="requestParam" value="Anne"/></td>
			           <td><button type="button" class="btn btn-primary" onclick="callapi('greetMe',$('#requestParam').val() );">Submit</button></td>
			       	<tr>
	    			<tr>
					   <td><label>greetMeOneWay</label></td>
			           <td><input type="text" class="form-control" name="requestParam" id="requestParam" value="Bill" palceholder=""/></td>
			           <td><button type="button" class="btn btn-primary" onclick="callapi('greetMeOneWay',$('#requestParam').val() );">Submit</button></td>
			       	<tr>
	    			<tr>
					   <td><label>sayHi</label></td>
			           <td><input type="text" class="form-control" name="requestParam" id="requestParam" value="" palceholder="N/A" /></td>
			           <td><button type="button" class="btn btn-primary" onclick="callapi('sayHi',$('#requestParam').val() );">Submit</button></td>
			       	<tr>
	    		</tbody>
	    		</table>
			</form>
			</div>
		</div>

    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
<!--     <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
 -->
     <!-- <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery-slim.min.js"><\/script>')</script> -->
    <script src="/js/popper.min.js"></script>
    <!-- <script src="/js/bootstrap.min.js"></script> -->
    <script src="/js/holder.min.js"></script>
    <script>
      Holder.addTheme('thumb', {
        bg: '#55595c',
        fg: '#eceeef',
        text: 'Thumbnail'
      });
    </script>
  </body>
</html>
