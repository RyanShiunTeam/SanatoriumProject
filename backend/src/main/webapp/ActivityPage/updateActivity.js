document.addEventListener("DOMContentLoaded", () => {
    const updateForm = document.getElementById("updateActivityForm");
    const messageDiv = document.getElementById("message");
    const errorDiv = document.getElementById("error");
    const backButton = document.getElementById("backButton");
    const ctx = window.location.pathname.split('/')[1];
    
    // Form fields
    const idField = document.getElementById("activityId");
    const nameField = document.getElementById("activityName");
    const limitField = document.getElementById("activityLimit");
    const dateField = document.getElementById("activityDate");
    const timeField = document.getElementById("activityTime");
    const locationField = document.getElementById("activityLocation");
    const instructorField = document.getElementById("activityInstructor");
    const descriptionField = document.getElementById("activityDescription");
    
    // Parse URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    
    // Helper function to format date for input fields
    function formatDateForInput(dateString) {
        if (!dateString) return "";
        
        try {
            // Try to handle different date formats
            let date;
            if (dateString.includes("/")) {
                // Handle format like MM/DD/YYYY or DD/MM/YYYY
                const parts = dateString.split("/");
                if (parts.length === 3) {
                    // Try different date formats
                    if (parts[2].length === 4) { // Year is at index 2
                        date = new Date(`${parts[2]}-${parts[0].padStart(2, '0')}-${parts[1].padStart(2, '0')}`);
                    } else {
                        date = new Date(dateString); // Let browser parse
                    }
                } else {
                    date = new Date(dateString);
                }
            } else {
                date = new Date(dateString);
            }
            
            // Format as YYYY-MM-DD
            return date.toISOString().split('T')[0];
        } catch (e) {
            console.error("Date parsing error:", e);
            return dateString; // Return original if parsing fails
        }
    }
    
    // Helper function to format time for input field
    function formatTimeForInput(timeString) {
        if (!timeString) return "";
        
        try {
            // Clean the timeString of any potential whitespace
            const cleanedTime = timeString.trim();
            
            // Handle common time formats
            if (cleanedTime.includes(':')) {
                // If it's already in HH:MM format, ensure it has at least 5 characters (HH:MM)
                const parts = cleanedTime.split(':');
                if (parts.length >= 2) {
                    const hours = parts[0].trim().padStart(2, '0');
                    const minutes = parts[1].trim().padStart(2, '0');
                    return `${hours}:${minutes}`;
                }
            }
            
            // Try to parse as time
            const timeParts = cleanedTime.match(/(\d+):(\d+)/);
            if (timeParts) {
                const hours = timeParts[1].padStart(2, '0');
                const minutes = timeParts[2].padStart(2, '0');
                return `${hours}:${minutes}`;
            }
            
            return cleanedTime;
        } catch (e) {
            console.error("Time parsing error:", e);
            return timeString;
        }
    }
    
    // Populate form with URL parameters
    function populateForm() {
        if (urlParams.has("id")) {
            // Debug logging
            console.log("URL parameters:", Object.fromEntries(urlParams.entries()));
            
            idField.value = urlParams.get("id");
            nameField.value = urlParams.get("Name") || "";
            limitField.value = urlParams.get("Limit") || "";
            // Format date for input field
            dateField.value = formatDateForInput(urlParams.get("Date") || "");
            
            // Format time for input field with improved handling
            const timeValue = urlParams.get("Time") || "";
            console.log("Raw time value:", timeValue);
            timeField.value = formatTimeForInput(timeValue);
            console.log("Formatted time value:", timeField.value);
            
            locationField.value = urlParams.get("location") || "";
            
            // Handle instructor field with more robust parsing
            const instructorValue = urlParams.get("instructor") || "";
            console.log("Raw instructor value:", instructorValue);
            instructorField.value = instructorValue.trim();
            
            descriptionField.value = urlParams.get("description") || "";
            
            // Log for debugging
            console.log("Populated form with values:", {
                id: idField.value,
                name: nameField.value,
                limit: limitField.value,
                date: dateField.value,
                time: timeField.value,
                location: locationField.value,
                instructor: instructorField.value,
                description: descriptionField.value
            });
        } else {
            // No ID provided, redirect back to list page
            window.location.href = "getAllActivity.html";
        }
    }
    
    // Handle form submission
    updateForm.addEventListener("submit", (e) => {
        e.preventDefault();
        
        // Collect form data
        const activityData = {
            id: idField.value,
            Name: nameField.value,
            Limit: limitField.value,
            Date: dateField.value,
            Time: timeField.value,
            location: locationField.value,
            instructor: instructorField.value,
            description: descriptionField.value
        };
        
        // Send update request
        fetch(`/${ctx}/UpdateActivityServlet`, {
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
            messageDiv.textContent = data || "活動修改成功";
            messageDiv.style.display = "block";
            
            // Scroll to message
            messageDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
            
            // Disable form temporarily to prevent double submission
            const submitBtn = updateForm.querySelector("button[type='submit']");
            submitBtn.disabled = true;
            submitBtn.textContent = "修改成功";
            
            // Re-enable after delay
            setTimeout(() => {
                submitBtn.disabled = false;
                submitBtn.textContent = "確認修改";
            }, 3000);
        })
        .catch(err => {
            console.error("Update error:", err);
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
    
    // Load form data on page load
    populateForm();
});
