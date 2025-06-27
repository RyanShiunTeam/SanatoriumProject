document.addEventListener("DOMContentLoaded", () => {

    const form = document.querySelector("#updateEmpForm")
    const msg = document.querySelector("#msg");
    const ctx =  window.location.pathname.split('/')[1];

    // 取出 query.js 傳過來的使用者資料
    const params = new URLSearchParams(window.location.search);

    const userID = params.get("userID") || "";
    const userName = params.get("userName") || "";
    const email = params.get("email") || "";
    const role = params.get("role") || "";
    const createdAt = params.get("createdAt") || "";

    // 將其設為表單預設值
    document.querySelector("#userID").textContent = userID;
    document.querySelector("#createdAt").textContent = createdAt;
    document.querySelector("#userName").value = userName;
    document.querySelector("#email").value = email;
    
    // 下拉式選單
    const roleSelect =  document.querySelector("#role")
    // 預設值為 URL 傳來的參數，避免 fetech 取得失敗
    roleSelect.value = role;

    // 修改表單提交
    form.addEventListener("submit", e => {
        e.preventDefault();

        const empInfo = {
            userID: userID,
            userName: document.querySelector("#userName").value.trim(),
            email: document.querySelector("#email").value.trim(),
            role: document.querySelector("#role").value
        };

        fetch(`/${ctx}/UpdateEmp`, {
            method : "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(empInfo)
        })
        .then(res => { if (!res.ok) throw new Error(`HTTP ${res.status}`); return res.json(); })
        .then(message => {
            msg.textContent = message;

            if (message === "修改成功") {
                form.reset();
            }
        })
        .catch(err => {
            console.log(err);
            msg.textContent = "發生錯誤，請稍後再試";
        }); 

    })

    // 去後端要權限總表
    fetch(`/${ctx}/GetRole`)
    .then(res => { if (!res.ok) throw new Error(`HTTP ${res.status}`); return res.json(); })
    .then(data => {
        roleSelect.innerHTML = "";
        // 字串陣列
        data.forEach(item => {
            const opt = document.createElement("option");
            opt.value = item;
            opt.textContent = item;
            roleSelect.appendChild(opt);
        })

        // 設定被選中的項目
        roleSelect.value = role;
    })
    .catch(err => {
      console.error('取得角色列表失敗', err);
    });
})