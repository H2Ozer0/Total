<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>创建相册</title>
  <style>
    body {
      font: 14px/1.5 "PingFang SC","Lantinghei SC","Microsoft YaHei","HanHei SC","Helvetica Neue","Open Sans",Arial,"Hiragino Sans GB","微软雅黑",STHeiti,"WenQuanYi Micro Hei",SimSun,sans-serif;
      min-height: 1200px;
      background-color: #009688;
      color: white;
    }

    .st-main {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      margin-top: 20px;
    }

    form {
      display: flex;
      flex-direction: column;
      align-items: center;
      background-color: white;
      padding: 20px;
      border-radius: 5px;
    }

    div {
      margin-bottom: 15px;
      display: flex;
      align-items: center;
    }

    label {
      background-color: #009688;
      padding: 8px;
      border-radius: 4px;
      color: white;
      margin-right: 10px;
    }

    textarea, input {
      width: 100%;
      padding: 10px;
      box-sizing: border-box;
      margin-bottom: 10px;
      border: 1px solid #009688;
      border-radius: 4px;
      color: black; /* 修改文字颜色为黑色 */
    }

    #coverImage {
      display: block;
    }

    button {
      background-color: #009688;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }

    #previewContainer {
      display: flex;
      justify-content: center;
      align-items: center;
    }

    #authCheckboxContainer {
      flex-direction: row;
      margin-right: 10px;
    }
  </style>
</head>
<body>

<div class="st-main">
  <form id="albumForm" action="${pageContext.request.contextPath}/albums/createAlbum" method="post" enctype="multipart/form-data">
    <div>
      <label>相册名</label>
      <input id="title" type="text" name="title" required placeholder="请输入相册名" autocomplete="off">
    </div>

    <div>
      <label>相册描述</label>
      <textarea name="desc" placeholder="请输入描述"></textarea>
    </div>

    <div id="authCheckboxContainer">
      <label>是否分享</label>
      <div>
        <input type="checkbox" name="auth" id="authCheckbox">
        <input type="hidden" name="auth" id="authValue" value="">
      </div>
    </div>

    <div>
      <label>上传封面</label>
      <input type="file" name="coverImage" id="coverImage" accept="image/*" onchange="previewImage(this)">
      <div id="previewContainer"></div>
      <input type="hidden" name="coverImageBase64" id="coverImageBase64" value="">
    </div>

    <div>
      <button type="submit">立即提交</button>
      <button type="reset">重置</button>
    </div>
  </form>
</div>

<script>
  function previewImage(input) {
    var previewContainer = document.getElementById('previewContainer');
    previewContainer.innerHTML = '';

    if (input.files && input.files[0]) {
      var reader = new FileReader();

      reader.onload = function (e) {
        var imgElem = document.createElement('img');
        imgElem.src = e.target.result;
        imgElem.style.width = '150px';
        imgElem.style.height = '150px';
        previewContainer.appendChild(imgElem);

        document.getElementById('coverImageBase64').value = e.target.result;
      };

      reader.readAsDataURL(input.files[0]);
    }
  }

  document.getElementById('authCheckbox').addEventListener('change', function () {
    document.getElementById('authValue').value = this.checked ? 'true' : 'false';
  });
</script>
</body>
</html>
