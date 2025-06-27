document.addEventListener('DOMContentLoaded', function() {
  const step1 = document.querySelector('#step1');
  const step2 = document.querySelector('#step2');
  const requestBtn = document.querySelector('#requestCodeBtn');
  const verifyBtn = document.querySelector('#verifyBtn');
  const userIdInput = document.querySelector('#userID');
  const codeInput = document.querySelector('#code');
  const newPwdInput = document.querySelector('#newPwd');
  const step1Msg = document.querySelector('#step1Msg');
  const step2Msg = document.querySelector('#step2Msg');
  const ctx =  window.location.pathname.split('/')[1];

  // Step 1: 送出 UserID 取得驗證碼
  requestBtn.addEventListener('click', () => {
    const userID = userIdInput.value.trim();
    if (!userID) {
      step1Msg.style.color = 'red';
      step1Msg.textContent = '請輸入使用者 ID';
      return;
    }
    step1Msg.textContent = '';

    // 根據使用者 ID 寄送重設密碼 email
    fetch(`/${ctx}/RequestPwdReset`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      body: JSON.stringify({ userID: parseInt(userID) })
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        step1Msg.style.color = 'green';
        step1Msg.textContent = data.message;
        step1.classList.add('hidden');
        step2.classList.remove('hidden');
      } else {
        step1Msg.style.color = 'red';
        step1Msg.textContent = data.message;
      }
    })
    .catch(() => {
      step1Msg.style.color = 'red';
      step1Msg.textContent = '發生錯誤，請稍後再試';
    });
  });

  // Step 2: 驗證驗證碼並重設密碼
  verifyBtn.addEventListener('click', () => {
    const userID = userIdInput.value.trim();
    const code = codeInput.value.trim();
    const newPwd = newPwdInput.value.trim();
    if (!code || !newPwd) {
      step2Msg.style.color = 'red';
      step2Msg.textContent = '請填寫驗證碼與新密碼';
      return;
    }
    step2Msg.textContent = '';

    // 檢查驗證碼是否正確
    fetch(`/${ctx}/VerifyPwdReset`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      body: JSON.stringify({ userID: parseInt(userID), code, newPwd })
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        step2Msg.style.color = 'green';
        step2Msg.textContent = data.message;
        // 隱藏確認按鈕，避免重複提交
        verifyBtn.style.display = 'none';
        // 成功後顯示 回到登入 按鈕
        if (!document.querySelector('#backToLoginBtn')) {
          const backBtn = document.createElement('button');
          backBtn.id = 'backToLoginBtn';
          backBtn.textContent = '回到登入';
          backBtn.style.marginTop = '10px';
          backBtn.addEventListener('click', () => {
            window.location.href = 'login.html';
          });
          step2.appendChild(backBtn);
        }
      } else {
        step2Msg.style.color = 'red';
        step2Msg.textContent = data.message;
      }
    })
    .catch(() => {
      step2Msg.style.color = 'red';
      step2Msg.textContent = '發生錯誤，請稍後再試';
    });
  });
});
