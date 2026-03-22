import java.util.*;

class BookingRequest {
    private String customerName;
    private int roomsRequested;

    public BookingRequest(String customerName, int roomsRequested) {
        this.customerName = customerName;
        this.roomsRequested = roomsRequested;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getRoomsRequested() {
        return roomsRequested;
    }
}

class BookingRequestQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notifyAll();
    }

    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        return queue.poll();
    }
}

class RoomInventory {
    private int availableRooms;

    public RoomInventory(int rooms) {
        this.availableRooms = rooms;
    }

    public synchronized boolean allocateRooms(int rooms) {
        if (availableRooms >= rooms) {
            availableRooms -= rooms;
            return true;
        }
        return false;
    }

    public synchronized int getAvailableRooms() {
        return availableRooms;
    }
}

class RoomAllocationService {
    public void processRequest(BookingRequest request, RoomInventory inventory) {
        boolean success = inventory.allocateRooms(request.getRoomsRequested());
        if (success) {
            System.out.println(Thread.currentThread().getName() + " booked " + request.getRoomsRequested() + " room(s) for " + request.getCustomerName());
        } else {
            System.out.println(Thread.currentThread().getName() + " failed to book " + request.getRoomsRequested() + " room(s) for " + request.getCustomerName());
        }
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(BookingRequestQueue bookingQueue, RoomInventory inventory, RoomAllocationService allocationService) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    public void run() {
        while (true) {
            BookingRequest request = bookingQueue.getRequest();
            if (request == null) break;
            allocationService.processRequest(request, inventory);
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter total rooms available: ");
        int rooms = sc.nextInt();

        System.out.print("Enter number of booking requests: ");
        int n = sc.nextInt();
        sc.nextLine();

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory(rooms);
        RoomAllocationService allocationService = new RoomAllocationService();

        for (int i = 0; i < n; i++) {
            System.out.print("Customer name: ");
            String name = sc.nextLine();
            System.out.print("Rooms requested: ");
            int r = sc.nextInt();
            sc.nextLine();
            bookingQueue.addRequest(new BookingRequest(name, r));
        }

        Thread t1 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));

        t1.start();
        t2.start();

        Thread.sleep(1000);
        t1.interrupt();
        t2.interrupt();

        t1.join();
        t2.join();

        System.out.println("Remaining rooms: " + inventory.getAvailableRooms());
    }
}