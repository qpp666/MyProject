<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="model" class="com.sist.model.Model"/>
<%
	model.dataupdatemodel_ok(request);
	
%>
<c:if test="${chk==true }">
	<c:redirect url="detail.jsp?no=${no }&page=${page }"/>
</c:if>
<c:if test="${chk==false }">
	<script>
		alert("비밀번호가 틀립니다.");
		history.back();
	</script>
</c:if>
