<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert image</title>
</head>
<body>
	<div>
		<h3>Insert your image</h3>
		<form action="<%=request.getContextPath() %>/image" method="post" enctype='multipart/form-data'>
			<label>Email</label><br>
			<input placeholder="email" name="email" type="email"><br>
			<input type="file" name="file"><br>
			<br>
			<input type="submit" name="submit" value="Submit">
		</form>
	</div>
</body>
</html>