
package com.qrtracker.service;

import com.qrtracker.model.Payment;
import com.qrtracker.database.PaymentRepository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Verify received payment amount
    public boolean verifyPayment(
            Payment payment,
            BigDecimal receivedAmount,
            String transactionId) {

        if (payment == null) {
            return false;
        }

        if (receivedAmount == null || transactionId == null
                || transactionId.trim().isEmpty()) {
            payment.setStatus("FAILED");
            return false;
        }

        payment.setReceivedAmount(receivedAmount);
        payment.setTransactionId(transactionId.trim());

        boolean amountMatches =
                receivedAmount.compareTo(BigDecimal.ZERO) > 0
                && payment.getFixedAmount() != null
                && receivedAmount.compareTo(payment.getFixedAmount()) == 0;

        payment.setStatus(amountMatches ? "SUCCESS" : "FAILED");

        boolean saved = paymentRepository.savePayment(payment);

        if (!saved) {
            payment.setStatus("FAILED");
            return false;
        }

        return amountMatches;
    }

    // Save simulated payment with account holder name
    public boolean saveDemoPayment(
            BigDecimal amount,
            String transactionId,
            String status,
            String accountHolderName) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0
                || transactionId == null
                || transactionId.trim().isEmpty()
                || status == null
                || accountHolderName == null
                || accountHolderName.trim().isEmpty()) {
            return false;
        }

        String normalizedStatus = status.trim().toUpperCase();

        if (!normalizedStatus.equals("SUCCESS")
                && !normalizedStatus.equals("FAILED")) {
            return false;
        }

        Payment payment = new Payment();

        payment.setFixedAmount(amount);
        payment.setReceivedAmount(amount);
        payment.setTransactionId(transactionId.trim());
        payment.setStatus(normalizedStatus);
        payment.setAccountHolderName(accountHolderName.trim());

        return paymentRepository.savePayment(payment);
    }

    // Get all payment records
    public List<Payment> getPaymentHistory() {
        return paymentRepository.getAllPayments();
    }

    // Find payment using transaction ID
    public Payment getPaymentByTransactionId(String transactionId) {
        return paymentRepository.getPaymentByTransactionId(transactionId);
    }

    // Filter payments by status
    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.getPaymentsByStatus(status);
    }

    // Dashboard total payments
    public long getTotalPayments() {
        return paymentRepository.getAllPayments().size();
    }

    // Dashboard successful payments
    public long getSuccessfulPayments() {
        return paymentRepository.getPaymentsByStatus("SUCCESS").size();
    }

    // Dashboard failed payments
    public long getFailedPayments() {
        return paymentRepository.getPaymentsByStatus("FAILED").size();
    }

    // Dashboard pending payments
    public long getPendingPayments() {
        return paymentRepository.getPaymentsByStatus("PENDING").size();
    }
}
