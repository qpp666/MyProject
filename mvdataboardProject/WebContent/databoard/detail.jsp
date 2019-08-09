<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.model.*"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="model" class="com.sist.model.Model"/>
<%
	model.dataDetailModel(request);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<link rel="stylesheet" href="css/bootstrap.min.css">
<script type="text/javascript">
var i=0;
$(function(){
	$('.del_update').click(function(){
		var no=$(this).attr("value");
		$('#dels').hide();
		if(i==0){
			$('#del'+no).show();
			$(this).text("취소");
			i=1;
		}else{
			$('#del'+no).hide();
			$(this).text("수정");
			i=0;
		}
	});
});
</script>
<style type="text/css">
.row{
	margin: 0px auto;
	width: 700px;
}
</style>
</head>
<body>
	<div class="container">
		<h2 class="text-center">내용보기</h2>
		
		<div class="row">
			<table class="table">
				<tr>
					<th width="20%" class="text-center info">번호</th>
					<td width="30%" class="text-center">${vo.no }</td>
					<th width="20%" class="text-center info">작성일</th>
					<td width="30%" class="text-center">${vo.regdate }</td>
				</tr>
				<tr>
					<th width="20%" class="text-center info">이름</th>
					<td width="30%" class="text-center">${vo.name }</td>
					<th width="20%" class="text-center info">조회수</th>
					<td width="30%" class="text-center">${vo.hit }</td>
				</tr>
				<tr>
					<th width="20%" class="text-center info">제목</th>
					<td colspan="3" class="text-left">${vo.subject }</td>
				</tr>
				<c:if test="${vo.filesize>0 }">
				<tr>
					<th width="20%" class="text-center info">첨부파일</th>
					<td colspan="3" class="text-left">
					<a href="download.jsp?fn=${vo.filename }">${vo.filename}</a>(${vo.filesize }Bytes)
					</td>
				</tr>
				</c:if>
				<tr>
					<td colspan="4" align="top" height="200" class="text-left">${vo.content }</td>
				</tr>
				<tr>
					<td colspan="4" class="text-right">
						<a href="update.jsp?no=${vo.no }&page=${curpage}" class="btn btn-sm btn-success">수정</a>
						<a href="delete.jsp?no=${vo.no }&page=${curpage}" class="btn btn-sm btn-info">삭제</a>
						<a href="list.jsp?page=${curpage }" class="btn btn-sm btn-warning">목록</a>
					</td>
				</tr>
			</table>
		</div>
		<div class="row">
			<h3>댓글</h3>
      <table class="table">
        <c:if test="${len==0 }">
        <tr>
          <td>
            	댓글이 없습니다.
          </td>
        </tr>
        </c:if>
        
        <c:if test="${len>0 }">
        <tr>
          <td>
            	<c:forEach var="rvo" items="${list }">
            		<table class="table">
            		<tr>
            			<td class="text-left">${rvo.name }${rvo.dbday }</td>
            			<td class="text-right">
            				  <c:if test="${sessionScope.id==rvo.id }">
		                        <a href="#" class="btn btn-xs btn-success del_update" value="${rvo.no }">수정</a>
		                        <a href="reply_delete.jsp?no=${rvo.no }&bno=${vo.no}&page=${curpage}" class="btn btn-xs btn-danger">삭제</a>
		                      </c:if>
            			</td>
       				</tr>
       				<tr>
            			<td colspan="2" valign="top">
							<pre style="background-color: rgb(92,70,45);opacity:0.5;color:#FFF">${rvo.msg }</pre>
						</td>
       				</tr>
       				<tr id="del${rvo.no }" style="display:none" class="dels">
       					<td colspan="2">
       						<form method="post" action="reply_Update.jsp">
				               <textarea rows="3" cols="80" name="msg" style="float: left">${rvo.msg }</textarea>
				               <input type="hidden" name="bno" value="${vo.no }">
				               <input type="hidden" name="page" value="${curpage }">
				               <input type="hidden" name="no" value="${rvo.no }">
				               <input type=submit value="수정" style="height: 65px" class="btn btn-sm btn-primary">
				             </form>
       					</td>
       				</tr>
            		</table>
            	</c:forEach>
          </td>
        </tr>
        </c:if>
        
        
      </table>
      <table class="table">
        <tr>
          <td colspan="2">
             <form method="post" action="reply_insert.jsp">
               <textarea rows="3" cols="80" name="msg" style="float: left"></textarea>
               <input type="hidden" name="bno" value="${vo.no }">
               <input type="hidden" name="page" value="${curpage }">
               <input type=submit value="댓글쓰기" style="height: 65px" class="btn btn-sm btn-primary">
             </form>
          </td>
        </tr>
      </table>
		</div>
	</div>
</body>
</html>