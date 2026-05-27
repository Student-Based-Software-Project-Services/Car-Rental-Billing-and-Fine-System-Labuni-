package model;

import java.sql.Date;

public class Transaction {
    private int id;
    private int customerId;
    private int carId;
    private double salePrice;
    private Date saleDate;
    private Date dueDate;
    private String status;
    private String customerName;
    private String carInfo;

    public Transaction(int id, int customerId, int carId, double salePrice, Date saleDate, Date dueDate, String status) {
        this.id = id;
        this.customerId = customerId;
        this.carId = carId;
        this.salePrice = salePrice;
        this.saleDate = saleDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getCarId() { return carId; }
    public double getSalePrice() { return salePrice; }
    public Date getSaleDate() { return saleDate; }
    public Date getDueDate() { return dueDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCarInfo() { return carInfo; }
    public void setCarInfo(String carInfo) { this.carInfo = carInfo; }
}