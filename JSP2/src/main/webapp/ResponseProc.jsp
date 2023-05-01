<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>이 페이지는 로그인 정보가 넘어오는 페이지 입니다.</h2>
	
<%
	request.setCharacterEncoding("utf-8"); // post 한글 처리
	
	String id = request.getParameter("id"); // request 객체에 담겨진 사용자 정보 중 id만 추출
	
	//response.sendRedirect("ResponseRedirect.jsp"); // 흐름제어
	
%>

	<jsp:forward page="ResponseRedirect.jsp">
		<jsp:param value="mmmm" name="id"/>
		<jsp:param value="1234" name="phone"/>
	</jsp:forward>  <!-- 흐름제어 -->

	<h3> 아이디는 <%=id %></h3>
</body>
</html>