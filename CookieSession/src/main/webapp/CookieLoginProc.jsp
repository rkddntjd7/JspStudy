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
	
		// 아이디 저장 체크박스가 체크되었는지 판단 여부
		String save = request.getParameter("save");
		// 아이디 값을 저장
		String id = request.getParameter("id");
		
		// 체크되었는지 비교 판단
		if (save != null) { //아이디 저장이 눌렸다면
			
			// 쿠키를 사용하려면 - 쿠키클래스를 생성해주어여함
			Cookie cookie = new Cookie("id", id); // 1번재 String 키 값, 2번째 String 해당하는 value값
			// 쿠키 유효시간 설정
			cookie.setMaxAge(60*3); // 10분간 유효
			
			// 사용자에게 쿠키값을 넘겨줌
			response.addCookie(cookie);
			
			out.write("쿠키 생성완료");
		} 
		
		
		
		
	%>
	
</body>
</html>