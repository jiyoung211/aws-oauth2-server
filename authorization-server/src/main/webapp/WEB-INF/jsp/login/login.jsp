<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/include-header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top ">
	      <div class="container">
	        <div class="navbar-header">
	          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
	            <span class="sr-only">Toggle navigation</span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
	          <a class="navbar-brand" href="http://<%=ip%>:7070">OAuth</a>
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
    <main role="main" class="container ">
   <div class="row ">
		<div class=" col-md-12">
		<form action="login.do" method="POST" >
			<table class="table">
				<tr>
					<td><label>UserID</label></td>
					<td><input class="form-control" type="text" name="userid" id="userid" value="gdhong" /></td>
				</tr>
				<tr>
					<td><label>Password</label></td>
					<td><input class="form-control" type="password" name="password"  id="password" value="gdhong" /></td>
				</tr>
				<tr>
					<td colspan="2">
						<input class=" btn btn-primary" type="submit" value="로그인" />
					</td>
				</tr>
			</table>
		</form>
		 </div>
		</div>
	</main>
</body>
</html>	    
