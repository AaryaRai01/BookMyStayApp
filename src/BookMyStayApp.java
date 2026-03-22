import java.util.*;

class AddonService {
    private String serviceName;
    private double cost;

    public AddonService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

class AddonServiceManager {
    private Map<String, List<AddonService>> servicesByReservation;

    public AddonServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    public void addService(String reservationId, AddonService service) {
        if (!servicesByReservation.containsKey(reservationId)) {
            servicesByReservation.put(reservationId, new ArrayList<>());
        }
        servicesByReservation.get(reservationId).add(service);
    }

    public double calculateTotalServiceCost(String reservationId) {
        if (!servicesByReservation.containsKey(reservationId)) {
            return 0.0;
        }

        double total = 0.0;
        for (AddonService s : servicesByReservation.get(reservationId)) {
            total += s.getCost();
        }
        return total;
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        AddonServiceManager manager = new AddonServiceManager();

        AddonService breakfast = new AddonService("Breakfast", 20.0);
        AddonService spa = new AddonService("Spa", 50.0);
        AddonService pickup = new AddonService("Airport Pickup", 30.0);

        String reservationId = "R001";

        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, spa);
        manager.addService(reservationId, pickup);

        double total = manager.calculateTotalServiceCost(reservationId);

        System.out.println("Total Add-on Service Cost: " + total);
    }
}