<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html>
<body>
	<%!String SCOPE = "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile"
			+ "+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email"
			+ "+https%3A%2F%2Fwww.googleapis.com/auth/calendar"
			+ "+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar.readonly"
			+ "+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile"
			+ "+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email";
	String STATE = "";
	String REDIRECT_URI = "";
	String RESPONSE_TYPE = "code";
	String CLIENT_ID = "";%>


	<a href="https://accounts.google.com/o/oauth2/auth?response_type=<%=RESPONSE_TYPE%>&client_id=<%=CLIENT_ID%>&redirect_uri=<%=REDIRECT_URI%>&scope=<%=SCOPE%>">以Google帳戶登入</a>

</body>
</html>

