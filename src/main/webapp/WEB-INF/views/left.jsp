<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>파일 탐색기</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css"/>
</head>
<body>
경로: <input type="text" style="width: 100%" value="${realPath}" readonly>
<hr>
<div id="jstree"></div>
<hr>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
<script>
    $(function () {
        $('#jstree').jstree({
            "plugins": ["changed", "contextmenu", "dnd", "massload", "search", "state", "unique", "wholerow"],
            'core': {
                'data': {
                    'url': function (node) {
                        return node.id === '#' ? '/api/getRoot' : '/api/getChildren'
                    },
                    'data': function (node) {
                        return {
                            'id': node.id
                        }
                    }
                },
                'check_callback': function (operation, node, node_parent, node_position, more) {
                    // operation can be 'create_node', 'rename_node', 'delete_node', 'move_node', 'copy_node' or 'edit'
                    // in case of 'rename_node' node_position is filled with the new node name
                    return operation === 'rename_node' ? true : false;
                }
            },
            "contextmenu": {
                "show_at_node": false,
                "items": {
                    "파일 추가": {
                        "label": "파일 추가",
                        "action": function (obj) {
                            alert("파일 추가");
                            console.log(obj)
                            window.parent.frame_right.document.location.href = "/fileExplorer/addFile?path=" + obj.reference[0].id.replaceAll("\\", "/").replaceAll("_anchor", "");
                            $.vakata.context.hide()
                        },
                        "_class": "class",
                    },
                    "디렉터리 추가": {
                        "label": "디렉터리 추가",
                        "action": function (obj) {
                            alert("디렉터리 추가");
                            console.log(obj)
                            window.parent.frame_right.document.location.href = "/fileExplorer/addDirectory?path=" + obj.reference[0].id.replaceAll("\\", "/").replaceAll("_anchor", "");
                            $.vakata.context.hide()
                        },
                        "_class": "class",
                    },
                    "수정": {
                        "label": "편집",
                        "action": function (obj) {
                            alert("편집");
                            window.parent.frame_right.document.location.href = "/fileExplorer/read?path=" + obj.reference[0].id.replaceAll("\\", "/").replaceAll("_anchor", "");
                            $.vakata.context.hide()
                        },
                        "_class": "class",
                    },
                    "이름 바꾸기": {
                        "label": "이름 바꾸기",
                        "action": function (obj) {
                            alert("이름 바꾸기");
                            window.parent.frame_right.document.location.href = "/fileExplorer/rename?path=" + obj.reference[0].id.replaceAll("\\", "/").replaceAll("_anchor", "");
                            $.vakata.context.hide()
                        },
                        "_class": "class",
                    }
                }
            }
        });
        // 7 bind to events triggered on the tree
        $('#jstree').on("changed.jstree", function (e, data) {
            console.log(data.selected);
        });
        // 8 interact with the tree - either way is OK
        $('button').on('click', function () {
            $('#jstree').jstree(true).select_node('child_node_1');
            $('#jstree').jstree('select_node', 'child_node_1');
            $.jstree.reference('#jstree').select_node('child_node_1');
        });
    });
</script>
</body>
</html>
