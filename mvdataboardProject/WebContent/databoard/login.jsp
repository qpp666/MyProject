<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<style type="text/css">
.row{
	margin: 0px auto;
	width: 350px;
}
</style>
</head>
<body>
	<div class="container">
		<h2 class="text-center">Login</h2>
		<div class="row">
			<form action="login_ok.jsp" method="post">
			<table class="table">
				<tr>
					<td class="text-right success" width="25%">ID</td>
					<td class="text-left" width="75%">
						<input type="text" name=id class="input-sm" size="15">
					</td>
				</tr>
				<tr>
					<td class="text-right success" width="25%">PW</td>
					<td class="text-left" width="75%">
						<input type="password" name=pwd class="input-sm" size="15">
					</td>
				</tr>
				<tr>
					<td colspan="2" class="text-center">
						<input type="submit" value="로그인" class="btn btn-sm btn-success" size="15">
						<input type="submit" value="취소" class="btn btn-sm btn-warning" size="15">
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
</body>
</html>