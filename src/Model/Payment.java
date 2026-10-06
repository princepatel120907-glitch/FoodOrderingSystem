package Model;

import java.sql.Timestamp;

public class Payment {
    private int id;
    private int orderId;
    private double amount;
    private String method; // CASH/UPI/CREDIT_CARD/DEBIT_CARD
    private String status; // SUCCESS/FAILED/PENDING/REFUNDED
    private String transactionId;
    private Timestamp paymentDate;
    private String cardLastFour;
    private String upiId;

    public Payment() {}

    public Payment(int id, int orderId, double amount, String method, String status,
                   String transactionId, Timestamp paymentDate, String cardLastFour, String upiId) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
        this.cardLastFour = cardLastFour;
        this.upiId = upiId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Timestamp getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }

    public String getCardLastFour() { return cardLastFour; }
    public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", method='" + method + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}