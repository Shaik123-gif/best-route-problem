package com.assignment.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistanceCalculatorFactory {
  @Autowired private HaversineDistanceCalculator haversineDistanceCalculator;

  private DistanceCalculatorFactory() {}

  public DistanceCalculator getDistanceCalculator(DistanceCalculatorTypes distanceCalculatorType) {
    if (distanceCalculatorType.equals(DistanceCalculatorTypes.HAVERSINE)) {
      return haversineDistanceCalculator;
    }
    return null;
  }
}
