document.addEventListener("DOMContentLoaded", () => {
    // 移除 busIdInput
    const carDealershipInput = document.querySelector("#carDealership");
    const busBrandInput = document.querySelector("#busBrand");
    const busModelInput = document.querySelector("#busModel");
    const seatCapacityInput = document.querySelector("#seatCapacity");
    const wheelchairCapacityInput = document.querySelector("#wheelchairCapacity");
    const licensePlateInput = document.querySelector("#licensePlate");
    const messageDiv = document.querySelector("#message");
    const errorDiv = document.querySelector("#error");
    const addButton = document.querySelector("#addButton");
    const returnButton = document.querySelector("#returnButton");
    const ctx = window.location.pathname.split('/')[1];

    // Handle add button click
    addButton.addEventListener("click", () => {
        // 不再取 busId
        const carDealership = carDealershipInput.value;
        const busBrand = busBrandInput.value;
        const busModel = busModelInput.value;
        const seatCapacity = seatCapacityInput.value;
        const wheelchairCapacity = wheelchairCapacityInput.value;
        const licensePlate = licensePlateInput.value;

        // 驗證欄位
        if (!carDealership || !busBrand || !busModel || !seatCapacity || !wheelchairCapacity || !licensePlate) {
            messageDiv.textContent = "所有欄位均為必填！";
            messageDiv.classList.remove("d-none", "alert-success");
            messageDiv.classList.add("alert-danger");
            return;
        }

        fetch(`/${ctx}/InsertBus`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                carDealership,
                busBrand,
                busModel,
                seatCapacity,
                wheelchairCapacity,
                licensePlate
            })
        })
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => {
                messageDiv.textContent = data; // 顯示後端回傳訊息
                messageDiv.classList.remove("d-none", "alert-danger");
                messageDiv.classList.add("alert-success");
                errorDiv.classList.add("d-none");
            })
            .catch(err => {
                console.error(err);
                errorDiv.textContent = "發生錯誤，請稍後再試";
                errorDiv.classList.remove("d-none");
                messageDiv.classList.add("d-none");
            });
    });

    // Handle return button click
    returnButton.addEventListener("click", () => {
        window.location.href = "getAllBus.html";
    });
});
