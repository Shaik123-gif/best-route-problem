package com.assignment.dto;

public class Location {
  private double longitude;
  private double latitude;

  public Location() {}

  private Location(double longitude, double latitude) {
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public void setLocation(double longitude, double latitude) {
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public Location getLocation() {
    return new Location(longitude, latitude);
  }

  public double getLongitude() {
    return longitude;
  }

  public double getLatitude() {
    return latitude;
  }
}
