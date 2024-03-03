package com.assignment.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.assignment.calculator.DistanceCalculator;
import com.assignment.calculator.DistanceCalculatorFactory;
import com.assignment.calculator.DistanceCalculatorTypes;
import com.assignment.dto.Location;

@Component
public class BestRouteHelper {

  @Autowired private DistanceCalculatorFactory distanceCalculatorFactory;

  @Value("${distance.calculator.type}")
  private String distanceCalculatorType;

  @Value("${average.speed}")
  private double averageSpeed; // Average speed in km/hr

  private double calculateTravelTime(double distance) {
    return distance / averageSpeed; // Travel time in hours
  }

  public String findShortestRoute(
      Location amanLocation,
      Location r1Location,
      Location r2Location,
      Location c1Location,
      Location c2Location,
      int timeTakenToPrepareMeal1,
      int timeTakenToPrepareMeal2) {

    DistanceCalculator distanceCalculator =
        distanceCalculatorFactory.getDistanceCalculator(
            DistanceCalculatorTypes.valueOf(distanceCalculatorType));

    // Calculate distances
    double dAmanR1 = distanceCalculator.calculateDistance(amanLocation, r1Location);
    double dAmanR2 = distanceCalculator.calculateDistance(amanLocation, r2Location);
    double dR1C1 = distanceCalculator.calculateDistance(r1Location, c1Location);
    double dR2C2 = distanceCalculator.calculateDistance(r2Location, c2Location);

    // Calculate travel times
    double tAmanR1 = calculateTravelTime(dAmanR1);
    double tAmanR2 = calculateTravelTime(dAmanR2);
    double tR1C1 = calculateTravelTime(dR1C1);
    double tR2C2 = calculateTravelTime(dR2C2);

    // Total time for Route 1: Aman -> R1 -> C1 -> R2 -> C2
    double totalRoute1 =
        tAmanR1 + timeTakenToPrepareMeal1 + tR1C1 + timeTakenToPrepareMeal2 + tR2C2;

    // Total time for Route 2: Aman -> R2 -> C2 -> R1 -> C1
    double totalRoute2 =
        tAmanR2 + timeTakenToPrepareMeal2 + tR2C2 + timeTakenToPrepareMeal1 + tR1C1;

    return findShortestRoute(totalRoute1, totalRoute2);
  }

  private String findShortestRoute(
      double totalRoute1, double totalRoute2) { // Find the shortest route
    if (totalRoute1 < totalRoute2) {
      return "Route 1: Aman -> R1 -> C1 -> R2 -> C2";
    } else {
      return "Route 2: Aman -> R2 -> C2 -> R1 -> C1";
    }
  }
}
