
let transactions = JSON.parse(localStorage.getItem("transactions")) || [];
let selectedAmount = 0;

// Generate QR Code
function generateQR() {
    selectedAmount = document.getElementById("amount").value;

    const qrArea = document.getElementById("qr-area");
    qrArea.innerHTML = "";

    const qrData = "DEMO_PAYMENT|AMOUNT=" + selectedAmount;

    const qrBox = document.createElement("div");
    qrBox.style.width = "200px";
    qrBox.style.margin = "15px auto";

    qrArea.appendChild(qrBox);

    new QRCode(qrBox, {
        text: qrData,
        width: 200,
        height: 200
    });

    const message = document.createElement("p");
    message.textContent = "Demo QR generated for ₹" + selectedAmount;

    qrArea.appendChild(message);

    document.getElementById("payment-actions").style.display = "block";
}

// Simulate Payment
function simulatePayment(status) {
    if (selectedAmount === 0) {
        alert("Please generate a QR code first.");
        return;
    }

    const transaction = {
        id: "TXN" + Date.now(),
        amount: Number(selectedAmount),
        status: status,
        date: new Date().toLocaleString()
    };

    transactions.push(transaction);

    localStorage.setItem(
        "transactions",
        JSON.stringify(transactions)
    );

    alert("Demo Payment " + status + "!");

    document.getElementById("qr-area").innerHTML = "";
    document.getElementById("payment-actions").style.display = "none";

    selectedAmount = 0;

    updateDashboard();
    filterTransactions();
}

// Update Dashboard

function updateDashboard() {

    const total = transactions.length;

    const success = transactions.filter(
        t => t.status === "Success"
    ).length;

    const failed = transactions.filter(
        t => t.status === "Failed"
    ).length;

    // Calculate total successful payment amount
    const totalAmount = transactions
        .filter(t => t.status === "Success")
        .reduce((sum, t) => sum + Number(t.amount), 0);

    // Update dashboard values
    document.getElementById("total").textContent = total;

    document.getElementById("success").textContent = success;

    document.getElementById("failed").textContent = failed;

    document.getElementById("totalAmount").textContent =
        "₹" + totalAmount;
}


// Display Transactions
function displayFilteredTransactions(filteredTransactions) {
    const list = document.getElementById("transaction-list");

    list.innerHTML = "";

    filteredTransactions.slice().reverse().forEach(function(t) {

        const row = document.createElement("tr");

        const idCell = document.createElement("td");
        idCell.textContent = t.id;

        const amountCell = document.createElement("td");
        amountCell.textContent = "₹" + t.amount;

        const statusCell = document.createElement("td");
        statusCell.textContent = t.status;

        statusCell.className =
            t.status === "Success" ? "success-status" : "failed-status";

        const dateCell = document.createElement("td");
        dateCell.textContent = t.date || "Date unavailable";

        row.appendChild(idCell);
        row.appendChild(amountCell);
        row.appendChild(statusCell);
        row.appendChild(dateCell);

        list.appendChild(row);
    });
}

// Search Transactions
function searchTransactions() {
    filterTransactions();
}

// Filter Transactions by Status
function filterTransactions() {

    const searchValue = document
        .getElementById("searchInput")
        .value.toLowerCase();

    const selectedStatus =
        document.getElementById("statusFilter").value;

    const filteredTransactions = transactions.filter(function(t) {

        const matchesSearch =
            t.id.toLowerCase().includes(searchValue);

        const matchesStatus =
            selectedStatus === "All" ||
            t.status === selectedStatus;

        return matchesSearch && matchesStatus;
    });

    displayFilteredTransactions(filteredTransactions);
}

// Load Dashboard and Transactions
window.onload = function() {

    updateDashboard();

    filterTransactions();

};
