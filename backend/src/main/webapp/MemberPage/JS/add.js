document.addEventListener("DOMContentLoaded", () => {

    const form = document.querySelector("#addEmpForm");
    const msg  = document.querySelector("#msg");
    const ctx =  window.location.pathname.split('/')[1];

    form.addEventListener("submit", e => {
        e.preventDefault();

        const empInfo = {
            userName: document.querySelector("#empName").value.trim(),
            password: document.querySelector("#password").value.trim(),
            email: document.querySelector("#email").value.trim()
        };

        fetch(`/${ctx}/AddEmp`, {
            method: 'POST',
            // 宣告物件為 JSON 格式
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(empInfo)
        })
        .then(res => { if (!res.ok) throw new Error(`HTTP ${res.status}`); return res.json(); })
        .then(message => {
            msg.textContent = message;

            if (message === "新增成功") {
                form.reset();
            }
        })
        .catch(err => {
            console.log(err);
            msg.textContent = "發生錯誤，請稍後再試";
        }); 
    });
});