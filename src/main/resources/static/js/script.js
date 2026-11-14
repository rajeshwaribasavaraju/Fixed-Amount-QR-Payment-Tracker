
let transactions = [];
let selectedAmount = 0;

const API_BASE = "/api";

// Generate QR Code
function generateQR() {

    const accountHolderName =
        document.getElementById("accountHolderName").value.trim();

    if (accountHolderName === "") {
        alert("Please enter the account holder name.");
        return;
    }

    selectedAmount = Number(
        document.getElementById("amount").value
    );

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
    message.textContent =
        "Demo QR generated for ₹" + selectedAmount;

    qrArea.appendChild(message);

    document.getElementById("payment-actions").style.display = "block";
}

// Save simulated payment in backend
async function simulatePayment(status) {

    if (selectedAmount === 0) {
        alert("Please generate a QR code first.");
        return;
    }

    const accountHolderName =
        document.getElementById("accountHolderName").value.trim();

    if (accountHolderName === "") {
        alert("Please enter the account holder name.");
        return;
    }

    const transactionId = "TXN" + Date.now();

    const paymentData = new URLSearchParams();

    paymentData.append("amount", selectedAmount);
    paymentData.append("transactionId", transactionId);
    paymentData.append("status", status.toUpperCase());
    paymentData.append("accountHolderName", accountHolderName);

    try {

        const response = await fetch(
            API_BASE + "/demo-payment",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: paymentData
            }
        );

        const message = await response.text();

        if (!response.ok) {
            alert("Payment could not be saved: " + message);
            return;
        }

        alert("Demo Payment " + status + " saved successfully!");

        document.getElementById("qr-area").innerHTML = "";
        document.getElementById("payment-actions").style.display = "none";

        selectedAmount = 0;

        await loadDashboard();

    } catch (error) {

        console.error("Payment error:", error);

        alert(
            "Unable to connect to the backend. Please check whether the server is running."
        );
    }
}

// Load payment history from backend
async function loadPaymentHistory() {

    try {

        const response = await fetch(API_BASE + "/payment-history");

        if (!response.ok) {
            throw new Error("Unable to load payment history.");
        }

        transactions = await response.json();

        return true;

    } catch (error) {

        console.error("History error:", error);
        alert("Unable to load payment history.");

        return false;
    }
}

// Update Dashboard
async function loadDashboard() {

    const loaded = await loadPaymentHistory();

    if (!loaded) {
        return;
    }

    const total = transactions.length;

    const success = transactions.filter(
        t => t.status.toUpperCase() === "SUCCESS"
    ).length;

    const failed = transactions.filter(
        t => t.status.toUpperCase() === "FAILED"
    ).length;

    const totalAmount = transactions
        .filter(t => t.status.toUpperCase() === "SUCCESS")
        .reduce(
            (sum, t) => sum + Number(t.fixedAmount),
            0
        );

    document.getElementById("total").textContent = total;
    document.getElementById("success").textContent = success;
    document.getElementById("failed").textContent = failed;

    document.getElementById("totalAmount").textContent =
        "₹" + totalAmount;

    filterTransactions();
}

// Display Transactions
function displayFilteredTransactions(filteredTransactions) {

    const list = document.getElementById("transaction-list");
    list.innerHTML = "";

    filteredTransactions.slice().reverse().forEach(function(t) {

        const row = document.createElement("tr");

        const idCell = document.createElement("td");
        idCell.textContent = t.transactionId;

        const nameCell = document.createElement("td");
        nameCell.textContent =
            t.accountHolderName || "Not available";

        const amountCell = document.createElement("td");
        amountCell.textContent = "₹" + t.fixedAmount;

        const statusCell = document.createElement("td");

        const displayStatus =
            t.status.toUpperCase() === "SUCCESS" ? "Success" :
            t.status.toUpperCase() === "FAILED" ? "Failed" :
            "Pending";

        statusCell.textContent = displayStatus;

        statusCell.className =
            displayStatus === "Success" ? "success-status" :
            displayStatus === "Failed" ? "failed-status" : "";

        const dateCell = document.createElement("td");
        dateCell.textContent = t.createdAt || "Date unavailable";

        row.appendChild(idCell);
        row.appendChild(nameCell);
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

    const searchInput = document.getElementById("searchInput");
    const statusFilter = document.getElementById("statusFilter");

    if (!searchInput || !statusFilter) {
        return;
    }

    const searchValue = searchInput.value.toLowerCase();
    const selectedStatus = statusFilter.value.toUpperCase();

    const filteredTransactions = transactions.filter(function(t) {

        const matchesSearch =
            t.transactionId.toLowerCase().includes(searchValue);

        const matchesStatus =
            selectedStatus === "ALL" ||
            t.status.toUpperCase() === selectedStatus;

        return matchesSearch && matchesStatus;
    });

    displayFilteredTransactions(filteredTransactions);
}

// Load dashboard when page opens
window.onload = function() {
    loadDashboard();
};
