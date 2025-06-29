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
            // 創建卡片容器
            const cardDiv = document.createElement('div');
            cardDiv.className = 'card border shadow-sm rounded';
            
            // 創建卡片標題
            const headerDiv = document.createElement('div');
            headerDiv.className = 'card-header bg-light';
            headerDiv.innerHTML = '<h4 class="mb-0">員工資料</h4>';
            cardDiv.appendChild(headerDiv);
            
            // 創建卡片內容
            const bodyDiv = document.createElement('div');
            bodyDiv.className = 'card-body p-0';
            
            // 創建表格容器
            const tableResponsiveDiv = document.createElement('div');
            tableResponsiveDiv.className = 'table-responsive';
            
            // 創建表格並添加類別
            const table = document.createElement('table');
            table.className = 'table table-striped table-hover mb-0';
            
            // 創建表頭
            const thead = document.createElement('thead');
            thead.className = 'bg-primary text-white';
            const headerRow = document.createElement('tr');
            
            // 動態決定欄位
            const cols = ['ID', '姓名', 'Email', '權限', '到職日'];
            if (userRole === 'Admin') cols.push('資料異動');
            
            cols.forEach(colText => {
                const th = document.createElement('th');
                th.textContent = colText;
                th.className = 'py-3';
                headerRow.appendChild(th);
            });
            
            thead.appendChild(headerRow);
            table.appendChild(thead);
            
            // 創建表格主體
            const tbody = document.createElement('tbody');
            
            // 塞入資料列
            data.empList.forEach(emp => {
                const tr = document.createElement('tr');
                
                // ID 欄位
                const tdId = document.createElement('td');
                tdId.textContent = emp.userID || '';
                tdId.className = 'align-middle';
                tr.appendChild(tdId);
                
                // 姓名欄位
                const tdName = document.createElement('td');
                tdName.className = 'align-middle';
                const nameSpan = document.createElement('span');
                nameSpan.className = 'fw-bold'; // 加粗姓名
                nameSpan.textContent = emp.userName || '';
                tdName.appendChild(nameSpan);
                tr.appendChild(tdName);
                
                // Email 欄位 - 使用 text-info 樣式
                const tdEmail = document.createElement('td');
                tdEmail.className = 'align-middle';
                const emailSpan = document.createElement('span');
                emailSpan.className = 'text-info';
                emailSpan.textContent = emp.email || '';
                tdEmail.appendChild(emailSpan);
                tr.appendChild(tdEmail);
                
                // 權限欄位
                const tdRole = document.createElement('td');
                tdRole.className = 'align-middle';
                const roleSpan = document.createElement('span');
                // 修正條件判斷，改為 "Admin"，而非 "ADMIN"
                roleSpan.className = emp.role === 'Admin' ? 'badge bg-warning text-dark' : 'badge bg-success';
                roleSpan.textContent = emp.role || '';
                tdRole.appendChild(roleSpan);
                tr.appendChild(tdRole);
                
                // 到職日欄位
                const tdCreatedAt = document.createElement('td');
                tdCreatedAt.className = 'align-middle';
                tdCreatedAt.textContent = emp.createdAt || '';
                tr.appendChild(tdCreatedAt);
                
                // Admin 專屬操作欄
                if (userRole === 'Admin') {
                    const tdAction = document.createElement('td');
                    tdAction.className = 'align-middle';
                    
                    // 修改按鈕 - 使用 me-3 增加右邊間距
                    const editBtn = document.createElement('button');
                    editBtn.className = 'btn btn-sm btn-info me-3'; // 增加更明顯的間隔
                    editBtn.innerHTML = '<i class="fa fa-edit"></i> 修改';
                    editBtn.title = '修改';
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
                    
                    // 停權按鈕
                    const delBtn = document.createElement('button');
                    delBtn.className = 'btn btn-sm btn-danger';
                    delBtn.innerHTML = '<i class="fa fa-trash-o"></i> 停權';
                    delBtn.title = '停權';
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
                            console.error(err);
                            alert("停權失敗，稍後再試");
                         });
                    });
                    
                    tdAction.appendChild(editBtn);
                    tdAction.appendChild(document.createTextNode(' ')); // 添加額外空白
                    tdAction.appendChild(delBtn);
                    tr.appendChild(tdAction);
                }
                
                tbody.appendChild(tr);
            });
            
            table.appendChild(tbody);
            tableResponsiveDiv.appendChild(table);
            bodyDiv.appendChild(tableResponsiveDiv);
            cardDiv.appendChild(bodyDiv);
            resultDiv.appendChild(cardDiv);
            
            // 如果沒有資料，顯示提示訊息
            if (data.empList.length === 0) {
                const messageDiv = document.createElement('div');
                messageDiv.className = 'alert alert-info mt-3';
                messageDiv.textContent = "沒有找到匹配的員工資料";
                resultDiv.appendChild(messageDiv);
            }
        } else {
            const messageDiv = document.createElement('div');
            messageDiv.className = 'alert alert-warning';
            messageDiv.textContent = data.message || "查詢失敗";
            resultDiv.appendChild(messageDiv);
        }
    }

    // 停權員工列表
    function renderBanTable(data) {
        resultDiv.innerHTML = '';
        
        if (data.find && Array.isArray(data.banList)) {
            // 創建卡片容器
            const cardDiv = document.createElement('div');
            cardDiv.className = 'card border shadow-sm rounded';
            
            // 創建卡片標題
            const headerDiv = document.createElement('div');
            headerDiv.className = 'card-header bg-light';
            headerDiv.innerHTML = '<h4 class="mb-0 text-danger">停權員工名單</h4>';
            cardDiv.appendChild(headerDiv);
            
            // 創建卡片內容
            const bodyDiv = document.createElement('div');
            bodyDiv.className = 'card-body p-0';
            
            // 創建表格容器
            const tableResponsiveDiv = document.createElement('div');
            tableResponsiveDiv.className = 'table-responsive';
            
            // 創建表格並添加類別
            const table = document.createElement('table');
            table.className = 'table table-striped table-hover mb-0';
            
            // 創建表頭
            const thead = document.createElement('thead');
            thead.className = 'bg-danger text-white';
            const headerRow = document.createElement('tr');
            
            // 欄位定義
            const cols = ['ID', '姓名', 'Email', '權限', '停權日期', '操作'];
            
            cols.forEach(colText => {
                const th = document.createElement('th');
                th.textContent = colText;
                th.className = 'py-3';
                headerRow.appendChild(th);
            });
            
            thead.appendChild(headerRow);
            table.appendChild(thead);
            
            // 創建表格主體
            const tbody = document.createElement('tbody');
            
            // 填入每筆停權員工資料列
            data.banList.forEach(emp => {
                const tr = document.createElement('tr');
                
                // ID 欄位
                const tdId = document.createElement('td');
                tdId.className = 'align-middle';
                tdId.textContent = emp.userID || '';
                tr.appendChild(tdId);
                
                // 姓名欄位
                const tdName = document.createElement('td');
                tdName.className = 'align-middle';
                const nameSpan = document.createElement('span');
                nameSpan.className = 'fw-bold text-muted';
                nameSpan.textContent = emp.userName || '';
                tdName.appendChild(nameSpan);
                tr.appendChild(tdName);
                
                // Email 欄位
                const tdEmail = document.createElement('td');
                tdEmail.className = 'align-middle';
                const emailSpan = document.createElement('span');
                emailSpan.className = 'text-info';
                emailSpan.textContent = emp.email || '';
                tdEmail.appendChild(emailSpan);
                tr.appendChild(tdEmail);
                
                // 權限欄位
                const tdRole = document.createElement('td');
                tdRole.className = 'align-middle';
                const roleSpan = document.createElement('span');
                // 已經正確檢查 'Admin' 了，不需要修改
                roleSpan.className = emp.role === 'Admin' ? 'badge bg-warning text-dark' : 'badge bg-success';
                roleSpan.textContent = emp.role || '';
                tdRole.appendChild(roleSpan);
                tr.appendChild(tdRole);
                
                // 停權日期欄位
                const tdUpdatedAt = document.createElement('td');
                tdUpdatedAt.className = 'align-middle';
                tdUpdatedAt.textContent = emp.updatedAt || '';
                tr.appendChild(tdUpdatedAt);
                
                // 操作按鈕
                const tdAction = document.createElement('td');
                tdAction.className = 'align-middle';
                
                const restoreBtn = document.createElement('button');
                restoreBtn.className = 'btn btn-sm btn-success';
                restoreBtn.innerHTML = '<i class="fa fa-refresh"></i> 恢復權限';
                
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
                
                tdAction.appendChild(restoreBtn);
                tr.appendChild(tdAction);
                
                tbody.appendChild(tr);
            });
            
            table.appendChild(tbody);
            tableResponsiveDiv.appendChild(table);
            bodyDiv.appendChild(tableResponsiveDiv);
            cardDiv.appendChild(bodyDiv);
            resultDiv.appendChild(cardDiv);
            
            // 添加返回按鈕
            const actionsDiv = document.createElement('div');
            actionsDiv.className = 'mt-3';
            
            const backBtn = document.createElement('button');
            backBtn.className = 'btn btn-primary';
            backBtn.innerHTML = '<i class="fa fa-arrow-left"></i> 返回正常員工列表';
            backBtn.addEventListener('click', fetchAllEmployees);
            
            actionsDiv.appendChild(backBtn);
            resultDiv.appendChild(actionsDiv);
            
            // 如果沒有資料，顯示提示訊息
            if (data.banList.length === 0) {
                const messageDiv = document.createElement('div');
                messageDiv.className = 'alert alert-info mt-3';
                messageDiv.textContent = "沒有停權員工資料";
                resultDiv.appendChild(messageDiv);
            }
        } else {
            const messageDiv = document.createElement('div');
            messageDiv.className = 'alert alert-warning';
            messageDiv.textContent = data.message || "查詢失敗";
            resultDiv.appendChild(messageDiv);
        }
    }
})