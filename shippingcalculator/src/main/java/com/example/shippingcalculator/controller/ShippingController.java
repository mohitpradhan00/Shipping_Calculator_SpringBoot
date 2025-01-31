package com.example.shippingcalculator.controller;

import com.example.shippingcalculator.service.ShippingService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shipping")
@CrossOrigin("*")
public class ShippingController {

    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping
    public Map<String, String> getShipping(@RequestParam String destination) throws Exception {
        return shippingService.getBestOptions(destination);
    }
}
