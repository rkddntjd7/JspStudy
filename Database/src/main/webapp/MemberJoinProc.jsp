<%@page import="java.sql.PreparedStatement"%>
<%@page import="com.mysql.cj.xdevapi.PreparableStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="model.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	request.setCharacterEncoding("utf-8");
	
	// 취미 부분은 별도로 읽어들여 다시 빈 클래스에 저장
	String[] hobby = request.getParameterValues("hobby");
	
	// 배열의 있는 내용을 하나의 스트링으로 저장
	String texthobby = "";
	for(int i = 0; i < hobby.length; i++) {
		texthobby += hobby[i] + " ";
	}

%>
	<!-- useeBean을 이용하여 한꺼번에 테이터를 받아옴 -->
	<jsp:useBean id="mbean" class="model.MemberBean">
		<jsp:setProperty name="mbean" property="*" /> <!-- 매핑 시키시오 -->
	</jsp:useBean>
<%
	mbean.setHobby(texthobby); // 기존 취미는 주소번지가 저장되기에 위에 배열의 내용을 하나의 스트링으로 저장한 변수 다시입력
	
	// 데이터 베이스클래스 객체 생성
	MemberDAO mdao = new MemberDAO();
	mdao.insertMember(mbean);
	
	// 회원 가입이 되었다면 회원 정보를 보여주는 페이지로 이동시킴
	response.sendRedirect("MemberList.jsp");
%>


	
	
</body>
</html>