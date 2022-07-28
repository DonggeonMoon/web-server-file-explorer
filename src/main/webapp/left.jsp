<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
realPath: ${realPath}
<br/>
${file}
<br/>
---------------------------------------------
<br/>
<c:forEach var="item" items="${data}">
    <a href="/fileExplorer/read?filePath=${fn:replace(realPath, '\\', '/')}${fn:replace(item.getName(), '\\', '/')}" target="frame_right">${item.getName()}</a>
    <br/>
</c:forEach>
</body>
</html>
