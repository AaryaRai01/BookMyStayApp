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