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
    }
}