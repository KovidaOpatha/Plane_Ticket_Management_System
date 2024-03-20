import java.util.Scanner;

public class PlaneManagement {

    Scanner scanner = new Scanner(System.in);
    int choice;
    private static int[][] seats = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    private Ticket[] soldTickets = new Ticket[0];

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
                print_tickets_info();
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

    private double calculatePrice(String row, int seat) {
        char rowChar = Character.toUpperCase(row.charAt(0));
        int rowIndex = rowChar - 'A';
        int seatIndex = seat - 1;

        System.out.println("Row Index: " + rowIndex);
        System.out.println("Seat Index: " + seatIndex);

        double price = 0.0;

        if (rowIndex >= 0 && rowIndex < seats.length && seatIndex >= 0 && seatIndex < seats[rowIndex].length) {
            if ((rowIndex == 1 || rowIndex == 2) && seatIndex >= 9) {
                price = 180.0;
            } else if (seatIndex < 5) {
                price = 200.0;
            } else if (seatIndex >= 5 && seatIndex < 9) {
                price = 150.0;
            } else {
                price = 180.0;
            }
        }

        System.out.println("Price: " + price);
        return price;
    }

    private void buy_seat(Scanner scanner) {
        System.out.println("Enter row Number: ");
        String rowNumber = scanner.next();
        System.out.println("Enter Seat Number: ");
        int seatNumber = scanner.nextInt();

        if ((rowNumber.equalsIgnoreCase("A") || rowNumber.equalsIgnoreCase("D") && seatNumber <= 14) || (rowNumber.equalsIgnoreCase("B") || rowNumber.equalsIgnoreCase("C") && seatNumber <= 12)) {
            int rowIndex = Character.toUpperCase(rowNumber.charAt(0)) - 'A';
            int seatIndex = seatNumber - 1;

            if (seats[rowIndex][seatIndex] != 0) {
                System.out.println("Seat already taken. Please select another seat.");
            } else {
                seats[rowIndex][seatIndex] = 1;
                System.out.println("Enter your First Name:");
                String name = scanner.next();
                System.out.println("Enter your Last Name:");
                String surname = scanner.next();
                System.out.println("Enter your email:");
                String email = scanner.next();
                System.out.println("Seat booked successfully.");

                Person person = new Person(name, surname, email);

                // Calculate the price using the calculatePrice method
                double price = calculatePrice(rowNumber, seatNumber);

                // Print the calculated price for debugging
                System.out.println("Calculated price: £" + price);

                Ticket ticket = new Ticket(rowNumber, seatNumber, price, person);

                // Resize the soldTickets array to accommodate the new ticket
                Ticket[] newSoldTickets = new Ticket[soldTickets.length + 1];
                System.arraycopy(soldTickets, 0, newSoldTickets, 0, soldTickets.length);
                newSoldTickets[soldTickets.length] = ticket;
                soldTickets = newSoldTickets;

                System.out.println("Ticket price: £" + ticket.getPrice()); // Use getPrice() method to print the price
            }
        }
    }


    private void cancel_seat(Scanner scanner) {
        System.out.println("Enter row Letter: ");
        String row = scanner.next().toUpperCase(); // Convert row input to uppercase
        System.out.println("Enter Seat Number: ");
        int seat = scanner.nextInt();

        int rowIndex = row.charAt(0) - 'A';  // Convert row letter to index
        int seatIndex = seat - 1;  // Convert seat number to index

        if (rowIndex >= 0 && rowIndex < seats.length && seatIndex >= 0 && seatIndex < seats[rowIndex].length) {
            if (seats[rowIndex][seatIndex] == 1) {
                seats[rowIndex][seatIndex] = 0;
                System.out.println("Seat " + row + seat + " has been canceled.");

                // Find the ticket to cancel
                Ticket cancelledTicket = null;
                for (int i = 0; i < soldTickets.length; i++) {
                    if (soldTickets[i].getRow().equalsIgnoreCase(row) && soldTickets[i].getSeat() == seatIndex) {
                        cancelledTicket = soldTickets[i];
                        break;
                    }
                }

                // If ticket found, remove it from the soldTickets array
                if (cancelledTicket != null) {
                    Ticket[] newSoldTickets = new Ticket[soldTickets.length - 1];
                    int index = 0;
                    for (int i = 0; i < soldTickets.length; i++) {
                        if (soldTickets[i] != cancelledTicket) {
                            newSoldTickets[index++] = soldTickets[i];
                        }
                    }
                    soldTickets = newSoldTickets;
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
                if (seats[i][j] == 0) {
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

    private static void display_seat(Scanner scanner) {
        for (int i = 0; i < seats.length; i++) {
            System.out.println("");
            for (int j = 0; j < seats[i].length; j++) {
                if(seats[i][j] == 1){
                    System.out.print("X ");
                }else {
                    System.out.print("O ");
                }
            }
        }
    }

    public void print_tickets_info() {
        double totalPrice = 0.0;
        System.out.println("Tickets Information:");
        for (Ticket ticket : soldTickets) {
            String row = ticket.getRow();
            int seat = ticket.getSeat();
            double price = ticket.getPrice();

            System.out.println("Row: " + row + ", Seat: " + seat + ", Price: " + price);
            totalPrice += price;
        }

        System.out.println("Total Sales: £" + totalPrice);
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

    public static void main(String[] args) {
        PlaneManagement planeManagement = new PlaneManagement();
        do {
            planeManagement.printMenu();
            planeManagement.choice = planeManagement.getChoice();
            planeManagement.performAction(planeManagement.choice);
        } while (planeManagement.choice != 0);
        planeManagement.scanner.close();
    }
}
