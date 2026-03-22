import java.util.*;

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public boolean bookRoom(String type) {
        if (!inventory.containsKey(type) || inventory.get(type) == 0) {
            return false;
        }
        inventory.put(type, inventory.get(type) - 1);
        return true;
    }

    public void releaseRoom(String type) {
        if (inventory.containsKey(type)) {
            inventory.put(type, inventory.get(type) + 1);
        }
    }

    public void showInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

class CancellationService {
    private Stack<String> releasedRoomIds;
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Invalid reservation ID");
            return;
        }

        String roomType = reservationRoomTypeMap.remove(reservationId);
        inventory.releaseRoom(roomType);
        releasedRoomIds.push(reservationId);
        System.out.println("Booking cancelled for reservation ID: " + reservationId);
    }

    public void showRollbackHistory() {
        if (releasedRoomIds.isEmpty()) {
            System.out.println("No cancellations recorded");
            return;
        }

        System.out.println("Rollback History:");
        for (String id : releasedRoomIds) {
            System.out.println(id);
        }
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("single", 5);
        rooms.put("double", 3);
        rooms.put("suite", 2);
    }

    public boolean isRoomAvailable(String type) {
        return rooms.containsKey(type.toLowerCase()) && rooms.get(type.toLowerCase()) > 0;
    }

    public void bookRoom(String type) {
        type = type.toLowerCase();
        rooms.put(type, rooms.get(type) - 1);
    }
}

class ReservationValidator {

    public void validate(String guestName, String roomType, RoomInventory inventory) throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        if (!inventory.isRoomAvailable(roomType)) {
            throw new InvalidBookingException("Requested room type is not available");
        }
import java.util.*;

class Reservation {

    private String customerName;
    private String roomType;
    private int nights;

    public Reservation(String customerName, String roomType, int nights) {
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String toString() {
        return "Customer: " + customerName + ", Room: " + roomType + ", Nights: " + nights;
    }
}

class BookingHistory {

    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

class BookingReportService {

    public void generateReport(BookingHistory history) {

        System.out.println("\n===== BOOKING REPORT =====");

        if (history.getConfirmedReservations().isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        for (Reservation r : history.getConfirmedReservations()) {
            System.out.println(r);
        }

        System.out.println("Total Bookings: " + history.getConfirmedReservations().size());
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        System.out.print("Enter number of room types: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter room type: ");
            String type = sc.nextLine();
            System.out.print("Enter count: ");
            int count = sc.nextInt();
            sc.nextLine();
            inventory.addRoomType(type, count);
        }

        while (true) {
            System.out.println("\n1.Book Room");
            System.out.println("2.Cancel Booking");
            System.out.println("3.Show Inventory");
            System.out.println("4.Show Rollback History");
            System.out.println("5.Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter reservation ID: ");
                String id = sc.nextLine();
                System.out.print("Enter room type: ");
                String type = sc.nextLine();

                if (inventory.bookRoom(type)) {
                    service.registerBooking(id, type);
                    System.out.println("Booking confirmed");
                } else {
                    System.out.println("Room not available");
                }

            } else if (choice == 2) {
                System.out.print("Enter reservation ID to cancel: ");
                String id = sc.nextLine();
                service.cancelBooking(id, inventory);

            } else if (choice == 3) {
                inventory.showInventory();

            } else if (choice == 4) {
                service.showRollbackHistory();

            } else if (choice == 5) {
                break;
            }
        }

        sc.close();

        Scanner scanner = new Scanner(System.in);
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();

        System.out.print("Enter guest name: ");
        String guestName = scanner.nextLine();

        System.out.print("Enter room type (single/double/suite): ");
        String roomType = scanner.nextLine();

        try {
            validator.validate(guestName, roomType, inventory);
            inventory.bookRoom(roomType);
            System.out.println("Booking successful for " + guestName + " in a " + roomType + " room.");
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        scanner.close();
        Scanner sc = new Scanner(System.in);
        BookingHistory history = new BookingHistory();

        System.out.print("Enter number of reservations: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 1; i <= n; i++) {

            System.out.println("\nReservation " + i);

            System.out.print("Enter customer name: ");
            String name = sc.nextLine();

            System.out.print("Enter room type: ");
            String room = sc.nextLine();

            System.out.print("Enter number of nights: ");
            int nights = sc.nextInt();
            sc.nextLine();

            history.addReservation(new Reservation(name, room, nights));
        }

        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);
    }
}
