package com.qrtracker;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qrtracker.model.Payment;
import com.qrtracker.service.PaymentService;
import java.util.List;
import org.springframework.http.ResponseEntity;
@RestController
public class QRController {
        private final PaymentService paymentService;

    public QRController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/api/test")
    public String testAPI() {
        return "QR Tracker Backend is Working Successfully!";
    }
  @GetMapping("/api/verify-amount")
public String verifyAmount(@org.springframework.web.bind.annotation.RequestParam double amount) {

    if (amount <= 0) {
        return "Invalid amount! Amount must be greater than zero.";
    }

    return "Amount verified successfully: Rs. " + String.format("%.2f", amount);
}  
@GetMapping("/api/payment-status")
public String getPaymentStatus() {
    return "Payment Status: PENDING";
}
@GetMapping("/api/payment-details")
public Payment getPaymentDetails() {
    return new Payment(250.00, "PENDING");
}
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
@GetMapping("/api/payment-history")
public List<Payment> getPaymentHistory() {
    return paymentService.getPaymentHistory();
}
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
@GetMapping("/api/payments-by-status")
public List<Payment> getPaymentsByStatus(
        @RequestParam String status) {

    return paymentService.getPaymentsByStatus(status);
}
@GetMapping("/api/payment-total")
public long getTotalPayments() {
    return paymentService.getTotalPayments();
}
@GetMapping("/api/payment-success-total")
public long getSuccessfulPayments() {
    return paymentService.getSuccessfulPayments();
}
@GetMapping("/api/payment-failed-total")
public long getFailedPayments() {
    return paymentService.getFailedPayments();
}
@GetMapping("/api/payment-pending-total")
public long getPendingPayments() {
    return paymentService.getPendingPayments();
}
}
