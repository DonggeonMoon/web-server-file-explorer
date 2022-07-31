<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script>

    </script>
</head>
<body>
<form action="/fileExplorer/write" method="post">
    <input type="hidden" name="filePath" value="<c:out value="${filePath}"/>">
    <div><c:out value="${message}"/></div>
    <c:choose>
        <c:when test="${data == ''}">
            파일 이름:
            <input type="text" name="fileName">
            <br/>
            파일 내용:
            <br/>
            <textarea name="fileContent" rows="50" cols="150">${data}</textarea>
            <button>생성</button>
        </c:when>
        <c:when test="${data != null}">
            파일 내용:
            <br/>
            <textarea name="fileContent" rows="50" cols="150">${data}</textarea>
            <button>수정</button>
            <form action="/fileExplorer/delete" method="post" style="display:inline">
                <input type="hidden" name="filePath" value="<c:out value="${filePath}"/>">
                <button>삭제</button>
            </form>
        </c:when>
        <c:otherwise/>
    </c:choose>
</form>
</body>
</html>
