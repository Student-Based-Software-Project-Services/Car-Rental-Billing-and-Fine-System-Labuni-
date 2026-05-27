package model;

import java.sql.Date;

public class Payment {
    private int id;
    private int transactionId;
    private double amountPaid;
    private Date paymentDate;
    private double fineApplied;

    public Payment(int id, int transactionId, double amountPaid, Date paymentDate, double fineApplied) {
        this.id = id;
        this.transactionId = transactionId;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.fineApplied = fineApplied;
    }

    public int getId() { return id; }
    public int getTransactionId() { return transactionId; }
    public double getAmountPaid() { return amountPaid; }
    public Date getPaymentDate() { return paymentDate; }
    public double getFineApplied() { return fineApplied; }
}