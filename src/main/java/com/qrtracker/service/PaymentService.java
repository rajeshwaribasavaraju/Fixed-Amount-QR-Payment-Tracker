package com.qrtracker.service;

import com.qrtracker.model.Payment;
import com.qrtracker.database.PaymentRepository;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

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

        if (amountMatches) {
            payment.setStatus("SUCCESS");
        } else {
            payment.setStatus("FAILED");
        }

        boolean saved = paymentRepository.savePayment(payment);

        if (!saved) {
            payment.setStatus("FAILED");
            return false;
        }

        return amountMatches;
    }
    public List<Payment> getPaymentHistory() {
    return paymentRepository.getAllPayments();
}
public Payment getPaymentByTransactionId(String transactionId) {
    return paymentRepository.getPaymentByTransactionId(transactionId);
}
public List<Payment> getPaymentsByStatus(String status) {
    return paymentRepository.getPaymentsByStatus(status);
}
public long getTotalPayments() {
    return paymentRepository.getAllPayments().size();
}
public long getSuccessfulPayments() {
    return paymentRepository.getPaymentsByStatus("SUCCESS").size();
}
public long getFailedPayments() {
    return paymentRepository.getPaymentsByStatus("FAILED").size();
}
public long getPendingPayments() {
    return paymentRepository.getPaymentsByStatus("PENDING").size();
}
}