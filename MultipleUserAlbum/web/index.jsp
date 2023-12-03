<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>首页</title>
    <!-- 可以在这里添加其他头部信息，如样式表、脚本等 -->
</head>

<body>
<script>
    // 使用 JavaScript 在页面加载时重定向到 login.jsp
    window.location.href = "${pageContext.request.contextPath}/login_page";
</script>
</body>

</html>
