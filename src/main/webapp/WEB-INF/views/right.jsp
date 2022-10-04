<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script>

    </script>
</head>
<body>
${message}
${message2}
<c:choose>
    <c:when test="${data == '' || data == null}">
    </c:when>
    <c:when test="${mode == 'addFile'}">
        <form action="/fileExplorer/addFile" method="post">
            <input type="hidden" name="path" value="<c:out value="${path}"/>">
            <div>파일 이름:</div>
            <input type="text" name="fileName" value=""/>">
            <div>파일 내용:</div>
            <textarea name="fileContent" rows="50" cols="150">${data}</textarea>
            <button>생성</button>
        </form>
    </c:when>
    <c:when test="${mode == 'addDirectory'}">
        <form action="/fileExplorer/addDirectory" method="post">
            <input type="hidden" name="path" value="<c:out value="${path}"/>">
            <div>파일 이름:</div>
            <input type="text" name="fileName" value=""/>">
            <div>파일 내용:</div>
            <textarea name="fileContent" rows="50" cols="150">${data}</textarea>
            <button>생성</button>
        </form>
    </c:when>
    <c:when test="${mode == 'read'}">
        <form action="/fileExplorer/write" method="post">
            <input type="hidden" name="path" value="<c:out value="${path}"/>">
            <div><c:out value="${message}"/></div>
            <div>파일 이름:</div>
            <input type="text" name="fileName" value="<c:out value="${fileName}"/>">
            <div>파일 내용:</div>
            <textarea name="fileContent" rows="50" cols="150">${data}</textarea>
            <div>
                <button>수정</button>
            </div>
        </form>
    </c:when>
    <c:when test="${mode == 'renameFile'}">
        <form action="/fileExplorer/renameFile" method="post">
            <input type="hidden" name="path" value="<c:out value="${path}"/>">
            <div>파일 이름:</div>
            <input type="text" name="fileName" value="<c:out value='${fileName}'/>">
            <div>
                <button>수정</button>
            </div>
        </form>
    </c:when>
    <c:when test="${mode == 'renameDirectory'}">
        <form action="/fileExplorer/renameDirectory" method="post">
            <input type="hidden" name="path" value="<c:out value="${path}"/>">
            <div>파일 이름:</div>
            <input type="text" name="fileName" value="<c:out value='${fileName}'/>">
            <div>
                <button>수정</button>
            </div>
        </form>
    </c:when>
    <c:otherwise/>
</c:choose>
</body>
</html>
