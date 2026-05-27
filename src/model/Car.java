package model;

public class Car {
    private int id;
    private String make;
    private String model;
    private int year;
    private String color;
    private String vin;
    private double price;
    private String status;

    public Car(int id, String make, String model, int year, String color, String vin, double price, String status) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.vin = vin;
        this.price = price;
        this.status = status;
    }

    public int getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getColor() { return color; }
    public String getVin() { return vin; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}