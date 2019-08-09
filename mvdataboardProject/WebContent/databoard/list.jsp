<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.model.*"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="model" class="com.sist.model.Model"/>
<%
	model.dataBoardModel(request);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<style type="text/css">
.row{
	margin: 0px auto;
	width: 900px;
}
</style>
</head>
<body>
	<div class="container">
		<h2 class="text-center">자료실</h2>
		<form method="post" action="list.jsp${page}">
		<div class="row">
			<table class="table">
				<tr>
					<td class="text-left">
						<a href="insert.jsp" class="btn btn-sm btn-success">새글</a>
					</td>
					<td class="text-right">
						${curpage }page / ${totalpage }page
					</td>
				</tr>
			</table>
			<table class="table">
				<tr class="danger">
					<th class="text-center" width="10%">번호</th>
					<th class="text-center" width="45%">제목</th>
					<th class="text-center" width="15%">이름</th>
					<th class="text-center" width="20%">작성일</th>
					<th class="text-center" width="10%">조회수</th>
				</tr>
				<c:forEach var="vo" items="${list }" varStatus="s">
					<tr class="${s.index%2==0?'':'warning' }">
											<%--(짝)	주황색(홀) --%>
						<td width="10%" class="text-center">${vo.no }</td>
						<td width="45%" class="text-left"><a href="detail.jsp?no=${vo.no}&page=${curpage}">${vo.subject }</a></td>
						<td width="15%" class="text-center">${vo.name }</td>
						<td width="20%" class="text-center">${vo.regdate }</td>
						<td width="10%" class="text-center">${vo.hit }</td>
					</tr>
				</c:forEach>
			</table>
			<center>
				<div class="row">
					<ul class="pagination">
					  <li><a href="list.jsp?page=1">&lt;&lt;</a></li>
					  <li><a href="#">&lt;</a></li>
					  
					  <c:forEach var="i" begin="1" end="${totalpage }" >
					  <li class="${i==curpage?'active':''} "><a href="list.jsp?page=${i}">${i}</a></li>
					  </c:forEach>
					  
					  <li><a href="#">&gt;</a></li>
					  <li><a href="list.jsp?page=${totalpage }">&gt;&gt;</a></li>
					</ul>
				</div>
			</center>
		</div>
		</form>
	</div>
</body>
</html>