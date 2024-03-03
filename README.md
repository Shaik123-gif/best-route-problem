**Best Route Problem**<br/>
I have solved this problem using java spring boot framework (As I am currently working on this framework, I am comfortable in this framework). 
I am pretty sure that I have written a production ready code.<br/><br/>
I also used strategy and factory design patterns in this application to achieve readability, modularity and extensibility.<br/>
I have used all the OOPs concepts like inheritance, abstraction, encapsulation and complile time polymorphism (overloading).<br/>

**Code Explanation**<br/>
Code starts from *BestRouteProjectApplication* class, where the main method is present. I have annotated a method with @PostConstruct, which means once the spring boot main method
is executed, after that this method will be invoked.<br/>
I have set coordinates manually for consumer, restaurant and the delivery executive. we can configure these values in application.properties also.<br/>
```
@SpringBootApplication
public class BestRouteProjectApplication {

  @Autowired private BestRouteHelper bestRouteHelper;

  @PostConstruct
  public void init() {

    // Here I am setting the locations manually.
    // All these values, we can drive through configuration.
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
```
I have created a *Location* class, which consists of longitude and latitude as the attributes, and I have provided *setLocation* and *getLocation* methods to the outside of the class to access these longitude and latitude attributes (*Encapsulation*).
I have created Consumer, Restaurant, DeliveryExecutive classes which inherits Location (*Inheritance*).<br/>
```
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
```

```
public class Consumer extends Location {}
```
*BestRouteHelper*: This class contains *findShortestRoute* method to find the shortest route. This method is invoked from the main class.
First we are calculating the distances from aman to restaurants and from restaurants to consumers. After that we are calculating the time taken to travel aman to restaurants and from restaurants to consumers.
After finding all those values, we are adding up and finding the best route. Also some of the values like average speed I have taken it from configuration file (application.properties). So that no need to change the code when we want to increase or decrease the average speed.<br/>
```
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
      double totalRoute1, double totalRoute2) {
    if (totalRoute1 < totalRoute2) {
      return "Route 1: Aman -> R1 -> C1 -> R2 -> C2";
    } else {
      return "Route 2: Aman -> R2 -> C2 -> R1 -> C1";
    }
  }
}
```
*Strategy Design Pattern*: As of now, we are going with haversine formula to calculate the distance. But what if tomorrow we want to use other formula to calculate the distance. For that we need to do more changes for our code.
Thus, why I am solving this problem using strategy design pattern. We can have an interface which has a method to calculate the distance. After that we need to create implementations of this interface with different distance calculators.<br/><br/>
*DistanceCalculator*: An interface which contains a method to calculateDistance. (*Abstraction*)<br/>
```
public interface DistanceCalculator {
  double calculateDistance(Location location1, Location location2);
}
```
*DistanceCalculatorTypes*:  An enum which consists of all the distance calculator types. (As of now we have only one distance calculator type - HAVERSINE)<br/>
```
public enum DistanceCalculatorTypes {
  HAVERSINE
}
```
*HaversineDistanceCalculator*: This class calculates the distance between two locations using haversine formula.<br/>
```
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
```
*DistanceCalculatorFactory*: This class acts as a factory, which gives the instance of distance calculator based on the provided distanceCalculatorType. In this case, as we are going with haversine, it will give us haversine distance calculator class. (Factory Design Pattern)<br/>
```
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
```



