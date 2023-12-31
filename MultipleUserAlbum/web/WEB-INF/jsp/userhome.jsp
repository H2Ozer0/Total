
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${userInfo.username}的主页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
    <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/layui/layui.all.js"></script>
    <link rel="stylesheet" href="https://cdn.staticfile.org/dropzone/5.9.2/min/dropzone.min.css">
    <script src="https://cdn.staticfile.org/dropzone/5.9.2/min/dropzone.min.js"></script>
    <style>
        .layui-tab-item {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh; /* 使容器充满整个视口高度 */
        }
        body.bg-gray {
            overflow: hidden; /* 禁用所有滚动条 */
            background-image: url('${pageContext.request.contextPath}/static/no2.jpg');
            background-size: cover;
            background-position: center;
        }

        #myInfoContent {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        #userhomechange{
            width: 300px; /* 设置固定宽度 */
            margin-left: auto;
            margin-right: auto;
            position: fixed;
            left: 0;
            right: 0;
        }
        .custom-btn {
            background-color: #336699; /* 自定义颜色的背景色 */
            color: white; /* 文字颜色 */
        }
        .custom-btn2 {
            background-color: #990033; /* 自定义颜色的背景色 */
            color: white; /* 文字颜色 */
        }
        .bg-blue {
            background-color: #666699;
        }
    </style>
    <style>
        .st-main {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh; /* 使容器充满整个视口高度 */
        }
    </style>

</head>
<body class="bg-gray">

<jsp:include page="header.jsp"></jsp:include>

<div class="st-banner bg-blue" style="height: 150px"></div>

<div style="background: white">
    <div class="home-information-box">
        <div class="information-headimg-box">
            <img src="${pageContext.request.contextPath}/getAvatar?username=${myInfo.username}" width="150px" height="150px"/>
        </div>
    </div>

    <div class="text-center" style="margin-top: 80px">
        <div id="user_name" style="font-size: 24px">${myInfo.username}</div>
        <div id="user_id" style="font-size: 14px;color: #bbbbbb">id:${myInfo.userId}</div>

    </div>
</div>

<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
    <ul class="layui-tab-title" style="text-align: center; background:white">
        <li class="layui-this" style="width: 300px" id="myInfoTab">个人信息</li>
    </ul>
    <div class="st-main">
        <div class="layui-tab-content"id="userhomechange">
            <%--编辑个人信息--%>
            <div class="layui-tab-item layui-show" id="myInfoContent">
                <form class="layui-form" id="myInfoForm">
                    <div class="layui-form-item">


                        <label class="layui-form-label layui-col-md2 layui-col-md-offset3">用户ID</label>
                        <div class="layui-input-inline layui-col-md4 ">
                            <input type="text" name="userId" id="userId" value="${myInfo.userId}" class="layui-input" disabled>
                        </div>

                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label layui-col-md2 layui-col-md-offset3">用户名</label>
                        <div class="layui-input-inline layui-col-md4">
                            <input type="text" name="username" id="username" value="${myInfo.username}" class="layui-input">
                            <button type="button" class="layui-btn custom-btn layui-btn-sm layui-btn-normal" onclick="modifyInfo('username')">修改</button>
                            <button class="layui-btn layui-btn-sm custom-btn layui-btn-normal" lay-submit lay-filter="saveEmail">保存</button>

                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label layui-col-md2 layui-col-md-offset3">邮箱</label>
                        <div class="layui-input-inline layui-col-md4 ">
                            <input type="text" name="email" id="email" value="${myInfo.email}" class="layui-input" >
                            <button type="button" class="layui-btn custom-btn layui-btn-sm layui-btn-normal" onclick="modifyInfo('email')">修改</button>
                            <button class="layui-btn layui-btn-sm custom-btn layui-btn-normal" lay-submit lay-filter="saveEmail">保存</button>

                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label layui-col-md-offset3">头像上传</label>
                        <div class="layui-input-block">
                            <button type="button" class="layui-btn custom-btn" id="avatarUploadBtn">选择头像</button>
                            <input type="file" name="avatarFile" id="avatarFile" style="display: none;" accept="image/*">
                            <div class="layui-upload-list" id="avatarPreview"></div>

                        </div>
                    </div>
                    <div class="layui-form-item layui-col-md-offset5">
                        <button type="button" id="btn_submit_avatar" class="custom-btn layui-btn">上传头像</button>
                    </div>
                    <script src="https://cdn.staticfile.org/jquery/3.6.0/jquery.min.js"></script>
                    <script src="https://cdn.staticfile.org/layui/2.5.7/layui.min.js"></script>
                    <script src="your-dropzone-library.js"></script>
                    <script src="your-avatar-upload-script.js"></script>


                </form>

            </div>
        </div>
    </div>
</div>

<!-- 添加每个信息独立的保存按钮 -->
<!-- 例如：<button class="layui-btn layui-btn-sm" lay-submit lay-filter="saveEmail">保存</button> -->

</form>
</div>
</div>
</div>
</div>
    </div>

                    <!-- 添加每个信息独立的保存按钮 -->
                    <!-- 例如：<button class="layui-btn layui-btn-sm" lay-submit lay-filter="saveEmail">保存</button> -->

                </form>
            </div>
        </div>
    </div>
</div>
<script>
    layui.use(['form', 'layer', 'element'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var element = layui.element;
        // 处理信息修改的函数
        window.modifyInfo = function (fieldName) {
            // 启用输入框以便编辑
            $('#' + fieldName).removeAttr('disabled');
            // 将焦点设置到输入框
            $('#' + fieldName).focus();
            // 显示保存按钮
            $('#saveBtn-' + fieldName).show();
        };
        // 提交表单数据到服务器的通用函数
        function submitForm(fieldValue, url) {
            // 发送 AJAX 请求到服务器
            $.ajax({
                url: url,
                type: 'POST',
                data: { fieldValue: fieldValue },
                success: function (response) {
                    layer.open({
                        title: '提示',
                        content: response.msg,
                        shade: 0.5,
                        yes: function () {
                            layer.closeAll();
                            //刷新页面
                            window.location.href = "${pageContext.request.contextPath}/userhome";
                        }
                    });
                },
                error: function (error) {
                    // 处理来自服务器的错误响应
                    console.error(error);
                }
            });
        }

        // 提交用户名表单数据到服务器
        form.on('submit(saveUsername)', function (data) {
            var fieldValue = data.field.username;
            var url = '${pageContext.request.contextPath}/me/updateUsername';
            submitForm(fieldValue, url);
            return false;
        });

        // 提交邮箱表单数据到服务器
        form.on('submit(saveEmail)', function (data) {
            var fieldValue = data.field.email;
            var url = '${pageContext.request.contextPath}/me/updateEmail';
            submitForm(fieldValue, url);
            return false;
        });

        // 提交个人描述表单数据到服务器
        form.on('submit(saveDescription)', function (data) {
            var fieldValue = data.field.description;
            var url = '${pageContext.request.contextPath}/me/updateDescription';
            submitForm(fieldValue, url);
            return false;
        });



        // 头像上传按钮点击事件
        $('#avatarUploadBtn').on('click', function () {
            // 触发文件选择
            $('#avatarFile').click();
        });

        // 文件选择发生变化时
        $('#avatarFile').on('change', function () {
            var fileInput = this;
            var file = fileInput.files[0];

            // 预览头像
            if (file) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#avatarPreview').html('<img src="' + e.target.result + '" alt="' + file.name + '" class="upload-img">');
                };
                reader.readAsDataURL(file);
            }
        });

        // 提交头像上传
        $('#btn_submit_avatar').on('click', function () {
            var fileInput = document.getElementById('avatarFile');
            var file = fileInput.files[0];

            if (!file) {
                layer.msg('请选择头像文件');
                return;
            }

            // 使用 FormData 对象进行异步文件上传
            var formData = new FormData();
            formData.append('avatarFile', file);

            // 使用 AJAX 提交 FormData 对象
            $.ajax({
                url: '${pageContext.request.contextPath}/me/uploadAvatar', // 上传接口，替换为实际处理头像上传的URL
                type: 'POST',
                data: formData,
                processData: false,  // 告诉 jQuery 不要去处理发送的数据
                contentType: false,  // 告诉 jQuery 不要去设置 Content-Type 请求头
                success: function (res) {
                    // 头像上传完毕
                    if (res.status === 0) {
                        layer.msg("上传成功！");
                        window.location.href = "${pageContext.request.contextPath}/userhome";
                        // 如果有需要，可以在这里处理上传成功后的逻辑
                    } else {
                        layer.msg("上传失败！");
                        // 如果有需要，可以在这里处理上传失败后的逻辑
                    }
                },
                error: function () {
                    layer.msg("上传失败！");
                }
            });
        });



    });
</script>




</body>
</html>
