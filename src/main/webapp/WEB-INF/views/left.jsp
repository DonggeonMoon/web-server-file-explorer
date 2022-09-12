<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css"/>
</head>
<body>
realPath: ${realPath}
<div id="jstree">
    <ul>
        <li>Root node 1
            <ul>
                <li id="child_node_1">Child node 1</li>
                <li>Child node 2</li>
            </ul>
        </li>
        <li>Root node 2</li>
    </ul>
</div>
<button>demo button</button>
<br/>
${file}
<br/>
---------------------------------------------
<br/>
<a href="/fileExplorer/add?filePath=${fn:replace(realPath, '\\', '/')}" target="frame_right">+ 파일 추가</a>
<br/>
<c:forEach var="item" items="${data}">
    <a href="/fileExplorer/read?filePath=${fn:replace(realPath, '\\', '/')}${fn:replace(item.getName(), '\\', '/')}" target="frame_right">${item.getName()}</a>
    <br/>
</c:forEach>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.1/jquery.min.js"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"/>
<script>
    $(function () {
        $('#jstree').jstree();

        $('#jstree').on("changed.jstree", function (e, data) {
            console.log(data.selected);
        });

        $('button').on('click', function () {
            $('#jstree').jstree(true).select_node('child_node_1');
            $('#jstree').jstree('select_node', 'child_node_1');
            $.jstree.reference('#jstree').select_node('child_node_1');
        });
    });
</script>
</body>
</html>
