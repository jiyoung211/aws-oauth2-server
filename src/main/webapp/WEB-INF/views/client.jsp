<%@page import="ca.sgicanada.security.constant.OAuth2Constants"%>
<%@ page import="org.apache.cxf.rs.security.oauth2.common.Client"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<%
	Client client = (Client)request.getAttribute("data");
	String clientType = client.isConfidential() ? "Confidential" : "Public";
	String brokerNumber = client.getProperties().get(OAuth2Constants.BROKER_NUMBER);
	if (brokerNumber == null || brokerNumber.trim().isEmpty()) {
		brokerNumber = "NA";
	} 
    String basePath = request.getContextPath() + request.getServletPath();
    if (!basePath.endsWith("/")) {
        basePath += "/";
    }
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>API Client Information</title>
</head>
<body>
<div class="padded">
<h1><%= StringEscapeUtils.escapeHtml(client.getApplicationName()) %></h1>
<br/>
<table border="1" id=client>
    <%
       SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.US);
       dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    %>
    <tr><th>ID</th><th>Type</th><th>Secret</th><th>Creation Date</th></tr> 
       <tr>
           <td>
               <%= client.getClientId() %>
           </td>
           <td>
               <%= clientType %>
           </td> 
           <td>
           <%
              if (client.getClientSecret() != null) {
           %>
              <%= client.getClientSecret() %>
           <%
              } else {
           %>
              <i>Unavailable</i>
           <%
              } 
           %>
           </td>
           <td>
           <% 
               Date date = new Date(client.getRegisteredAt() * 1000);
               String created = dateFormat.format(date);
		   %>
           <%=    created %><br/>
           
           </td>
           
       </tr>
     
</table>
<br/>
<h2>Restrictions:</h2>
<p/>
<table>
<tr>
<td>
<b>Broker Office/Number </b>
</td>
<td>
    <%=  brokerNumber %>
</td>
</tr>
<tr>
<td>
<b>Allowed Scopes</b>
</td>
<td>
    <%=  client.getRegisteredScopes() %>
</td>
</tr>
<tr>
<td>
<b>Redirect URL</b>
</td>
<td>
<% if (client.getRedirectUris() != null) {
                for (String redirectURI : client.getRedirectUris()) {
		   %>
           <%=    redirectURI %><br/>
           <%   }
              } %>
</td>
</tr>
<tr>
<td>
<b>Audience URL</b>
</td>
<td>
<% if (client.getRegisteredAudiences() != null) {
                for (String audURI : client.getRegisteredAudiences()) {
		   %>
           <%=    audURI %><br/>
           <%   }
              } %>
</td>
</tr>
<tr>
<td>
<b>Logout URL</b>
</td>
<td>
<% if (client.getProperties().get("post_logout_redirect_uris") != null) { %>
           <%=    client.getProperties().get("post_logout_redirect_uris") %>
<% } %>
</td>
</tr>
</table>
<br/>
<p>
<p><a href="<%= basePath + "services/" + client.getClientId() + "/tokens" %>">Issued Tokens</a></p>
</p>
<p><a href="<%= basePath + "services/idp/authorize?" 
+"response_type=code"
+ "&client_id="+client.getClientId() 
+ "&scope=apiAccess+refreshToken"
+ "&state=1"
+"&userSubjectId=11111"%>">Issue Tokens</a></p>
</p>

<!-- http://localhost:7070/services/idp/authorize?response_type=code&client_id=11111&scope=apiAccess+refreshToken&state=1&userSubjectId=11111 -->

<%--
<p>
<p><a href="<%= basePath + "services/" + client.getClientId() + "/codes" %>">Issued Code Grants</a></p>
</p>
 --%>
<br/>
<table class="table_no_border">
<tr>
<%--
    if (client.getClientSecret() != null) {
%>
<td class="td_no_border">
<form name="resetSecretForm" action="<%=basePath%>services/<%= client.getClientId() + "/reset"%>" method="POST">    
     <div data-type="control_button" class="form-line">
	<button name="submit_reset_button" class="form-submit-button" type="submit">Reset Client Secret</button>
</form>
     </div> 
</td>
<%
    }
--%>
</tr>
</table>
</div>
</body>
</html>