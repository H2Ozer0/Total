<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 52491
  Date: 2019/12/29
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<style>
    .personal-menu {
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
        border: 1px solid #ccc; /* 添加边框线 */
        padding: 10px; /* 可选：为内容添加一些内边距 */
    }

    .personal-menu ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .personal-menu dd {
        margin-bottom: 10px;
    }

    .personal-menu a {
        text-decoration: none;
        color: #333;
    }
    .personal-card-vertical {
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
        border: 1px solid #ccc; /* 添加边框线 */
        padding: 10px; /* 可选：为内容添加一些内边距 */
    }

    .personal-card-vertical ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .personal-card-vertical dd {
        margin-bottom: 10px;
    }

    .personal-card-vertical a {
        text-decoration: none;
        color: #333;
    }
</style>
<body>
<div class = "vertical" style="width: 250px">
    <div class="personal-card-vertical" style="margin-bottom: 20px;padding:10px">
        <div class="me-avatar content-center">
            <img src="${pageContext.request.contextPath}/getAvatar?username=${sessionScope.myInfo.username}" width="150px" height="150px"/>
        </div>
        <div class = "content-center" style="font-size: 24px">${sessionScope.myInfo.username}</div>
        <div class = "content-center" style="font-size: 14px;color: #bbbbbb">ID:${sessionScope.myInfo.userId}</div>
        <div class="content-center" style="font-size: 14px; color: #bbbbbb">
            <c:choose>
                <c:when test="${sessionScope.myInfo.admin}">
                    权限: 管理员
                </c:when>
                <c:otherwise>
                    权限: 用户
                </c:otherwise>
            </c:choose>
        </div>


            <a href="${pageContext.request.contextPath}/albums/createalbum_page" class = "content-center" target="_blank">
                <button type="button" class=" layui-btn layui-btn-primary}">创建相册</button>
            </a>
            <a href="/uploadPhoto" style="margin: 0 auto" target="_blank">
                <button type="button" class="layui-btn layui-btn-primary">上传照片</button>
            </a>


    </div>
    <div class="personal-menu" style="margin-bottom: 20px">
        <ul>
            <dd><a href="${pageContext.request.contextPath}/me/albums">管理相册</a></dd>
            <dd><a href="${pageContext.request.contextPath}/me/photos">管理照片</a></dd>
            <dd><a href="${pageContext.request.contextPath}/me/info">个人信息</a></dd>
        </ul>
    </div>

    <%--管理员才有的后台菜单--%>
    <c:if test="${sessionScope.myInfo.admin }">
        <div class="personal-menu" style="margin-bottom: 20px">
            <ul>
                <li><a href="${pageContext.request.contextPath}/admin/viewAllAlbums">管理用户相册</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/viewAllUsers">管理用户状态</a></li>
                <li><a href="/admin/operations">操作记录</a></li>

            </ul>
        </div>
    </c:if>
</div>
</body>
</html>
