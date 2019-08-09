<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.model.*"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="model" class="com.sist.model.Model"/>
<%
	model.login(request);
%>
<c:choose>
	<c:when test="${result=='NOID' }">
		<script>
		alert('ID를 확인해주세요!');
		history.back();
		</script>
	</c:when>
	<c:when test="${result=='NOPWD' }">
	<script>
		alert('PW를 확인해주세요!');
		history.back();
		</script>
	</c:when>
	<c:otherwise>
		<c:redirect url="list.jsp"/>
	</c:otherwise>
</c:choose>
