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
   	
   	function submitForm(formInfo, formValue)
   	{
    		console.log(formInfo);
   		console.log(formInfo.id);
   		console.log(formInfo.name);
   		console.log(formInfo.action);
   		console.log(formInfo.method);
   		console.log(formInfo.onsubmit); 
		console.log(formValue);

   		 $.ajax({
 	        type : formInfo.method,
 	        url : formInfo.action,
 	        dataType : 'text',
 	        data: formValue,
 	        beforeSend : function(xhr){
 	            xhr.setRequestHeader("Authorization", "Bearer "+"<%=data.getTokenKey()%>" );
 	            xhr.setRequestHeader("Content-Type", "text/xml" );
 	        },
 	        error: function(xhr, status, error){
 	            alert(error);
 	           document.getElementById('SOAPMessage').value = error;
 	        },
 	        success : function(msg){
 	            alert(msg);
 	           document.getElementById('SOAPMessage').value = msg;
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
			<form id="createForm" name="createForm" action="<%=basePath%>app/main1" method="GET" >
				<table class="table">
				<thead><h1>Create Request SOAPMessage  </h1></thead>
	    		<tbody>
	 		    	<tr>
					   <td><label>soapEndpointUrl</label></td>
			           <td><input type="text" class="form-control" name="soapEndpointUrl" id="soapEndpointUrl" value="http://localhost:7070/services/soap"/></td>
			       	<tr>
	    			<tr>
					   <td><label>soapAction</label></td>
			           <td><input type="text" class="form-control" name="soapAction" id="soapAction" value="" palceholder="N/A"/></td>
			       	<tr>
	    			<tr>
					   <td><label>Namespace</label></td>
			           <td><input type="text" class="form-control" name="myNamespace" id="myNamespace" value="web"/></td>
			       	<tr>
			       	<tr>
					   <td><label>NamespaceURI</label></td>
			           <td><input type="text" class="form-control" name="myNamespaceURI" id="myNamespaceURI" value="http://webservice.cxfexample.exampledriven.org/" /></td>
			       	<tr>
			       	<tr>
					   <td><label>elementName</label></td>
			           <td><input type="text" class="form-control" name="elementName" id="elementName" value="readAllLocations" /></td>
			       	<tr>
			       	<tr>
					   <td><label>childElementName</label></td>
			           <td><input type="text" class="form-control" name="childElementName" id="childElementName" value="" placeholder="N/A" /></td>
			       	<tr>
			       	<tr>
					   <td><label>childElementValue</label></td>
			           <td><input type="text" class="form-control" name="childElementValue" id="childElementValue" value="" placeholder="N/A" /></td>
			       	<tr>
			       	<tr>
					   <td><label>filePath(addAttachmentPart)</label></td>
			           <td><input type="text" class="form-control" name="filePath" id="filePath" value="C:\Users\inzent\Documents\14.Oauth\attachmentTestFile" placeholder="filePath" /></td>
			       	<tr>
			       	<tr>
			           <td><button type="submit" name="submit_login_button" class=" btn btn-primary" >Submit</button></td>
	 		       	</tr>
	    		</tbody>
	    		</table>
			</form>
			</div>
		</div>
	</div>
</body>
</html>