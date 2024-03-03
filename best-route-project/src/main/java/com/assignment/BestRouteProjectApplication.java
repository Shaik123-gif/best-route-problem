package com.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.assignment.entity.Consumer;
import com.assignment.entity.DeliveryExecutive;
import com.assignment.entity.Restaurant;
import com.assignment.helper.BestRouteHelper;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BestRouteProjectApplication {

  @Autowired private BestRouteHelper bestRouteHelper;

  @PostConstruct
  public void init() {

    // Here I am setting the locations manually here.
    // All these values, we can drive from configuration also.
    // (We can configure all these values in application.properties)

    DeliveryExecutive executive = new DeliveryExecutive();
    executive.setLocation(12.9345, 77.6100); // Aman's current location

    Consumer consumer1 = new Consumer();
    consumer1.setLocation(12.9375, 77.6266); // Consumer 1's location

    Consumer consumer2 = new Consumer();
    consumer2.setLocation(12.9308, 77.6226); // Consumer 2's location

    Restaurant restaurant1 = new Restaurant();
    restaurant1.setLocation(12.9342, 77.6221); // Restaurant 1's location

    Restaurant restaurant2 = new Restaurant();
    restaurant2.setLocation(12.9286, 77.6238); // Restaurant 2's location

    int timeTakenToPrepareMeal1 = 10; // Time taken to prepare meal at Restaurant 1 (in minutes)
    int timeTakenToPrepareMeal2 = 20; // Time taken to prepare meal at Restaurant 2 (in minutes)

    String shortestRoute =
        bestRouteHelper.findShortestRoute(
            executive.getLocation(),
            restaurant1.getLocation(),
            restaurant2.getLocation(),
            consumer1.getLocation(),
            consumer2.getLocation(),
            timeTakenToPrepareMeal1,
            timeTakenToPrepareMeal2);
    System.out.println("Shortest Route: " + shortestRoute);
  }

  public static void main(String[] args) {
    SpringApplication.run(BestRouteProjectApplication.class, args);
  }
}
