package com.example.shippingcalculator.service;

import com.example.shippingcalculator.model.ShippingOption;
import com.google.maps.*;
import com.google.maps.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShippingService {

    @Value("${google.api.key}")
    private String googleApiKey;

    private static final List<String> CITIES = Arrays.asList("Delhi", "Mumbai", "Bangalore", "Kolkata", "Chennai");

    // Fixed Air Transport Data
    private static final Map<String, Double> AIR_COSTS = Map.of(
        "Delhi-Mumbai", 2000.0, "Delhi-Bangalore", 2500.0, "Delhi-Kolkata", 2200.0, "Delhi-Chennai", 1900.0,
        "Mumbai-Bangalore", 1800.0, "Mumbai-Kolkata", 2300.0, "Mumbai-Chennai", 2100.0,
        "Bangalore-Kolkata", 2600.0, "Bangalore-Chennai", 1200.0, "Kolkata-Chennai", 2400.0
    );

    private static final Map<String, Double> AIR_TIMES = Map.of(
        "Delhi-Mumbai", 2.0, "Delhi-Bangalore", 2.5, "Delhi-Kolkata", 2.2, "Delhi-Chennai", 0.8,
        "Mumbai-Bangalore", 1.8, "Mumbai-Kolkata", 2.3, "Mumbai-Chennai", 2.1,
        "Bangalore-Kolkata", 2.6, "Bangalore-Chennai", 1.0, "Kolkata-Chennai", 2.4
    );

    public List<ShippingOption> getShippingOptions(String destination) throws Exception {
        List<ShippingOption> options = new ArrayList<>();

        GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiKey).build();

        for (String city : CITIES) {
            if (!city.equals(destination)) {
                // Calculate road transport
                DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context)
                        .origins(city)
                        .destinations(destination)
                        .mode(TravelMode.DRIVING);

                DistanceMatrix result = request.await();

                long distanceInMeters = result.rows[0].elements[0].distance.inMeters;
                double distanceInKm = distanceInMeters / 1000.0;
                double timeInHours = Math.round(distanceInKm / 80); // Speed of 80 km/hr
                double cost = distanceInKm * 2; // ₹2 per km

                options.add(new ShippingOption(city, destination, "Road", cost, timeInHours));

                // Add air transport option
                String key = city + "-" + destination;
                if (AIR_COSTS.containsKey(key)) {
                    options.add(new ShippingOption(city, destination, "Air", AIR_COSTS.get(key), AIR_TIMES.get(key)));
                }
            }
        }

        return options;
    }

    public Map<String, String> getBestOptions(String destination) throws Exception {
        List<ShippingOption> options = getShippingOptions(destination);

        ShippingOption bestCost = Collections.min(options, Comparator.comparingDouble(ShippingOption::getCost));
        ShippingOption bestTime = Collections.min(options, Comparator.comparingDouble(ShippingOption::getTime));
        ShippingOption bestOverall = Collections.min(options, Comparator.comparingDouble(o -> (o.getCost() * 0.6) + (o.getTime() * 0.4)));

        Map<String, String> result = new HashMap<>();
        result.put("Best Cost Option", formatOption(bestCost));
        result.put("Best Time Option", formatOption(bestTime));
        result.put("Best Overall Option", formatOption(bestOverall));

        return result;
    }

    private String formatOption(ShippingOption option) {
        return option.getSource() + "-" + option.getDestination() + " | Mode: " + option.getMode() +
                " | Cost: ₹" + option.getCost() + " | Time: " + option.getTime() + " hrs";
    }
}
