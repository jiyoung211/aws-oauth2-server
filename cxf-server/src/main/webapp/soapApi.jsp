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
    
    function callwsdl()
    {
    	window.location.href = $("#wsdlurl").val();
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
	
	<h1>STEP3. Call Api</h1>
	<div class="row ">
		<div class=" col-md-12">
		<form name="apiForm" method="GET" >
			<table class="table">
			<thead> <h3>REST</h3></thead>
    		<tbody>
    			<tr>
		           <td><label>apiurl</label></td>
		           <td><input type="text" class="form-control" name="apiurl" id="apiurl" value="<%= basePath %>services/api/call" /></td>
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
	<div class="row ">
		<div class=" col-md-12">
			<form name="soapapiForm" method="POST" >
			<table class="table">
			<thead> <h3>SOAP</h3></thead>
    		<tbody>
    			<tr>
		           <td><label>soapapiurl</label></td>
		           <td><input type="text" class="form-control" name="soapapiurl" id="soapapiurl" value="<%= basePath %>services/GreeterPort" /></td>
		       	</tr>
		       	<%-- <tr>
		           <td><label>Authorization</label></td>
		           <td><input type="text" class="form-control" name="Authorization" id="Authorization" value="Bearer <%= data.getTokenKey()%>" /></td>
		       	</tr> --%>
		       	<tr>
		           <td><label>body</label></td>
        		    <!-- &lt;soapenv:Envelope xmlns:soapenv=&quot;http://schemas.xmlsoap.org/soap/envelope/&quot; xmlns:web=&quot;http://webservice.cxfexample.exampledriven.org/&quot;&gt;&lt;soapenv:Header/&gt;&lt;soapenv:Body&gt;&lt;web:readAllLocations/&gt;&lt;/soapenv:Body&gt;&lt;/soapenv:Envelope&gt; -->
		           <td><textarea type="text" class="form-control" name="body" id="body" row="10">
 			           	<soap:Envelope
	xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	<soap:Header>
		<wsse:Security
			xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
			xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" soap:mustUnderstand="1">
			<xenc:EncryptedKey
				xmlns:xenc="http://www.w3.org/2001/04/xmlenc#" Id="EK-356b05a5-1ebc-469d-ac52-83cf5368d655">
				<xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p"/>
				<ds:KeyInfo
					xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
					<wsse:SecurityTokenReference>
						<ds:X509Data>
							<ds:X509IssuerSerial>
								<ds:X509IssuerName>CN=Bethal,OU=Bethal,O=ApacheTest,L=Syracuse,C=US</ds:X509IssuerName>
								<ds:X509SerialNumber>944145418</ds:X509SerialNumber>
							</ds:X509IssuerSerial>
						</ds:X509Data>
					</wsse:SecurityTokenReference>
				</ds:KeyInfo>
				<xenc:CipherData>
					<xenc:CipherValue>mRNwjT0LRvTCJJhv6m7E9ie5OrTw84eUjlcTPDzMVTy/pi/HWC3TT5HTxcrJaeHvL2L0XRhR4V41VSD5fZ3nMb/0NYOnuOsVmZnNfivSTVLuLd2oh/GkTS6E3ClFIjdPqV2QnhkFEDWkThShN1owcVFNjtUrGnT/0sOm53aSrtcMjcMP5dofRn3IP9gBm6RLSVtCeLa4OXkCNCyTndw+BCrlYb4he796JLsZoSUTBSoYqX2ng/I8butSGaQ/Yp3ggguWD3R4mJ1oxLzRuGcNlsmfdigdejuOEyQTIFQU8DVVtOtSh6NkyBvLPgWPpGgqYeCrNNeG8HFzD0XLNOdL+g==</xenc:CipherValue>
				</xenc:CipherData>
				<xenc:ReferenceList>
					<xenc:DataReference URI="#ED-d054fa6e-f02c-4973-8822-28da47811eec"/>
					<xenc:DataReference URI="#ED-41cc93ac-b6a6-4d22-80f0-86d22e3f1099"/>
				</xenc:ReferenceList>
			</xenc:EncryptedKey>
			<wsse:BinarySecurityToken EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary" ValueType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3" wsu:Id="X509-ce27827c-d97b-46f9-887b-844265e2454a">MIIDYTCCAkmgAwIBAgIEDV+5sjANBgkqhkiG9w0BAQsFADBhMQswCQYDVQQGEwJVUzERMA8GA1UEBxMIU3lyYWN1c2UxEzARBgNVBAoTCkFwYWNoZVRlc3QxDzANBgNVBAsTBk1vcnBpdDEZMBcGA1UEAxMQd2hhdGV2ZXJob3N0LmNvbTAeFw0xNTA5MDkxNTUzMTRaFw0yNTA5MDYxNTUzMTRaMGExCzAJBgNVBAYTAlVTMREwDwYDVQQHEwhTeXJhY3VzZTETMBEGA1UEChMKQXBhY2hlVGVzdDEPMA0GA1UECxMGTW9ycGl0MRkwFwYDVQQDExB3aGF0ZXZlcmhvc3QuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjKod4Ne5+B5rPhvl7Dt7//O1fRZYw5GACCgTG6F2Cy8ozF7lfQo7jy3KTjrCxOkty6IUxcll5EKZQBfsqfKq2beEWI+tG//ZEfc1paK+4HGrqARtFXYm/azzEC8E66cVKRIej8DMyXPHvNsSSN/T7c5QCMBAd5p+uQFCGkHcX6ywiCu5hOBDhxBTr3680lRIOjBoICd3ytlT8pnHqjm7VexiG5sPg32f90Tf1UCJQL41Jn1miow4xLjDw0L9pCcoLtoh1jjOwErwISeTXtfp0zMAZ1T0CwmuDQCL2Ek0ysmoDSQlpwL/zi/9XzeZCUY9a4KK2DV6q1WNnGJq6pMu0QIDAQABoyEwHzAdBgNVHQ4EFgQULNllc99it0vTugh22XKUn7H3zUkwDQYJKoZIhvcNAQELBQADggEBAFnOo+ghsy59M25gjVBG82siBQkhgl0eSzp/wVqa41F/KCY5hY8moKZARelNgOFQQxRpK6gBhj53TjF7B0w834r3S30F37qAd+T7yfH9drN5I4mNeTHpxPKeI1KJneZUqKt1PR1iZScwPzHHIfUWRiZ8ilJwNNy2MoZONKh7lhf4ILfYclRmMu7UJfb2gFjvTnzUwS5YJ8U0H5EYy7oHZS+7q3GXuL953tFypr1m0kvDYW4kYwyhHRZEXcDvDWvmO83BIk1AOQhzQ4ak4JLBpVQJnrPBhGUZOUAmIuRoV9If5WfvjVymH13VuAKoPJR3902uGul/3Uq+ifNDF8btPpw=</wsse:BinarySecurityToken>
			<ds:Signature
				xmlns:ds="http://www.w3.org/2000/09/xmldsig#" Id="SIG-0ef39f34-f45a-4ed5-9c68-49e94f08db61">
				<ds:SignedInfo>
					<ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#">
						<ec:InclusiveNamespaces
							xmlns:ec="http://www.w3.org/2001/10/xml-exc-c14n#" PrefixList="soap"/>
						</ds:CanonicalizationMethod>
						<ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/>
						<ds:Reference URI="#TS-06ac350f-1d03-464e-9cfb-a8bcda4b02d4">
							<ds:Transforms>
								<ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#">
									<ec:InclusiveNamespaces
										xmlns:ec="http://www.w3.org/2001/10/xml-exc-c14n#" PrefixList="wsse soap"/>
									</ds:Transform>
								</ds:Transforms>
								<ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
								<ds:DigestValue>T088gyHfMmZI3W2N2y2Uf/7LCcA=</ds:DigestValue>
							</ds:Reference>
							<ds:Reference URI="#id-d1d4ce41-39a0-42de-8e53-c9df214f9f66">
								<ds:Transforms>
									<ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
								</ds:Transforms>
								<ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
								<ds:DigestValue>XQUzqDSHcT9474c77zSzic45fKI=</ds:DigestValue>
							</ds:Reference>
						</ds:SignedInfo>
						<ds:SignatureValue>Hm2WdhBPXMMKEHtbScf3auHCF9TleSxwevYPIIFiJAa7NV3BEV0jyIpq1IFWqcKCczVSb2iTu5Iw6F3fDHsakKrE5dt5aeE4XJWuQV0JurS+JC5F+9kGXZrXtwOlyY3klWSYidvdDYUaZ7JaRbSPDnix7DUFNWfGCYxWXZ5z/7hrrwsl9hhTWWt9yd4ZEfDat0f71kaxMZDr/+/XhhLtjqT0dmzCqlNUByDIKKgI9VtsCGbbUzQEnIsGHobh1mjT5sU5SuEudsBoHWl368a/bZWXdxcDlIuvidFId7WSbtjvxMaA9+pS3Vyc+Xdxy1LEmTXxW5ah2ijuPrlqHPKIKQ==</ds:SignatureValue>
						<ds:KeyInfo Id="KI-29b45276-3304-4a6d-a3ea-4fc1465a8b29">
							<wsse:SecurityTokenReference
								xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
								xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" wsu:Id="STR-c62866fd-3358-469d-a601-a54190d74ccb">
								<wsse:Reference URI="#X509-ce27827c-d97b-46f9-887b-844265e2454a" ValueType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3"/>
							</wsse:SecurityTokenReference>
						</ds:KeyInfo>
					</ds:Signature>
					<wsu:Timestamp wsu:Id="TS-06ac350f-1d03-464e-9cfb-a8bcda4b02d4">
						<wsu:Created>2020-06-22T01:09:44.091Z</wsu:Created>
						<wsu:Expires>2020-06-22T01:14:44.091Z</wsu:Expires>
					</wsu:Timestamp>
					<xenc:EncryptedData
						xmlns:xenc="http://www.w3.org/2001/04/xmlenc#" Id="ED-d054fa6e-f02c-4973-8822-28da47811eec" Type="http://www.w3.org/2001/04/xmlenc#Element">
						<xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#aes128-cbc"/>
						<ds:KeyInfo
							xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
							<wsse:SecurityTokenReference
								xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
								xmlns:wsse11="http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd" wsse11:TokenType="http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey">
								<wsse:Reference URI="#EK-356b05a5-1ebc-469d-ac52-83cf5368d655"/>
							</wsse:SecurityTokenReference>
						</ds:KeyInfo>
						<xenc:CipherData>
							<xenc:CipherValue>3V0dJ8pOytnKmij9FyAy3aqBDRw0W2g9p8ii7Kaxykzw7SB2l7I98MLREmz56HyLPX7RonAug+b0/nUeKOR4syoKPOQ40d3vasmWu0T4gSK6greCShekkLgIsoa8cHU7z366Lg+zW3t+QDWQ8x/+xe2KQ0xCBLbhH+nwtK9NLj4WuAwyQ9wUdMsuuGqALfAhUhE9yYCRifoCPHCt0CbOXwt6JdQX/ua62Lf+0dPGLTTyR+AupLq5vNVi0BFlRaRY4SxSLFQaVkZtUd/um3rjVSNATyQuU5+2Wv+SiwEBkoBl5NTfHZ7mqsT6lGQHnWYALVoF6vUNGQV/YpvBRKHjWo0EYCYcuTg2FsQ+Ng7t2J1O3dPOaerBT8loXs3SekBKkpdEOpG74XXHsuLaVPt1EA==</xenc:CipherValue>
						</xenc:CipherData>
					</xenc:EncryptedData>
				</wsse:Security>
				<Action
					xmlns="http://www.w3.org/2005/08/addressing">http://cxf.apache.org/hello_world_soap_http/Greeter/greetMeResponse
				</Action>
				<MessageID
					xmlns="http://www.w3.org/2005/08/addressing">urn:uuid:c9e15640-de23-439a-9a6f-d1666d7b3e91
				</MessageID>
				<To
					xmlns="http://www.w3.org/2005/08/addressing">http://localhost:9990/decoupled_endpoint
				</To>
				<RelatesTo
					xmlns="http://www.w3.org/2005/08/addressing">urn:uuid:607da2f9-fddd-436a-aa69-d783502d8cf4
				</RelatesTo>
			</soap:Header>
			<soap:Body
				xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" wsu:Id="id-d1d4ce41-39a0-42de-8e53-c9df214f9f66">
				<xenc:EncryptedData
					xmlns:xenc="http://www.w3.org/2001/04/xmlenc#" Id="ED-41cc93ac-b6a6-4d22-80f0-86d22e3f1099" Type="http://www.w3.org/2001/04/xmlenc#Content">
					<xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#aes128-cbc"/>
					<ds:KeyInfo
						xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
						<wsse:SecurityTokenReference
							xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
							xmlns:wsse11="http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd" wsse11:TokenType="http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey">
							<wsse:Reference URI="#EK-356b05a5-1ebc-469d-ac52-83cf5368d655"/>
						</wsse:SecurityTokenReference>
					</ds:KeyInfo>
					<xenc:CipherData>
						<xenc:CipherValue>rEBl0ELOFoKzIc8t8jPDHa/R3YrY0vDiLoOj/13wt98qRJPRz2hx88/C+3m8s6uViJ0FBwTUu6bhl0xPPWhvbsnaRDGnRNvwZ1aiHJIO0DLFWnLaj6aKMR4g6T/kGBWW7+Xe2nnM26zG1z+opmVLi45spmBCv3NRCYcxLx8X4S+o4EapbL2fYhBNNtBbvNVHZu/xnmTvaCfEnaascqYXsZ26S6hW5fKg9Xx1LLeSwpQ=</xenc:CipherValue>
					</xenc:CipherData>
				</xenc:EncryptedData>
			</soap:Body>
		</soap:Envelope> 
<soap:Envelope
	xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	<soap:Header>
		<wsse:Security
			xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
			xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" soap:mustUnderstand="1">
			<xenc:EncryptedKey
				xmlns:xenc="http://www.w3.org/2001/04/xmlenc#" Id="EK-356b05a5-1ebc-469d-ac52-83cf5368d655">
				<xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p"/>
				<ds:KeyInfo
					xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
					<wsse:SecurityTokenReference>
						<ds:X509Data>
							<ds:X509IssuerSerial>
								<ds:X509IssuerName>CN=Bethal,OU=Bethal,O=ApacheTest,L=Syracuse,C=US</ds:X509IssuerName>
								<ds:X509SerialNumber>944145418</ds:X509SerialNumber>
							</ds:X509IssuerSerial>
						</ds:X509Data>
					</wsse:SecurityTokenReference>
				</ds:KeyInfo>
				<xenc:CipherData>
					<xenc:CipherValue>mRNwjT0LRvTCJJhv6m7E9ie5OrTw84eUjlcTPDzMVTy/pi/HWC3TT5HTxcrJaeHvL2L0XRhR4V41VSD5fZ3nMb/0NYOnuOsVmZnNfivSTVLuLd2oh/GkTS6E3ClFIjdPqV2QnhkFEDWkThShN1owcVFNjtUrGnT/0sOm53aSrtcMjcMP5dofRn3IP9gBm6RLSVtCeLa4OXkCNCyTndw+BCrlYb4he796JLsZoSUTBSoYqX2ng/I8butSGaQ/Yp3ggguWD3R4mJ1oxLzRuGcNlsmfdigdejuOEyQTIFQU8DVVtOtSh6NkyBvLPgWPpGgqYeCrNNeG8HFzD0XLNOdL+g==</xenc:CipherValue>
				</xenc:CipherData>
				<xenc:ReferenceList>
					<xenc:DataReference URI="#ED-d054fa6e-f02c-4973-8822-28da47811eec"/>
					<xenc:DataReference URI="#ED-41cc93ac-b6a6-4d22-80f0-86d22e3f1099"/>
				</xenc:ReferenceList>
			</xenc:EncryptedKey>
			<wsse:BinarySecurityToken EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary" ValueType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3" wsu:Id="X509-ce27827c-d97b-46f9-887b-844265e2454a">MIIDYTCCAkmgAwIBAgIEDV+5sjANBgkqhkiG9w0BAQsFADBhMQswCQYDVQQGEwJVUzERMA8GA1UEBxMIU3lyYWN1c2UxEzARBgNVBAoTCkFwYWNoZVRlc3QxDzANBgNVBAsTBk1vcnBpdDEZMBcGA1UEAxMQd2hhdGV2ZXJob3N0LmNvbTAeFw0xNTA5MDkxNTUzMTRaFw0yNTA5MDYxNTUzMTRaMGExCzAJBgNVBAYTAlVTMREwDwYDVQQHEwhTeXJhY3VzZTETMBEGA1UEChMKQXBhY2hlVGVzdDEPMA0GA1UECxMGTW9ycGl0MRkwFwYDVQQDExB3aGF0ZXZlcmhvc3QuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjKod4Ne5+B5rPhvl7Dt7//O1fRZYw5GACCgTG6F2Cy8ozF7lfQo7jy3KTjrCxOkty6IUxcll5EKZQBfsqfKq2beEWI+tG//ZEfc1paK+4HGrqARtFXYm/azzEC8E66cVKRIej8DMyXPHvNsSSN/T7c5QCMBAd5p+uQFCGkHcX6ywiCu5hOBDhxBTr3680lRIOjBoICd3ytlT8pnHqjm7VexiG5sPg32f90Tf1UCJQL41Jn1miow4xLjDw0L9pCcoLtoh1jjOwErwISeTXtfp0zMAZ1T0CwmuDQCL2Ek0ysmoDSQlpwL/zi/9XzeZCUY9a4KK2DV6q1WNnGJq6pMu0QIDAQABoyEwHzAdBgNVHQ4EFgQULNllc99it0vTugh22XKUn7H3zUkwDQYJKoZIhvcNAQELBQADggEBAFnOo+ghsy59M25gjVBG82siBQkhgl0eSzp/wVqa41F/KCY5hY8moKZARelNgOFQQxRpK6gBhj53TjF7B0w834r3S30F37qAd+T7yfH9drN5I4mNeTHpxPKeI1KJneZUqKt1PR1iZScwPzHHIfUWRiZ8ilJwNNy2MoZONKh7lhf4ILfYclRmMu7UJfb2gFjvTnzUwS5YJ8U0H5EYy7oHZS+7q3GXuL953tFypr1m0kvDYW4kYwyhHRZEXcDvDWvmO83BIk1AOQhzQ4ak4JLBpVQJnrPBhGUZOUAmIuRoV9If5WfvjVymH13VuAKoPJR3902uGul/3Uq+ifNDF8btPpw=</wsse:BinarySecurityToken>
			<ds:Signature
				xmlns:ds="http://www.w3.org/2000/09/xmldsig#" Id="SIG-0ef39f34-f45a-4ed5-9c68-49e94f08db61">
				<ds:SignedInfo>
					<ds:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#">
						<ec:InclusiveNamespaces
							xmlns:ec="http://www.w3.org/2001/10/xml-exc-c14n#" PrefixList="soap"/>
						</ds:CanonicalizationMethod>
						<ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/>
						<ds:Reference URI="#TS-06ac350f-1d03-464e-9cfb-a8bcda4b02d4">
							<ds:Transforms>
								<ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#">
									<ec:InclusiveNamespaces
										xmlns:ec="http://www.w3.org/2001/10/xml-exc-c14n#" PrefixList="wsse soap"/>
									</ds:Transform>
								</ds:Transforms>
								<ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
								<ds:DigestValue>T088gyHfMmZI3W2N2y2Uf/7LCcA=</ds:DigestValue>
							</ds:Reference>
							<ds:Reference URI="#id-d1d4ce41-39a0-42de-8e53-c9df214f9f66">
								<ds:Transforms>
									<ds:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
								</ds:Transforms>
								<ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
								<ds:DigestValue>XQUzqDSHcT9474c77zSzic45fKI=</ds:DigestValue>
							</ds:Reference>
						</ds:SignedInfo>
						<ds:SignatureValue>Hm2WdhBPXMMKEHtbScf3auHCF9TleSxwevYPIIFiJAa7NV3BEV0jyIpq1IFWqcKCczVSb2iTu5Iw6F3fDHsakKrE5dt5aeE4XJWuQV0JurS+JC5F+9kGXZrXtwOlyY3klWSYidvdDYUaZ7JaRbSPDnix7DUFNWfGCYxWXZ5z/7hrrwsl9hhTWWt9yd4ZEfDat0f71kaxMZDr/+/XhhLtjqT0dmzCqlNUByDIKKgI9VtsCGbbUzQEnIsGHobh1mjT5sU5SuEudsBoHWl368a/bZWXdxcDlIuvidFId7WSbtjvxMaA9+pS3Vyc+Xdxy1LEmTXxW5ah2ijuPrlqHPKIKQ==</ds:SignatureValue>
						<ds:KeyInfo Id="KI-29b45276-3304-4a6d-a3ea-4fc1465a8b29">
							<wsse:SecurityTokenReference
								xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
								xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" wsu:Id="STR-c62866fd-3358-469d-a601-a54190d74ccb">
								<wsse:Reference URI="#X509-ce27827c-d97b-46f9-887b-844265e2454a" ValueType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3"/>
							</wsse:SecurityTokenReference>
						</ds:KeyInfo>
					</ds:Signature>
					<wsu:Timestamp wsu:Id="TS-06ac350f-1d03-464e-9cfb-a8bcda4b02d4">
						<wsu:Created>2020-06-22T01:09:44.091Z</wsu:Created>
						<wsu:Expires>2020-06-22T01:14:44.091Z</wsu:Expires>
					</wsu:Timestamp>
					<xenc:EncryptedData
						xmlns:xenc="http://www.w3.org/2001/04/xmlenc#" Id="ED-d054fa6e-f02c-4973-8822-28da47811eec" Type="http://www.w3.org/2001/04/xmlenc#Element">
						<xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#aes128-cbc"/>
						<ds:KeyInfo
							xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
							<wsse:SecurityTokenReference
								xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
								xmlns:wsse11="http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd" wsse11:TokenType="http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey">
								<wsse:Reference URI="#EK-356b05a5-1ebc-469d-ac52-83cf5368d655"/>
							</wsse:SecurityTokenReference>
						</ds:KeyInfo>
						<xenc:CipherData>
							<xenc:CipherValue>3V0dJ8pOytnKmij9FyAy3aqBDRw0W2g9p8ii7Kaxykzw7SB2l7I98MLREmz56HyLPX7RonAug+b0/nUeKOR4syoKPOQ40d3vasmWu0T4gSK6greCShekkLgIsoa8cHU7z366Lg+zW3t+QDWQ8x/+xe2KQ0xCBLbhH+nwtK9NLj4WuAwyQ9wUdMsuuGqALfAhUhE9yYCRifoCPHCt0CbOXwt6JdQX/ua62Lf+0dPGLTTyR+AupLq5vNVi0BFlRaRY4SxSLFQaVkZtUd/um3rjVSNATyQuU5+2Wv+SiwEBkoBl5NTfHZ7mqsT6lGQHnWYALVoF6vUNGQV/YpvBRKHjWo0EYCYcuTg2FsQ+Ng7t2J1O3dPOaerBT8loXs3SekBKkpdEOpG74XXHsuLaVPt1EA==</xenc:CipherValue>
						</xenc:CipherData>
					</xenc:EncryptedData>
				</wsse:Security>
				<Action
					xmlns="http://www.w3.org/2005/08/addressing">http://cxf.apache.org/hello_world_soap_http/Greeter/greetMeResponse
				</Action>
				<MessageID
					xmlns="http://www.w3.org/2005/08/addressing">urn:uuid:c9e15640-de23-439a-9a6f-d1666d7b3e91
				</MessageID>
				<To
					xmlns="http://www.w3.org/2005/08/addressing">http://localhost:9990/decoupled_endpoint
				</To>
				<RelatesTo
					xmlns="http://www.w3.org/2005/08/addressing">urn:uuid:607da2f9-fddd-436a-aa69-d783502d8cf4
				</RelatesTo>
			</soap:Header>
			<soap:Body
				xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" wsu:Id="id-d1d4ce41-39a0-42de-8e53-c9df214f9f66">
				<xenc:EncryptedData
					xmlns:xenc="http://www.w3.org/2001/04/xmlenc#" Id="ED-41cc93ac-b6a6-4d22-80f0-86d22e3f1099" Type="http://www.w3.org/2001/04/xmlenc#Content">
					<xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#aes128-cbc"/>
					<ds:KeyInfo
						xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
						<wsse:SecurityTokenReference
							xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
							xmlns:wsse11="http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd" wsse11:TokenType="http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey">
							<wsse:Reference URI="#EK-356b05a5-1ebc-469d-ac52-83cf5368d655"/>
						</wsse:SecurityTokenReference>
					</ds:KeyInfo>
					<xenc:CipherData>
						<xenc:CipherValue>rEBl0ELOFoKzIc8t8jPDHa/R3YrY0vDiLoOj/13wt98qRJPRz2hx88/C+3m8s6uViJ0FBwTUu6bhl0xPPWhvbsnaRDGnRNvwZ1aiHJIO0DLFWnLaj6aKMR4g6T/kGBWW7+Xe2nnM26zG1z+opmVLi45spmBCv3NRCYcxLx8X4S+o4EapbL2fYhBNNtBbvNVHZu/xnmTvaCfEnaascqYXsZ26S6hW5fKg9Xx1LLeSwpQ=</xenc:CipherValue>
					</xenc:CipherData>
				</xenc:EncryptedData>
				
				
				
			</soap:Body>
		</soap:Envelope>
		
		           </textarea> </td>
		       	</tr>
		       	<tr>
		           <td><button type="button" class="btn btn-primary" onclick="callsoapapi();">Submit</button></td>
		       	</tr>
				</tbody>
			</table>
		</form>
		</div>
	</div>
	
	<div class="row ">
		<div class=" col-md-12">
		<form name="wsdlForm" action="<%=basePath%>services/soap?wsdl" method="GET">
			<table class="table">
			<thead><h1>Call Soap Wsdl</h1></thead>
			<tbody>
				<tr>
		           <td><label>wsdlurl</label></td>
		           <td><input type="text" class="form-control" name="wsdlurl" id="wsdlurl" value="<%= basePath %>services/GreeterPort?wsdl" /></td>
		       	</tr>
		       	<tr>
		            <td><button type="button" class="btn btn-primary" onclick="callwsdl();">Submit</button></td>
		       	</tr>
			</tbody>
			</table>
		</form>
		</div>
	</div>
	
</div>
</body>
</html>