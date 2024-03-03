package com.assignment.calculator;

import org.springframework.stereotype.Component;

import com.assignment.dto.Location;

@Component
public class HaversineDistanceCalculator implements DistanceCalculator {

  @Override
  public double calculateDistance(Location location1, Location location2) {
    double lat1 = location1.getLatitude();
    double lon1 = location1.getLongitude();
    double lat2 = location2.getLatitude();
    double lon2 = location2.getLongitude();
    double r = 6371; // Radius of the Earth in kilometers

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a =
        Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return r * c;
  }
}
