
package com.qrtracker.database;

import com.qrtracker.model.Payment;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {

    public boolean savePayment(Payment payment) {

        String sql = """
                INSERT INTO payments
                (fixed_amount, received_amount, transaction_id, status)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(
                    1, payment.getFixedAmount().toPlainString());

            if (payment.getReceivedAmount() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(
                        2, payment.getReceivedAmount().toPlainString());
            }

            statement.setString(3, payment.getTransactionId().trim());
            statement.setString(4, payment.getStatus());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            if (e.getErrorCode() == 19) {
                return false;
            }

            throw new IllegalStateException(
                    "Unable to save payment record.", e);
        }
    }
    public List<Payment> getAllPayments() {

    List<Payment> payments = new ArrayList<>();

    String sql = """
            SELECT fixed_amount, received_amount,
                   transaction_id, status
            FROM payments
            ORDER BY id DESC
            """;

    try (Connection connection = DatabaseManager.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {

            Payment payment = new Payment();

            payment.setFixedAmount(
                    new BigDecimal(resultSet.getString("fixed_amount")));

            String receivedAmount =
                    resultSet.getString("received_amount");

            if (receivedAmount != null) {
                payment.setReceivedAmount(
                        new BigDecimal(receivedAmount));
            }

            payment.setTransactionId(
                    resultSet.getString("transaction_id"));

            payment.setStatus(
                    resultSet.getString("status"));

            payments.add(payment);
        }

    } catch (SQLException e) {
        throw new IllegalStateException(
                "Unable to retrieve payment history.", e);
    }

    return payments;
}
public Payment getPaymentByTransactionId(String transactionId) {

    String sql = """
            SELECT fixed_amount, received_amount,
                   transaction_id, status
            FROM payments
            WHERE transaction_id = ?
            """;

    try (Connection connection = DatabaseManager.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, transactionId.trim());

        try (ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                Payment payment = new Payment();

                payment.setFixedAmount(
                        new BigDecimal(resultSet.getString("fixed_amount")));

                String receivedAmount =
                        resultSet.getString("received_amount");

                if (receivedAmount != null) {
                    payment.setReceivedAmount(
                            new BigDecimal(receivedAmount));
                }

                payment.setTransactionId(
                        resultSet.getString("transaction_id"));

                payment.setStatus(
                        resultSet.getString("status"));

                return payment;
            }
        }

    } catch (SQLException e) {
        throw new IllegalStateException(
                "Unable to find payment record.", e);
    }

    return null;
}
public List<Payment> getPaymentsByStatus(String status) {

    List<Payment> payments = new ArrayList<>();

    if (status == null || status.trim().isEmpty()) {
        return payments;
    }

    status = status.trim().toUpperCase();

    String sql = """
            SELECT fixed_amount, received_amount,
                   transaction_id, status
            FROM payments
            WHERE status = ?
            ORDER BY id DESC
            """;

    try (Connection connection = DatabaseManager.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, status.toUpperCase());

        try (ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Payment payment = new Payment();

                payment.setFixedAmount(
                        new BigDecimal(resultSet.getString("fixed_amount")));

                String receivedAmount =
                        resultSet.getString("received_amount");

                if (receivedAmount != null) {
                    payment.setReceivedAmount(
                            new BigDecimal(receivedAmount));
                }

                payment.setTransactionId(
                        resultSet.getString("transaction_id"));

                payment.setStatus(
                        resultSet.getString("status"));

                payments.add(payment);
            }
        }

    } catch (SQLException e) {
        throw new IllegalStateException(
                "Unable to filter payment records.", e);
    }

    return payments;
}

}