<%
	String ip = request.getServerName() ;
	int port = request.getServerPort() ;
	String basePath = request.getContextPath();
    if (!basePath.endsWith("/")) {
        basePath += "/";
    }
%>
<link href="<%=basePath %>/starter-template.css" rel="stylesheet" /> 
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>