document.addEventListener("DOMContentLoaded", () => {
    const addForm = document.getElementById("addActivityForm");
    const messageDiv = document.getElementById("message");
    const errorDiv = document.getElementById("error");
    const backButton = document.getElementById("backButton");
    const ctx = window.location.pathname.split('/')[1];

    // Handle form submission
    addForm.addEventListener("submit", (e) => {
        e.preventDefault();

        // Collect form data
        const activityData = {
            Name: document.getElementById("activityName").value.trim(),
            Limit: document.getElementById("activityLimit").value.trim(),
            Date: document.getElementById("activityDate").value.trim(),
            Time: document.getElementById("activityTime").value.trim(),
            location: document.getElementById("activityLocation").value.trim(),
            instructor: document.getElementById("activityInstructor").value.trim(),
            description: document.getElementById("activityDescription").value.trim()
        };

        // Send add request
        fetch(`/${ctx}/AddActivityServlet`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(activityData)
        })
        .then(res => {
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            return res.json();
        })
        .then(data => {
            // Handle response
            errorDiv.style.display = "none";
            messageDiv.textContent = data || "活動新增成功";
            messageDiv.style.display = "block";

            // Scroll to message
            messageDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });

            // Reset form
            addForm.reset();
        })
        .catch(err => {
            console.error("Add error:", err);
            messageDiv.style.display = "none";
            errorDiv.textContent = "發生錯誤，請稍後再試";
            errorDiv.style.display = "block";

            // Scroll to error
            errorDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
        });
    });

    // Back button handler
    backButton.addEventListener("click", () => {
        window.location.href = "getAllActivity.html";
    });
});
