
package com.qrtracker.model;

import java.math.BigDecimal;

public class Payment {

    private BigDecimal fixedAmount;
    private BigDecimal receivedAmount;
    private String transactionId;
    private String status;
    private String createdAt;
    private String accountHolderName;

    public Payment() {
    }

    public Payment(double amount, String paymentStatus) {
        this.fixedAmount = BigDecimal.valueOf(amount);
        this.status = paymentStatus;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public double getAmount() {
        return fixedAmount != null ? fixedAmount.doubleValue() : 0.0;
    }

    public String getPaymentStatus() {
        return status;
    }
}
