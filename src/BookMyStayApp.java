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
