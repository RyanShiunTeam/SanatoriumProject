document.addEventListener("DOMContentLoaded", () => {
    const queryActivityNameInput = document.getElementById("queryActivityName");
    const queryActivityBtn = document.getElementById("queryActivityBtn");
    const queryAllActivitiesBtn = document.getElementById("queryAllActivitiesBtn");
    const activityCardContainer = document.querySelector("#activityCardContainer");
    const activityCount = document.querySelector("#activityCount");
    const messageDiv = document.querySelector("#message");
    const errorDiv = document.querySelector("#error");
    const ctx = window.location.pathname.split('/')[1];

    // Fetch all activity data
    function fetchAllActivities() {
        activityCardContainer.innerHTML = ""; // Clear previous cards
        
        fetch(`/${ctx}/QueryActivityServlet`)
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => {
                console.log("Received data:", data); // Debug log
                renderCardView(data);
            })
            .catch(err => {
                console.error(err);
                errorDiv.textContent = "發生錯誤，請稍後再試";
                errorDiv.style.display = "block";
            });
    }

    // Helper function to truncate text
    function truncateText(text, maxLength = 50) {
        if (!text) return "";
        return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
    }

    // Render card view
    function renderCardView(data) {
        activityCardContainer.innerHTML = ""; // Clear previous cards
        
        if (data && data.find === true && Array.isArray(data.activityList) && data.activityList.length > 0) {
            data.activityList.forEach(activity => {
                const col = document.createElement("div");
                col.className = "col-md-4 col-lg-3";
                
                const description = activity.description || "";
                const formattedDescription = truncateText(description, 120);
                
                col.innerHTML = `
                    <div class="card activity-card h-100">
                        <div class="activity-id">ID: ${activity.id || ""}</div>
                        <div class="card-body">
                            <h5 class="card-title">${activity.Name || ""}</h5>
                            <p class="card-text">
                                <strong>人數上限:</strong> ${activity.Limit || ""}<br>
                                <strong>日期:</strong> ${activity.Date || ""}<br>
                                <strong>時間:</strong> ${activity.Time || ""}<br>
                                <strong>地點:</strong> ${activity.location || ""}<br>
                                <strong>講師:</strong> ${activity.instructor || ""}
                            </p>
                            <div class="activity-description">
                                <p class="card-text">${formattedDescription}</p>
                            </div>
                        </div>
                        <div class="card-footer bg-transparent border-top-0">
                            <div class="d-flex justify-content-end gap-2">
                                <button class="btn btn-warning btn-sm update-btn" data-activity-id="${activity.id}">修改</button>
                                <button class="btn btn-danger btn-sm delete-btn" data-activity-id="${activity.id}">刪除</button>
                            </div>
                        </div>
                    </div>
                `;
                
                activityCardContainer.appendChild(col);
            });
            
            activityCount.textContent = `共${data.activityList.length}筆活動資料`;
            messageDiv.style.display = "none";
            errorDiv.style.display = "none";

            attachButtonEventListeners();
        } else {
            activityCount.textContent = "共0筆活動資料";
            messageDiv.textContent = data?.message || "沒有找到任何活動資料";
            messageDiv.style.display = "block";
        }
    }

    // Attach event listeners to buttons
    function attachButtonEventListeners() {
        // Update buttons
        document.querySelectorAll(".update-btn").forEach(button => {
            button.addEventListener("click", () => {
                const activityId = button.getAttribute("data-activity-id");
                const card = button.closest(".card");
                const name = card.querySelector(".card-title").textContent;
                const cardText = card.querySelectorAll(".card-text");
                
                const limitText = cardText[0].innerHTML.match(/人數上限:<\/strong> (.*?)<br>/);
                const dateText = cardText[0].innerHTML.match(/日期:<\/strong> (.*?)<br>/);
                const timeText = cardText[0].innerHTML.match(/時間:<\/strong> (.*?)<br>/);
                const locationText = cardText[0].innerHTML.match(/地點:<\/strong> (.*?)<br>/);
                const instructorText = cardText[0].innerHTML.match(/講師:<\/strong> ([^<]*)/);
                const description = cardText[1] ? cardText[1].textContent : "";

                const activityData = {
                    id: activityId,
                    Name: name,
                    Limit: limitText ? limitText[1] : "",
                    Date: dateText ? dateText[1] : "",
                    Time: timeText ? timeText[1] : "",
                    location: locationText ? locationText[1] : "",
                    instructor: instructorText ? instructorText[1] : "",
                    description
                };

                const queryParams = new URLSearchParams(activityData).toString();
                window.location.href = `updateActivity.html?${queryParams}`;
            });
        });

        // Delete buttons
        document.querySelectorAll(".delete-btn").forEach(button => {
            button.addEventListener("click", () => {
                const activityId = button.getAttribute("data-activity-id");
                if (confirm(`確定要刪除編號為 ${activityId} 的活動嗎？`)) {
                    deleteActivity(activityId);
                }
            });
        });
    }

    // Delete activity via AJAX
    function deleteActivity(activityId) {
        fetch(`/${ctx}/DeleteActivityServlet`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: activityId })
        })
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => {
                if (data.success) {
                    messageDiv.textContent = data.message || "活動刪除成功";
                    messageDiv.style.display = "block";
                    fetchAllActivities();
                } else {
                    errorDiv.textContent = data.message || "活動刪除失敗";
                    errorDiv.style.display = "block";
                }
            })
            .catch(err => {
                console.error("Delete error:", err);
                errorDiv.textContent = "發生錯誤，請稍後再試";
                errorDiv.style.display = "block";
            });
    }

    // Query a single activity by name
    queryActivityBtn.addEventListener("click", () => {
        const activityName = queryActivityNameInput.value.trim();
        if (!activityName) {
            errorDiv.textContent = "請輸入活動名稱後再查詢！";
            errorDiv.style.display = "block";
            return;
        }

        errorDiv.style.display = "none";
        messageDiv.style.display = "none";
        activityCardContainer.innerHTML = "";

        fetch(`/${ctx}/GetActivity`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ Name: activityName })
        })
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => {
                console.log("Search result:", data);
                // The response now contains an activity list instead of a single activity
                if (data.find && Array.isArray(data.activity)) {
                    // Create a data structure compatible with renderCardView
                    const formattedData = {
                        find: true,
                        activityList: data.activity
                    };
                    renderCardView(formattedData);
                } else {
                    errorDiv.textContent = data.message || "沒有找到活動資料";
                    errorDiv.style.display = "block";
                }
            })
            .catch(err => {
                console.error("Query error:", err);
                errorDiv.textContent = "發生錯誤，請稍後再試";
                errorDiv.style.display = "block";
            });
    });

    // Redirect to addActivity.html when "新增活動" button is clicked
    document.querySelector(".btn-primary").addEventListener("click", () => {
        window.location.href = "addActivity.html";
    });

    // Query all activities button
    queryAllActivitiesBtn.addEventListener("click", fetchAllActivities);

    // Fetch data on page load
    fetchAllActivities();
});
    

        fetch(`/${ctx}/QueryActivityServlet`)
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => {
                if (data.find) {
                    renderCardView(data);
                } else {
                    errorDiv.textContent = data.message || "沒有找到任何活動資料";
                    errorDiv.style.display = "block";
                }
            })
            .catch(err => {
                console.error("Query error:", err);
                errorDiv.textContent = "發生錯誤，請稍後再試";
                errorDiv.style.display = "block";
            });
    


    // Attach event listeners to buttons
    queryActivityBtn.addEventListener("click", queryActivityBtn);
    queryAllActivitiesBtn.addEventListener("click", queryAllActivities);


        
        // Redirect to addActivity.html when "新增活動" button is clicked
        document.querySelector(".btn-primary").addEventListener("click", () => {
            window.location.href = "addActivity.html";
        });
    
    
    // Delete activity via AJAX
    function deleteActivity(activityId) {
        fetch(`/${ctx}/DeleteActivityServlet`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: activityId })
        })
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => {
                if (data.success) {
                    messageDiv.textContent = data.message || "活動刪除成功";
                    messageDiv.style.display = "block";
                    // Refresh the activity list to show updated data
                    fetchAllActivities();
                } else {
                    errorDiv.textContent = data.message || "活動刪除失敗";
                    errorDiv.style.display = "block";
                }
            })
            .catch(err => {
                console.error("Delete error:", err);
                errorDiv.textContent = "發生錯誤，請稍後再試";
                errorDiv.style.display = "block";
            });
    }
    


