package model;

import annotations.FragileWeightLimit;
import annotations.MatchesPattern;
import annotations.NumericRange;

import java.time.LocalDate;

@FragileWeightLimit(maxWeight = 5.0,
        message = "Крихке відправлення не може важити більше 5 кг")
public class Parcel {

    // Формат UPU S10: 2 літери + 9 цифр + 2 літери, напр. RA123456789UA
    @MatchesPattern(regex = "^[A-Z]{2}\\d{9}[A-Z]{2}$",
            message = "Трек-номер має формат: 2 літери, 9 цифр, 2 літери (напр. RA123456789UA)")
    private String trackingNumber;

    @NumericRange(min = 0.1, max = 30.0,
            message = "Вага має бути в межах від 0.1 до 30 кг")
    private double weight;

    private boolean fragile;

    private String destination;

    private LocalDate shippingDate;

    public Parcel(String trackingNumber, double weight, boolean fragile,
                  String destination, LocalDate shippingDate) {
        this.trackingNumber = trackingNumber;
        this.weight = weight;
        this.fragile = fragile;
        this.destination = destination;
        this.shippingDate = shippingDate;
    }

    public String getTrackingNumber() { return trackingNumber; }
    public double getWeight() { return weight; }
    public boolean isFragile() { return fragile; }
    public String getDestination() { return destination; }
    public LocalDate getShippingDate() { return shippingDate; }

    @Override
    public String toString() {
        return "Parcel{trackingNumber='" + trackingNumber + "', weight=" + weight
                + ", fragile=" + fragile + ", destination='" + destination
                + "', shippingDate=" + shippingDate + '}';
    }
}