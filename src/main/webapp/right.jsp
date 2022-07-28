<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script>

    </script>
</head>
<body>
<form action="/fileExplorer/right" method="post">
    <div><c:out value="${message}"/></div>
    <c:choose>
        <c:when test="${not empty data}">
            파일 내용:
            <br/>
            <textarea name="fileContent" rows="50" cols="150">${data}</textarea>
            <button>수정</button>
            <button type="button">삭제</button>
        </c:when>
        <c:otherwise/>
    </c:choose>
</form>
</body>
</html>
