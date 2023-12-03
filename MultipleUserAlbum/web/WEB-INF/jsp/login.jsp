<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <title>登录界面</title>
  <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
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

    .login-wrapper {
      background-color: #fff; /* 白色不透明背景 */
      width: 358px;
      height: 588px;
      border-radius: 15px;
      padding: 0 50px;
      position: relative;
      text-align: center;
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
      height: 20px; /* 根据需要调整高度 */
      background-color: #2b43e1; /* 或其他你想要的颜色 */
      margin-top: 20px; /* 调整与上方内容的间距 */
      border-radius: 10px; /* 添加圆角 */
    }
    a:hover {
      color: #0656a9;
    }
    /* ... (你现有的样式) ... */

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





    /* ... (你现有的样式) ... */

  </style>
</head>

<body>
<div class="container">
  <div class="login-wrapper">
    <div class="header-wrapper">
      <div class="header">登录界面</div>
      <div class="description">欢迎访问多用户相册系统</div>
    </div>


    <!-- 添加一个 id 属性，以便在 JavaScript 中引用 -->
    <form id="loginForm">
      <div class="form-wrapper">
        <input type="text" name="username" placeholder="用户名" class="input-item">
        <input type="password" name="password" placeholder="密码" class="input-item">
        <button type="submit" class="btn">登录</button> <!-- 使用 button 元素作为提交按钮 -->
      </div>
    </form>



    <div class="msg">

      <a href="${pageContext.request.contextPath}/register_page">还没有账户？点击注册</a>
    </div>
    <div class="bottom-decoration"></div>
  </div>

</div>

</body>

<script>
  $(document).ready(function() {
    // 监听表单提交事件
    $('#loginForm').on('submit', function(event) {
      // 阻止表单默认提交行为
      event.preventDefault();

      // 获取用户名和密码
      var username = $('input[name="username"]').val();
      var password = $('input[name="password"]').val();

      // 发送Ajax请求
      $.ajax({
        type: 'POST',
        url: '${pageContext.request.contextPath}/login', // 替换为你的后端接口地址
        data: {
          username: username,
          password: password
        },
        success: function(response) {
          // 处理后端返回的响应
          console.log(response);
        },
        error: function(error) {
          // 处理错误
          console.log(error);
        }
      });
    });
  });
</script>
</html>