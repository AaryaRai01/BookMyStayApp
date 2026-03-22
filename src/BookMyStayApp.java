import java.io.*;
import java.util.*;

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public void setRoom(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

class FilePersistenceService {

    public void saveInventory(RoomInventory inventory, String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (Map.Entry<String, Integer> entry : inventory.getInventory().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }

            writer.close();
            System.out.println("Inventory saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving inventory");
        }
    }

    public void loadInventory(RoomInventory inventory, String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                String roomType = parts[0];
                int count = Integer.parseInt(parts[1]);
                inventory.setRoom(roomType, count);
            }

            reader.close();
            System.out.println("Inventory loaded successfully");
        } catch (IOException e) {
            System.out.println("Error loading inventory");
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService service = new FilePersistenceService();

        System.out.print("Enter number of room types: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {

            System.out.print("Enter room type: ");
            String type = sc.nextLine();

            System.out.print("Enter available rooms: ");
            int count = sc.nextInt();
            sc.nextLine();

            inventory.setRoom(type, count);
        }

        System.out.print("Enter file name to save data: ");
        String filePath = sc.nextLine();

        service.saveInventory(inventory, filePath);

        RoomInventory loadedInventory = new RoomInventory();

        service.loadInventory(loadedInventory, filePath);

        System.out.println("Recovered Inventory:");
        loadedInventory.displayInventory();
    }
}