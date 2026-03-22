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
    }
}