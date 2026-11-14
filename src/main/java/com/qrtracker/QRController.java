
package com.qrtracker;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import com.qrtracker.model.Payment;
import com.qrtracker.service.PaymentService;

import org.springframework.http.ResponseEntity;

@RestController
public class QRController {

    private final PaymentService paymentService;

    public QRController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Test backend connection
    @GetMapping("/api/test")
    public String testAPI() {
        return "QR Tracker Backend is Working Successfully!";
    }

    // Verify payment amount
    @GetMapping("/api/verify-amount")
    public String verifyAmount(@RequestParam double amount) {

        if (amount <= 0) {
            return "Invalid amount! Amount must be greater than zero.";
        }

        return "Amount verified successfully: Rs. "
                + String.format("%.2f", amount);
    }

    // Get payment status
    @GetMapping("/api/payment-status")
    public String getPaymentStatus() {
        return "Payment Status: PENDING";
    }

    // Get payment details
    @GetMapping("/api/payment-details")
    public Payment getPaymentDetails() {
        return new Payment(250.00, "PENDING");
    }

    // Verify received payment amount
    @PostMapping("/api/verify-payment")
    public Payment verifyPayment(
            @RequestParam BigDecimal receivedAmount,
            @RequestParam String transactionId) {

        Payment payment = new Payment(250.00, "PENDING");

        paymentService.verifyPayment(
                payment,
                receivedAmount,
                transactionId
        );

        return payment;
    }

    // Save simulated payment with account holder name
    @PostMapping("/api/demo-payment")
    public ResponseEntity<String> saveDemoPayment(
            @RequestParam BigDecimal amount,
            @RequestParam String transactionId,
            @RequestParam String status,
            @RequestParam String accountHolderName) {

        boolean saved = paymentService.saveDemoPayment(
                amount,
                transactionId,
                status,
                accountHolderName
        );

        if (!saved) {
            return ResponseEntity.badRequest()
                    .body("Payment could not be saved. Check the details or duplicate transaction ID.");
        }

        return ResponseEntity.ok(
                "Demo payment saved successfully!"
        );
    }

    // Get complete payment history
    @GetMapping("/api/payment-history")
    public List<Payment> getPaymentHistory() {
        return paymentService.getPaymentHistory();
    }

    // Find payment by transaction ID
    @GetMapping("/api/payment-by-transaction")
    public ResponseEntity<Payment> getPaymentByTransactionId(
            @RequestParam String transactionId) {

        Payment payment =
                paymentService.getPaymentByTransactionId(transactionId);

        if (payment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(payment);
    }

    // Filter payments by status
    @GetMapping("/api/payments-by-status")
    public List<Payment> getPaymentsByStatus(
            @RequestParam String status) {

        return paymentService.getPaymentsByStatus(status);
    }

    // Get total payments
    @GetMapping("/api/payment-total")
    public long getTotalPayments() {
        return paymentService.getTotalPayments();
    }

    // Get successful payments
    @GetMapping("/api/payment-success-total")
    public long getSuccessfulPayments() {
        return paymentService.getSuccessfulPayments();
    }

    // Get failed payments
    @GetMapping("/api/payment-failed-total")
    public long getFailedPayments() {
        return paymentService.getFailedPayments();
    }

    // Get pending payments
    @GetMapping("/api/payment-pending-total")
    public long getPendingPayments() {
        return paymentService.getPendingPayments();
    }
}
