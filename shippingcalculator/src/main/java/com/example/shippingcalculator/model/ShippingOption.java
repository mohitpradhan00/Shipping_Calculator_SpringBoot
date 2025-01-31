package com.example.shippingcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShippingOption {
    private String source;
    private String destination;
    private String mode;
    private double cost;
    private double time;
}
