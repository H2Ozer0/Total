<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 52491
  Date: 2019/12/11
  Time: 19:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
    <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/layui/layui.all.js" charset="utf-8"></script>
    <script type="text/javascript">

    </script>
    <style>
    body{
    overflow: hidden; /* 禁用所有滚动条 */
    background-image: url('${pageContext.request.contextPath}/static/no2.jpg');
    background-size: cover;
    background-position: center;
    }
    </style>
        </head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<div class="st-banner layui-bg-blue" style="height: 150px"></div>
<div class="home-information-box">
    <div class="information-headimg-box">
        <img src="/getAvatar?id=${sessionScope.myInfo.userId}" width="150px"/>
    </div>
</div>

<div class="text-center" style="margin-top: 80px">
    <div id="user_name" style="font-size: 24px">${sessionScope.myInfo.username}</div>
    <div id="user_id" style="font-size: 14px;color: #bbbbbb">id:${sessionScope.myInfo.userId}</div>
</div>

<div class="st-main" style="margin-top: 20px">
    <form class="layui-form">
        <label class="layui-form-label">相册</label>
        <div class="layui-input-block" style="xmargin-bottom: 10px">
            <select id="album_choose" name="album_choose" lay-verify="required">
                <option value="">请选择相册</option>
                <c:forEach var="album" items="${albumList}">
                    <option value="${album.albumID}">${album.albumName}</option>
                </c:forEach>
            </select>
        </div>
        <label class="layui-form-label">图片上传</label>
        <div class="layui-upload" style="margin-left: 110px;">
            <button type="button" class="layui-btn layui-btn-primary" id="test2">添加图片</button>
            <blockquote class="layui-elem-quote layui-quote-nm" style="margin-top: 10px;">
                预览图：
                <div class="layui-upload-list" id="demo2"></div>
            </blockquote>
        </div>


        <div class="layui-form-item">
            <div class="layui-input-block">
                <button type="button" id="btn_submit" class="layui-btn layui-btn-primary">立即提交</button>
                <button type="reset" class="layui-btn layui-btn-primary layui-btn-danger">重置</button>

            </div>
        </div>


    </form>

    <script>
        layui.use(['upload','form'], function(){
            var form = layui.form;
            form.render();
            var upload = layui.upload;
            upload.render({
                elem: '#test2'
                ,url: '${pageContext.request.contextPath}/albums/upload'
                ,data:{
                    albumId: $('#album_choose').val()
                }
                ,multiple: true
                ,field:'file'
                ,auto:false
                ,bindAction:'#btn_submit'
                ,choose:function (obj) {
                    obj.preview(function(index, file, result){
                        $('#demo2').append('<img src="'+ result +'" alt="'+ file.name +'" class="upload-img">')
                    });
                }
                ,before: function(obj){
                    this.data = {
                        albumId: $('#album_choose').val()
                    };
                    this.url = '${pageContext.request.contextPath}/albums/upload';
                    //预读本地文件示例，不支持ie8
                }
                ,done: function(res){
                    //上传完毕
                    if(res.status === 0){
                        layer.msg("上传成功！");
                        window.location.href = "${pageContext.request.contextPath}/albums/album_content?albumId="+this.data.albumId;
                    }else{
                        layer.msg("上传失败！");
                        window.location.reload();
                    }

                }
            });
        });
    </script>

</div>

</body>
</html>
