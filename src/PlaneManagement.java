import java.util.ArrayList;
import java.util.Scanner;

public class PlaneManagement {

    Scanner scanner = new Scanner(System.in);
    int choice;
    private static String[][] seats = {
            {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
            {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
            {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
            {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
    };
    private ArrayList<Ticket> soldTickets = new ArrayList<>();

    public void printMenu() {
        System.out.println("");
        System.out.println("************************************************");
        System.out.println("   Welcome to the Plane Management Application  ");
        System.out.println("************************************************");
        System.out.println("*                  MENU OPTIONS                *");
        System.out.println("************************************************");
        System.out.println("    1) Buy a seat");
        System.out.println("    2) Cancel a seat");
        System.out.println("    3) Find first available seat");
        System.out.println("    4) Show seating plan");
        System.out.println("    5) Print tickets information and total sales");
        System.out.println("    6) Search ticket");
        System.out.println("    0) Quit");
        System.out.println("************************************************");
        System.out.print("Please select an option: ");
    }

    public int getChoice() {
        int choice;
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number between 0 and 6: ");
            scanner.next(); // Clear the buffer
        }
        choice = scanner.nextInt();
        if (choice < 0 || choice > 6) {
            System.out.println("Invalid choice. Please enter a number between 0 and 6: ");
            return getChoice(); // Recursively get valid input
        }
        return choice;
    }

    public void performAction(int choice) {
        switch (choice) {
            case 1:
                buy_seat(scanner);
                break;
            case 2:
                cancel_seat(scanner);
                break;
            case 3:
                find_first_available(scanner);
                break;
            case 4:
                display_seat(scanner);
                break;
            case 5:
                System.out.println("Simulating printing tickets and total sales...");
                break;
            case 6:
                search_ticket(scanner);
                break;
            case 0:
                System.out.println("Exiting Program");
                System.exit(0);
                break;
            default:
                System.out.println("Unexpected choice. Please try again.");
        }
    }

    private void buy_seat(Scanner scanner) {
        System.out.println("Enter row Number: ");
        String rowNumber = scanner.next();
        System.out.println("Enter Seat Number: ");
        int seatNumber = scanner.nextInt();

        if ((rowNumber.equalsIgnoreCase("A") || rowNumber.equalsIgnoreCase("D") && seatNumber <= 14) || (rowNumber.equalsIgnoreCase("B") || rowNumber.equalsIgnoreCase("C") && seatNumber <= 12)) {
            int rowIndex = Character.toUpperCase(rowNumber.charAt(0)) - 'A';
            int seatIndex = seatNumber - 1;

            if (seats[rowIndex][seatIndex] == "X") {
                System.out.println("Seat already taken. Please select another seat.");
            } else {
                seats[rowIndex][seatIndex] = "X";
                System.out.println("Enter your First Name:");
                String name = scanner.next();
                System.out.println("Enter your Last Name:");
                String surname = scanner.next();
                System.out.println("Enter your email:");
                String email = scanner.next();
                System.out.println("Seat booked successfully.");

                Person person = new Person(name, surname, email);
                Ticket ticket = new Ticket(rowNumber, seatNumber, calculatePrice(rowNumber, seatNumber), person);
                soldTickets.add(ticket);
                System.out.println("Tickets sold:");
                for (Ticket t : soldTickets) {
                    System.out.println("Row: " + t.getRow() + ", Seat: " + t.getSeat() + ", Price: " + t.getPrice() + ", Name: " + t.getPerson().getName() + ", Surname: " + t.getPerson().getSurname() + ", Email: " + t.getPerson().getEmail());
                }
            }
        }else {
            System.out.println("Invalid row or seat number.");
        }
    }

    private double calculatePrice(String rowNumber, int seatNumber) {
        return 0;
    }

    private static void display_seat(Scanner scanner){
        for (int i = 0; i < seats.length; i++) {
            System.out.println("");
            for (int j = 0; j < seats[i].length; j++) {
                System.out.print(seats[i][j] + " ");
            }
        }
    }

    private void cancel_seat(Scanner scanner) {
        System.out.println("Enter row Letter: ");
        String row = scanner.next().toUpperCase(); // Convert input to uppercase
        System.out.println("Enter Seat Number: ");
        int seat = scanner.nextInt();

        int rowNumber = row.charAt(0) - 'A';  // Convert row letter to index
        int seatNumber = seat - 1;  // Convert seat number to index

        if (rowNumber >= 0 && rowNumber < seats.length && seatNumber >= 0 && seatNumber < seats[rowNumber].length) {
            if (seats[rowNumber][seatNumber].equals("X")) {
                seats[rowNumber][seatNumber] = "0";
                System.out.println("Seat " + row + seat + " has been canceled.");
                Ticket cancelledTicket = findTicket(row, seat);
                if (cancelledTicket != null) {
                    soldTickets.remove(cancelledTicket);
                }
            } else {
                System.out.println("Seat " + row + seat + " is already available.");
            }
        } else {
            System.out.println("Invalid row or seat number.");
        }
    }

    private static void find_first_available(Scanner scanner) {
        boolean found = false;
        char[] rows = {'A', 'B', 'C', 'D'};

        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j].equals("0")) {
                    System.out.println("The first available seat is: Row " + rows[i] + ", Seat " + (j + 1));
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        if (!found) {
            System.out.println("No available seats found.");
        }
    }
    private void search_ticket(Scanner scanner) {
        System.out.println("Enter row Letter: ");
        String row = scanner.next();
        System.out.println("Enter Seat Number: ");
        int seat = scanner.nextInt();

        Ticket ticket = findTicket(row, seat);
        if (ticket != null) {
            System.out.println("Ticket found:");
            System.out.println("Row: " + ticket.getRow() + ", Seat: " + ticket.getSeat());
            System.out.println("Price: " + ticket.getPrice());
            System.out.println("Person Information:");
            System.out.println("Name: " + ticket.getPerson().getName());
            System.out.println("Surname: " + ticket.getPerson().getSurname());
            System.out.println("Email: " + ticket.getPerson().getEmail());
        } else {
            System.out.println("Ticket not found.");
        }
    }

    private Ticket findTicket(String row, int seat) {
        for (Ticket ticket : soldTickets) {
            if (ticket.getRow().equalsIgnoreCase(row) && ticket.getSeat() == seat) {
                return ticket;
            }
        }
        return null;
    }

    public static void main (String[] args){
        PlaneManagement planeManagement = new PlaneManagement();
        do {
            planeManagement.printMenu();
            planeManagement.choice = planeManagement.getChoice();
            planeManagement.performAction(planeManagement.choice);
        } while (planeManagement.choice != 0);
        planeManagement.scanner.close();
    }
}
