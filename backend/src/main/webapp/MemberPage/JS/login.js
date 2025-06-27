document.addEventListener("DOMContentLoaded", () => {

    const form = document.querySelector("#loginForm");
    const msg  = document.querySelector("#msg");
    const ctx =  window.location.pathname.split('/')[1];
    
    form.addEventListener("submit", e => {
      e.preventDefault();

      // 使用者輸入資訊
    const userinfo = {
      userID: document.querySelector("#userID").value.trim(),
      password: document.querySelector("#password").value.trim()
    };
    
    if (!userID || !password) {
      msg.textContent = "請填入員工編號與密碼";
      return;
    }
    
    fetch(`/${ctx}/Login`, {
      method: 'POST',
      // 宣告物件為 JSON 格式
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userinfo)
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        // 將當前使用者資訊存在前端
        localStorage.setItem('loginUser', JSON.stringify(data.user));
        // 登入成功 → 導向後台首頁
        window.location.href = `/${ctx}/backHome.html`;
      } else {
        msg.textContent = data.message;
      }
    })
    .catch(err => console.error(err));
    });    
});