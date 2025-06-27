// 先讀取登入使用者資料
const loginUser = JSON.parse(localStorage.getItem('loginUser') || '{}');
const userRole = loginUser.role || '';
// 當前使用者權限
window.userRole = userRole;

// 用同一個 container listener 綁定元素
document.addEventListener("DOMContentLoaded", () => {
    const from = document.querySelector("#queryForm");
    const getAll = document.querySelector("#getAll");
    const banList = document.querySelector("#banList");
    const resultDiv = document.querySelector("#resultBox");
    const ctx =  window.location.pathname.split('/')[1];

    // 定義查詢全部員工的 function
    function fetchAllEmployees() {
    resultDiv.innerHTML = "";
    fetch(`/${ctx}/QueryEmp`)
        .then(res => { if (!res.ok) throw new Error(`HTTP ${res.status}`); return res.json(); })
        .then(data => renderTable(data))
        .catch(err => {
            console.error(err);
            resultDiv.textContent = "發生錯誤，請稍後再試";
        });
    }

    // 查詢單筆
    from.addEventListener("submit", e => {
        e.preventDefault();
        
        const queryName = {
            userName: document.querySelector("#userName").value.trim()
        };
        
        // 判斷是否為空字串
        if (!queryName.userName) {
            resultDiv.textContent = "請輸入員工姓名後再查詢 !";
            return;
        }
        // 先不顯示查詢結果表格
        resultDiv.innerHTML = "";
    
        
        fetch(`/${ctx}/QueryEmp`, {
            method: 'POST',
            // 宣告物件為 JSON 格式
            headers: {
            'Content-Type': 'application/json'
            },
            body: JSON.stringify(queryName)
        })
        // 確認請求是否成功
        .then(res => { if (!res.ok) throw new Error(`HTTP ${res.status}`); return res.json(); })
        .then(data => renderTable(data))
        .catch(err => {  
            console.log(err);
            resultDiv.textContent = "發生錯誤，請稍後在試";
        });    
    });

    // 查詢全部用呼叫函式
    getAll.addEventListener("click", fetchAllEmployees);

    // 頁面載入完成後自動查詢全部員工
    fetchAllEmployees();

    // 檢視停權員工名單
    if (userRole === "Admin") {
        banList.style.display = "inline-block";
        banList.addEventListener("click", () => {
            fetch(`/${ctx}/GetBanList`)
            .then(res => { if (!res.ok) throw new Error(`HTTP ${res.status}`); return res.json(); })
            .then(data => renderBanTable(data))
            .catch(err => {
                console.log(err);
                resultDiv.textContent = "發生錯誤，請稍後再試";
            });
        })
    }


    // 查詢結果顯示 (一般員工表格)
    function renderTable(data) {
        // 清空上一筆結果
        resultDiv.innerHTML = '';
        if (data.find && Array.isArray(data.empList)) {
            // 動態決定欄位
            const cols = ['ID','姓名','Email','權限','到職日'];
            if (userRole === 'Admin') cols.push('資料異動');

            // 建立表格與表頭
            const table = document.createElement('table');
            table.setAttribute('border', '1');
            table.style.margin = 'auto';
            const header = document.createElement('tr');
            cols.forEach(text => {
                const th = document.createElement('th');
                th.textContent = text;
                header.appendChild(th);
            });
            table.appendChild(header);

            // 塞入資料列
            data.empList.forEach(emp => {
                const tr = document.createElement('tr');
                // 前面固定欄位
                ['userID','userName','email','role','createdAt'].forEach(key => {
                    const td = document.createElement('td');
                    td.textContent = emp[key] || '';
                    tr.appendChild(td);
                });
                // Admin 專屬操作欄
                if (userRole === 'Admin') {
                    const opTd = document.createElement('td');
                    const editBtn = document.createElement('button');
                    editBtn.textContent = '修改';
                    // 實作修改按鈕，用 URL 帶參數給下個 page 使用
                    editBtn.addEventListener("click", () => {
                        const params = new URLSearchParams({
                            userID: emp.userID,
                            userName: emp.userName,
                            email: emp.email,
                            role: emp.role,
                            createdAt: emp.createdAt
                        }).toString();
                        window.location.href = `updateEmp.html?${params}`;
                    });

                    // 實作停權按鈕
                    const delBtn = document.createElement('button');
                    delBtn.textContent = '停權';
                    delBtn.addEventListener("click", () => {
                        if (!confirm(`確定要停權 ${emp.userName} 嗎？`)) return;
                        fetch(`/${ctx}/BanEmp`, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ userID: emp.userID })
                        })
                         .then(res => { if (!res.ok) throw new Error(`HTTP ${res.status}`); return res.json(); })
                         .then(data => {
                            if (data.success) tr.remove();
                            else alert(data.message || "停權失敗");
                         })
                         .catch(err => {
                            console.err(err);
                            alert("停權失敗，稍後在試");
                         });
                    });

                    opTd.appendChild(editBtn);
                    opTd.appendChild(delBtn);
                    tr.appendChild(opTd);
                }
                table.appendChild(tr);
            });

            resultDiv.appendChild(table);
        } else {
            resultDiv.textContent = data.message;
        }
    }

    // 停權員工列表
    function renderBanTable(data) {
        resultDiv.innerHTML = '';
        if (data.find && Array.isArray(data.banList)) {
        // 停權名單標題欄位
        const cols = ['ID','姓名','Email','權限','停權日期','恢復權限'];
        const table = document.createElement('table');
        table.setAttribute('border', '1');
        table.style.margin = 'auto';
        const header = document.createElement('tr');
        cols.forEach(text => {
            const th = document.createElement('th');
            th.textContent = text;
            header.appendChild(th);
        });
        table.appendChild(header);

        // 填入每筆停權員工資料列
        data.banList.forEach(emp => {
            const tr = document.createElement('tr');
            // 基本欄位
            ['userID','userName','email','role','updatedAt'].forEach(key => {
            const td = document.createElement('td');
            td.textContent = emp[key] || '';
            tr.appendChild(td);
            });

            // 恢復按鈕
            const opTd = document.createElement('td');
            const restoreBtn = document.createElement('button');
            restoreBtn.textContent = '恢復';

            // 呼叫後端恢復權限
            restoreBtn.addEventListener('click', () => {
            if (!confirm(`確定要恢復 ${emp.userName} 的權限嗎？`)) return;
            fetch(`/${ctx}/EnableEmp`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userID: emp.userID })
            })
                .then(res => { if (!res.ok) throw new Error(`HTTP ${res.status}`); return res.json(); })
                .then(resp => {
                if (resp.success) tr.remove();
                else alert(resp.message || '恢復失敗');
                })
                .catch(err => {
                console.error(err);
                alert('恢復失敗，請稍後再試');
                });
            });
            opTd.appendChild(restoreBtn);
            tr.appendChild(opTd);
            table.appendChild(tr);
        });
        resultDiv.appendChild(table);
        } else {
        resultDiv.textContent = data.message;
        }
    }
})