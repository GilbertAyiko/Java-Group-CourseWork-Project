package methodsoverloading; 

class Driver {
    private final String driverId;
    private final String name;

    public Driver(String driverId, String name) {
        this.driverId = driverId;
        this.name = name;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getName() {
        return name;
    }
}

// Class to represent a Vehicle in the UTMS
class Vehicle {
    private final String vehicleId;
    private final String type; // e.g., "Bus", "Van"
    private final String registrationNumber;

    public Vehicle(String vehicleId, String type, String registrationNumber) {
        this.vehicleId = vehicleId;
        this.type = type;
        this.registrationNumber = registrationNumber;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getType() {
        return type;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }
}

class TransportServiceManager {

    /**
     * Overloaded method 1: Assigns a specific driver and a specific vehicle to a trip.
     * @param tripId The ID of the trip.
     * @param driver The Driver object to assign.
     * @param vehicle The Vehicle object to assign.
     */
    public void assignDriver(String tripId, Driver driver, Vehicle vehicle) {
        System.out.println("Scenario 1: Assigning specific driver and vehicle.");
        System.out.println("  Trip ID: " + tripId);
        System.out.println("  Driver Assigned: " + driver.getName() + " (ID: " + driver.getDriverId() + ")");
        System.out.println("  Vehicle Assigned: " + vehicle.getType() + " - " + vehicle.getRegistrationNumber() + " (ID: " + vehicle.getVehicleId() + ")");
        System.out.println("  ---------------------------------------------");
        // In a real system: update database records for trip assignment
    }

    /**
     * Overloaded method 2: Assigns a driver, intending to find a vehicle based on its type and a specific shift time.
     * @param tripId The ID of the trip.
     * @param driver The Driver object to assign.
     * @param vehicleType The desired type of vehicle (e.g., "Bus", "Van").
     * @param shiftTime The desired shift time (e.g., "Morning", "Evening", "Night").
     */
    public void assignDriver(String tripId, Driver driver, String vehicleType, String shiftTime) {
        System.out.println("Scenario 2: Assigning driver, looking for vehicle by type and shift.");
        System.out.println("  Trip ID: " + tripId);
        System.out.println("  Driver Assigned: " + driver.getName() + " (ID: " + driver.getDriverId() + ")");
        System.out.println("  Desired Vehicle Type: " + vehicleType);
        System.out.println("  Desired Shift Time: " + shiftTime);
        System.out.println("  (Logic here would involve querying for an available " + vehicleType + " for the " + shiftTime + " shift)");
        System.out.println("  ---------------------------------------------");
        // In a real system: Query available vehicles, then assign the first suitable one.
    }

    /**
     * Overloaded method 3: Assigns a driver based on a specific route name.
     * This might imply selecting a vehicle suitable for that route or ensuring driver's route familiarity.
     * @param tripId The ID of the trip.
     * @param driver The Driver object to assign.
     * @param routeName The name or ID of the route.
     */
    public void assignDriver(String tripId, Driver driver, String routeName) {
        System.out.println("Scenario 3: Assigning driver based on a specific route.");
        System.out.println("  Trip ID: " + tripId);
        System.out.println("  Driver Assigned: " + driver.getName() + " (ID: " + driver.getDriverId() + ")");
        System.out.println("  Assigned Route: " + routeName);
        System.out.println("  (Logic here might check driver's route familiarity or assign a default vehicle for this route)");
        System.out.println("  ---------------------------------------------");
        // In a real system: Ensure driver is qualified for the route, possibly auto-assign a route-specific vehicle.
    }
}

// The main class to run the demonstration
public class MethodOverloading { // This is the public class matching the file name

    public static void main(String[] args) {
        System.out.println("--- Demonstrating Method Overloading for assignDriver() in UTMS ---");
        System.out.println();

        // Create instances of Driver and Vehicle for the demonstration
        Driver gilbert = new Driver("D001", "Gilbert Ayikobua");
        Driver samuel = new Driver("D002", "Samuel Mabor");
        Driver umaru = new Driver("D003", "Umaru Nsubuga");

        Vehicle campusBus = new Vehicle("V001", "Bus", "UAG 123A");

        // Create an instance of the TransportServiceManager
        TransportServiceManager manager = new TransportServiceManager();

        // 1. Call the assignDriver method with (tripId, Driver, Vehicle) parameters
        System.out.println("Attempting assignment using specific driver and vehicle:");
        manager.assignDriver("VU_TRIP_001", gilbert, campusBus);

        // 2. Call the assignDriver method with (tripId, Driver, vehicleType, shiftTime) parameters
        System.out.println("Attempting assignment using driver, vehicle type, and shift time:");
        manager.assignDriver("VU_TRIP_002", samuel, "Van", "Morning");

        // 3. Call the assignDriver method with (tripId, Driver, routeName) parameters
        System.out.println("Attempting assignment using driver and route name:");
        manager.assignDriver("VU_TRIP_003", umaru, "Student Shuttle Route 1");
    }
}
