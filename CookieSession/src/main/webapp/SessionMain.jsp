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
	// Center값을 변경해주기위해서 request 객체를 이용하여 center 값을 받아옴
	String center = request.getParameter("center");

	// 처음 SessionMain.jsp를 실행하면 null값이 실행되기에 null 처리를 해줌
	if (center == null) {
		center = "Center.jsp";
	}
%>
	<table border="1" width="800">
	<!-- top -->
		<tr height="150">
			<td align="center" colspan="2">
				 <jsp:include page="Top.jsp" />
					
			</td>
		</tr>
		
		<!-- left  -->
		<tr height="400">
			<td align="center" width="200">
				<jsp:include page="Left.jsp"></jsp:include>
			</td>
		
		
		<!-- center  -->
			<td align="center" width="600">
				<jsp:include page="<%=center %>"></jsp:include>
			</td>
		</tr>
		<!-- bottom -->
		<tr height="100">
			<td align="center" colspan="2">
				<jsp:include page="Bottom.jsp"></jsp:include>
			</td>
		</tr>
	</table>
</body>
</html>