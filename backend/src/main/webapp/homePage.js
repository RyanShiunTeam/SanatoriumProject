document.addEventListener('DOMContentLoaded', function() {
    // 取得使用者名稱 (從 localStorage 獲取)
    const loginUser = JSON.parse(localStorage.getItem('loginUser') || '{}');
    if (loginUser.userName) {
        document.getElementById('user').textContent = loginUser.userName;
    }
    
    // 取得應用上下文路徑
    const ctx = window.location.pathname.split('/')[1];
    
    // 建立員工日誌初始頁面
    createLogPage();
    
    // 載入員工使用紀錄
    loadEmployeeLogs(ctx);
    
    // 新增系統使用紀錄按鈕點擊事件
    document.getElementById('employeeLogLink').addEventListener('click', function(e) {
        e.preventDefault(); // 阻止默認行為
        
        // 重新創建並載入日誌頁面
        createLogPage();
        loadEmployeeLogs(ctx);
        
        // 讓目前選中的項目樣式變化 (如果需要)
        // const menuItems = document.querySelectorAll('#main-menu li');
        // menuItems.forEach(item => item.classList.remove('active'));
        // this.closest('li').classList.add('active');
    });
});

/**
 * 建立員工日誌頁面框架
 */
function createLogPage() {
    const contentFrame = document.getElementById('contentFrame');
    const iframeDoc = contentFrame.contentDocument || contentFrame.contentWindow.document;
    
    iframeDoc.open();
    iframeDoc.write(`
    <!DOCTYPE html>
    <html lang="zh-TW">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>員工使用紀錄</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <style>
            body {
                padding: 20px;
                background-color: #f8f9fa;
            }
            
            .page-header {
                margin-bottom: 20px;
                border-bottom: 2px solid #e9ecef;
                padding-bottom: 10px;
            }
            
            .log-container {
                max-width: 1000px;
                margin: 0 auto;
            }
            
            .log-card {
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                margin-bottom: 20px;
                overflow: hidden;
            }
            
            .log-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 12px 15px;
                background-color: #f8f9fa;
                border-bottom: 1px solid #e9ecef;
            }
            
            .log-body {
                padding: 15px;
                background-color: #fff;
            }
            
            .log-time {
                font-size: 0.9rem;
                color: #6c757d;
            }
            
            .log-action {
                font-weight: 500;
                margin-bottom: 8px;
            }
            
            .log-target {
                color: #495057;
            }
            
            .log-user {
                font-weight: bold;
                display: flex;
                align-items: center;
            }
            
            .log-user i {
                margin-right: 6px;
                color: #0d6efd;
            }
            
            .empty-message {
                text-align: center;
                padding: 40px;
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            }
            
            .loading-spinner {
                text-align: center;
                padding: 40px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid log-container">
            <div class="page-header">
                <h3><i class="fa fa-history text-primary"></i> 員工系統使用紀錄</h3>
                <p class="text-muted">檢視所有員工的系統操作日誌</p>
            </div>
            
            <div id="logsContainer">
                <div class="loading-spinner">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    <p class="mt-2">正在載入日誌資料...</p>
                </div>
            </div>
        </div>
    </body>
    </html>
    `);
    iframeDoc.close();
}

/**
 * 載入員工使用紀錄
 */
function loadEmployeeLogs(ctx) {
    // 等待 iframe 加載完成
    const contentFrame = document.getElementById('contentFrame');
    
    // 確保 iframe 已經完成初始化
    if (contentFrame.contentDocument.readyState === 'complete') {
        fetchLogData();
    } else {
        contentFrame.onload = fetchLogData;
    }
    
    function fetchLogData() {
        const iframeDoc = contentFrame.contentDocument || contentFrame.contentWindow.document;
        const logsContainer = iframeDoc.getElementById('logsContainer');
        
        // 從伺服器獲取日誌數據
        fetch(`/${ctx}/GetLog`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                // 清空載入動畫
                logsContainer.innerHTML = '';
                
                if (data.find && data.empList && data.empList.length > 0) {
                    // 按時間排序，最新的在前面
                    const sortedLogs = data.empList.sort((a, b) => {
                        return new Date(b.createdAt) - new Date(a.createdAt);
                    });
                    
                    // 創建日誌卡片
                    sortedLogs.forEach(log => {
                        const logCard = iframeDoc.createElement('div');
                        logCard.className = 'log-card';
                        
                        // 方式一：手動調整時區偏移（如果後端傳來的是 UTC 時間）
                        const logDate = new Date(log.createdAt);
                        // 調整為台灣時間（UTC+8）
                        logDate.setHours(logDate.getHours() + 8);
                        const formattedDate = logDate.toLocaleString('zh-TW', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit',
                            hour12: false
                        });
                        
                        // 根據操作類型選擇不同的圖標和顏色
                        let actionIcon = 'fa-info-circle';
                        let actionClass = 'text-info';
                        
                        if (log.action.includes('新增')) {
                            actionIcon = 'fa-plus-circle';
                            actionClass = 'text-success';
                        } else if (log.action.includes('修改')) {
                            actionIcon = 'fa-edit';
                            actionClass = 'text-warning';
                        } else if (log.action.includes('刪除') || log.action.includes('停權')) {
                            actionIcon = 'fa-trash';
                            actionClass = 'text-danger';
                        } else if (log.action.includes('查詢')) {
                            actionIcon = 'fa-search';
                            actionClass = 'text-primary';
                        }
                        
                        logCard.innerHTML = `
                            <div class="log-header">
                                <div class="log-user">
                                    <i class="fa fa-user-circle"></i>
                                    員工 ID: ${log.userId}
                                </div>
                                <div class="log-time">
                                    <i class="fa fa-clock-o"></i>
                                    ${formattedDate}
                                </div>
                            </div>
                            <div class="log-body">
                                <div class="log-action">
                                    <i class="fa ${actionIcon} ${actionClass}"></i>
                                    <span class="${actionClass}">${log.action}</span>
                                </div>
                                <div class="log-target">
                                    <i class="fa fa-crosshairs text-secondary"></i>
                                    操作目標 ID: ${log.targetId || '無指定目標 ID'}
                                </div>
                            </div>
                        `;
                        
                        logsContainer.appendChild(logCard);
                    });
                } else {
                    // 沒有日誌記錄的情況
                    const emptyMessage = iframeDoc.createElement('div');
                    emptyMessage.className = 'empty-message';
                    emptyMessage.innerHTML = `
                        <i class="fa fa-info-circle text-info" style="font-size: 48px;"></i>
                        <h4 class="mt-3">尚無員工使用紀錄</h4>
                        <p class="text-muted">系統目前沒有記錄任何員工操作日誌</p>
                    `;
                    logsContainer.appendChild(emptyMessage);
                }
            })
            .catch(error => {
                console.error('載入員工紀錄時發生錯誤:', error);
                logsContainer.innerHTML = `
                    <div class="alert alert-danger" role="alert">
                        <i class="fa fa-exclamation-triangle"></i>
                        載入紀錄時發生錯誤，請稍後再試。
                        <br>
                        <small class="text-muted">${error.message}</small>
                    </div>
                `;
            });
    }
}