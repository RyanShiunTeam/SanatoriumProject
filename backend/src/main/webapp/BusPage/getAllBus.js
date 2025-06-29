document.addEventListener("DOMContentLoaded", () => {
    const busTableBody = document.querySelector("#busTableBody");
    const busCount = document.querySelector("#busCount");
    const messageDiv = document.querySelector("#message");
    const errorDiv = document.querySelector("#error");
    const filterButton = document.querySelector("#filterButton");
    const fetchAllButton = document.querySelector("#fetchAllButton");
    const minSeatsInput = document.querySelector("#minSeats");
    const maxSeatsInput = document.querySelector("#maxSeats");
    const minWheelsInput = document.querySelector("#minWheels");
    const maxWheelsInput = document.querySelector("#maxWheels");
    const ctx = window.location.pathname.split('/')[1];

    // Fetch all bus data
    function fetchAllBuses() {
        busTableBody.innerHTML = ""; // Clear previous rows
        fetch(`/${ctx}/GetAllBus`)
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => renderTable(data))
            .catch(err => {
                console.error(err);
                errorDiv.textContent = "發生錯誤，請稍後再試";
                errorDiv.style.display = "block";
            });
    }

    // Render table rows dynamically
    function renderTable(data) {
        if (data.find && Array.isArray(data.busList)) {
            busTableBody.innerHTML = ""; // Clear previous rows
            data.busList.forEach(bus => {
                const tr = document.createElement("tr");

                tr.innerHTML = `
                    <td>${bus.busId || ""}</td>
                    <td>${bus.carDealership || ""}</td>
                    <td>${bus.busBrand || ""}</td>
                    <td>${bus.busModel || ""}</td>
                    <td>${bus.seatCapacity || ""}</td>
                    <td>${bus.wheelchairCapacity || ""}</td>
                    <td>${bus.licensePlate || ""}</td>
                    <td>
                        <button type="button" class="btn btn-warning btn-sm update-btn" data-bus-id="${bus.busId}">修改</button>
                        <button type="button" class="btn btn-danger btn-sm delete-btn" data-bus-id="${bus.busId}">刪除</button>
                    </td>
                `;
                busTableBody.appendChild(tr);
            });

            busCount.textContent = `共${data.busList.length}筆巴士資料`;
            messageDiv.style.display = "none";
            errorDiv.style.display = "none";

            // Attach update button event listeners
            document.querySelectorAll(".update-btn").forEach(button => {
                button.addEventListener("click", () => {
                    const busId = button.getAttribute("data-bus-id");
                    const row = button.closest("tr");
                    const carDealership = row.children[1].textContent;
                    const busBrand = row.children[2].textContent;
                    const busModel = row.children[3].textContent;
                    const seatCapacity = row.children[4].textContent;
                    const wheelchairCapacity = row.children[5].textContent;
                    const licensePlate = row.children[6].textContent;

                    const queryParams = new URLSearchParams({
                        busId,
                        carDealership,
                        busBrand,
                        busModel,
                        seatCapacity,
                        wheelchairCapacity,
                        licensePlate
                    }).toString();

                    window.location.href = `updateBus.html?${queryParams}`;
                });
            });

            // Attach delete button event listeners
            document.querySelectorAll(".delete-btn").forEach(button => {
                button.addEventListener("click", () => {
                    const busId = button.getAttribute("data-bus-id");
                    if (confirm(`確定要刪除編號 ${busId}嗎？`)) {
                        deleteBus(busId);
                    }
                });
            });
        } else {
            busTableBody.innerHTML = ""; // Clear table
            busCount.textContent = "共0筆巴士資料";
            messageDiv.textContent = data.message || "沒有找到任何巴士資料";
            messageDiv.style.display = "block";
        }
    }

    // Delete bus via AJAX
    function deleteBus(busId) {
        fetch(`/${ctx}/DeleteBus`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ busId })
        })
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => {
                if (data.success) {
                    messageDiv.textContent = data.message;
                    messageDiv.style.display = "block";
                    fetchAllBuses(); // Refresh table
                } else {
                    errorDiv.textContent = data.message;
                    errorDiv.style.display = "block";
                }
            })
            .catch(err => {
                console.error(err);
                errorDiv.textContent = "發生錯誤，請稍後再試";
                errorDiv.style.display = "block";
            });
    }

    // Handle filter button click
    filterButton.addEventListener("click", () => {
        const minSeats = minSeatsInput.value;
        const maxSeats = maxSeatsInput.value;
        const minWheels = minWheelsInput.value;
        const maxWheels = maxWheelsInput.value;

        // Validate inputs
        if (!minSeats || !maxSeats || !minWheels || !maxWheels) {
            alert("請填寫所有篩選條件！");
            return;
        }

        // Send filter request
        fetch(`/${ctx}/FilterBus`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ minSeats, maxSeats, minWheels, maxWheels })
        })
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => renderTable(data))
            .catch(err => {
                console.error(err);
                errorDiv.textContent = "發生錯誤，請稍後再試";
                errorDiv.style.display = "block";
            });
    });

    // Handle fetch all button click
    fetchAllButton.addEventListener("click", () => {
        fetchAllBuses();
    });

    // Fetch data on page load
    fetchAllBuses();
});
