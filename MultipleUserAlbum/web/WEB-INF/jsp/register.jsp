<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <title>注册界面</title>
  <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" type="text/css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/st-style.css" type="text/css"/>
  <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js" ></script>
  <script src="${pageContext.request.contextPath}/static/layui/layui.all.js" charset="UTF-8"></script>
  <style>
    * {
      margin: 0;
      padding: 0;
    }

    html {
      height: 100%;
    }

    body {
      height: 100%;
      overflow: hidden; /* Prevent scrolling */
    }

    .container {
      height: 100%;
      background-image: url('${pageContext.request.contextPath}/static/nnu.png');
      background-size: cover;
      background-position: center;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative; /* 添加相对定位 */
    }

    .register-wrapper {
      background-color: #fff;
      width: 358px;
      height: 700px;
      border-radius: 15px;
      padding: 0 50px;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      position: fixed;
      text-align: center;

      /* 添加阴影效果 */
      box-shadow: 12px 12px 15px rgba(0,0,0,0.9)/*blur值为15px*/
    }


    .header-wrapper {
      position: relative;
    }

    .header {
      font-size: 38px;
      font-weight: bold;
      color: #050b15;
      line-height: 200px;
    }

    .description {
      position: absolute;
      top: 170px; /* 调整向上的位置 */
      right: 0; /* 靠右对齐 */
      font-size: 16px;
      color: #333;
      text-align: right; /* 文字靠右对齐 */
      font-style: italic; /* 将文本设置为斜体 */
    }

    .form-wrapper {
      margin-top: 20px;
    }

    .input-item {
      display: block;
      width: 100%;
      margin-bottom: 20px;
      border: 0;
      padding: 10px;
      border-bottom: 1px solid rgb(60, 63, 65);
      font-size: 15px;
      color: #3c3f41;
      background: transparent;
      outline: none;
    }

    .input-item::placeholder {
      text-transform: uppercase;
      color: rgb(60, 63, 65);
    }

    .btn {
      text-align: center;
      padding: 10px;
      width: 100%;
      margin-top: 40px;
      background-image: linear-gradient(to right, #2b43e1, #2b43e1);
      color: #ffffff;
      cursor: pointer;
    }

    .btn:hover {
      background-image: linear-gradient(to right, #2b85e1, #2b85e1);
    }

    .msg {
      text-align: center;
      line-height: 88px;
      color: #0656a9;
    }

    a {
      text-decoration-line: none;
      color: #0656a9;
    }

    .bottom-decoration {
      width: 100%;
      height: 60px; /* 调整高度 */
      background-color: rgb(222, 218, 243); /* 设置半透明背景颜色 */
      border-radius: 0 0 15px 15px; /* 上方直角，下方圆角 */
      border: transparent;
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
    }
  </style>
</head>

<body>
<div class="container">
  <div class="register-wrapper">
    <div class="header-wrapper">
      <div class="header">注册界面</div>
      <div class="description">欢迎加入多用户相册系统</div>
    </div>

    <!-- 使用 button 元素作为提交按钮 -->
    <form id="registerForm">
      <div class="form-wrapper">
        <input type="text" name="username" placeholder="用户名" class="input-item">
        <input type="password" name="password" placeholder="密码" class="input-item">
        <input type="text" name="email" placeholder="邮箱" class="input-item">
        <input type="text" name="description" placeholder="描述" class="input-item">
        <button type="submit" class="btn">注册</button>
      </div>
    </form>


    <div class="msg">
      <a href="${pageContext.request.contextPath}/login_page">已有账户？点击登录</a>
    </div>
    <div class="bottom-decoration"></div>
  </div>
</div>
<script>
  $(document).ready(function() {
    // 监听表单提交事件
    $('#registerForm').on('submit', function(event) {
      // 阻止表单默认提交行为
      event.preventDefault();

      // 获取表单数据
      var formData = {
        username: $('input[name="username"]').val(),
        password: $('input[name="password"]').val(),
        email: $('input[name="email"]').val(),
        description: $('input[name="description"]').val()
      };

      // 发送Ajax请求
      $.ajax({
        type: 'POST',
        url: '${pageContext.request.contextPath}/Regestion',
        data: formData,
        success: function(result) {
        console.log(result);
        if(result.status==0)
        {
          layer.open({
            title: '注册成功',

            shade: 0.5,
            yes: function () {
              layer.closeAll();
              //刷新页面
              window.location.href = "${pageContext.request.contextPath}/login_page";
            }
          });
        }else if (result.status == -1) {
          layer.open({
            title: '用户名已存在',
            yes: function () {
              layer.closeAll();
              window.location.href = "${pageContext.request.contextPath}/register_page";

            }
          });
        }
        },
        error: function(error) {
          // 处理错误
          console.log(error);
        }
      });
    });
  });

</script>
</body>

</html>
