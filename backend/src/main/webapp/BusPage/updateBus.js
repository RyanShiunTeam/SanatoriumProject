document.addEventListener("DOMContentLoaded", () => {
    const busIdInput = document.querySelector("#busId");
    const carDealershipInput = document.querySelector("#carDealership");
    const busBrandInput = document.querySelector("#busBrand");
    const busModelInput = document.querySelector("#busModel");
    const seatCapacityInput = document.querySelector("#seatCapacity");
    const wheelchairCapacityInput = document.querySelector("#wheelchairCapacity");
    const licensePlateInput = document.querySelector("#licensePlate");
    const messageDiv = document.querySelector("#message");
    const errorDiv = document.querySelector("#error");
    const updateButton = document.querySelector("#updateButton");
    const returnButton = document.querySelector("#returnButton");
    const ctx = window.location.pathname.split('/')[1];

    // Populate form with bus details from URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    busIdInput.value = urlParams.get("busId");
    carDealershipInput.value = urlParams.get("carDealership");
    busBrandInput.value = urlParams.get("busBrand");
    busModelInput.value = urlParams.get("busModel");
    seatCapacityInput.value = urlParams.get("seatCapacity");
    wheelchairCapacityInput.value = urlParams.get("wheelchairCapacity");
    licensePlateInput.value = urlParams.get("licensePlate");

    // Handle update button click
    updateButton.addEventListener("click", () => {
        const busId = busIdInput.value;
        const seatCapacity = seatCapacityInput.value;
        const wheelchairCapacity = wheelchairCapacityInput.value;

        fetch(`/${ctx}/UpdateBus`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ busId, seatCapacity, wheelchairCapacity })
        })
            .then(res => {
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                return res.json();
            })
            .then(data => {
                messageDiv.textContent = data; // Display backend response message
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
