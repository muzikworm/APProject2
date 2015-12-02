<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Facebook Login Using Java Servlets</title>
	<style type="text/css">
		.container {
			width:100%;
			background-color:white;
			text-align:center;
		}
		
		#auth-display-name {
			height:30px; 
			float:left; 
			width:120px; 
			background-color:rgb(51,95,156);
			color:white; 
			font-family:tahoma; 
			font-weight:bold;
			font-size:12px; 
			line-height:30px;		
		}
		
		#auth-info {
			text-align:left;
			font-family:tahoma; 
			font-size:14px;
		}
		
		b {
			font-weight:normal;
			color:blue;
		}
		
		i {
			font-weight:bold;
			font-size:20px;
			text-transform:capitalize;
		}

		#logout {
			margin: 0px auto;
			width:80px;
			height:23px;
			background-color: blue; 
			color:white;
		}		
	</style>
</head>
<body>
	<div class="container">
		<h1>Results</h1>
		 <h2>Prepared by: Kunal Sharma</h2>
		<div id="auth-status">
			<div id="auth-loggedin" style="width:250px; margin:0px auto;">
				<div id="auth-display-name">${fbUser.name}</div><br/><br/>
				<div id="auth-info">
					<br/><br/>
					<b>Id: </b>${fbUser.id}<br/>
					<b>Full Name: </b>${fbUser.name}<br/>
					<b>TotalNumberOflikes/TotalNumberOfPosts: </b>${fbUser.likesaverage}<br/>
					<b>Check Console for post specific likes </b></br>
					<b> Post type specific results</b></br>
					<c:forEach var="country" items="${fbUser.map}">
					   Post Type: ${country.key} - AverageLike: ${country.value}<br/>
					</c:forEach>
					<br/><br/>					
				</div>
				<br/>
				<a href="./logout">
					<div id="logout">
						Logout
					</div>
				</a>				
			</div>
		</div>
	</div>
</body>
</html>