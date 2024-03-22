import java.util.Scanner;
public class PlaneManagement {

    Scanner scanner = new Scanner(System.in);
    int choice;
    //Define seats array
    private static int[][] seats = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    //Define Array to store Ticket data
    private Ticket[][] soldTickets = new Ticket[4][14];

    //Prints the menu options for the plane management application
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
        System.out.print("Please Select an Option(0-6): ");
    }
    //gGets the user's choice from the menu, ensuring it is a valid integer between 0 and 6.
    public int getChoice() {
        int choice;
        // Repeat until a valid input enter
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

    //Performs the action based on the user's choice from the menu.
    public void performAction(int choice) {
        // Perform different actions based on the user's choice
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

    //Calculates the price of a seat based on its row and seat number.
    private double calculatePrice(String row, int seat) {
        // Convert the row letter to uppercase and calculate the array indices
        char rowChar = Character.toUpperCase(row.charAt(0));
        int rowIndex = rowChar - 'A';
        int seatIndex = seat - 1;

        double price = 0.0;

        // Checking inputs for row and seat are valid
        if (rowIndex >= 0 && rowIndex < seats.length && seatIndex >= 0 && seatIndex < seats[rowIndex].length) {
            // Assign price based on row and seat number
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
        //return the calculated price
        return price;
    }

    //Allows the user to buy a seat, inputting their name, surname, and email. It also calculates the price of the seat and saves ticket information to a file.
    private void buy_seat(Scanner scanner) {
        // Loop until a seat is successfully booked
        while (true) {
            System.out.print("Enter ROW Letter (A,B,C,D): ");
            String rowNumber = scanner.next().toUpperCase(); // Convert input to uppercase for consistency

            //checking inputs are correct
            if (!isValidRow(rowNumber)) {
                System.out.println("Invalid row number. Please select between (A,B,C,D).");
                continue; // Repeat the loop to ask for row number again
            }

            System.out.print("Enter a SEAT Number: ");
            int seatNumber = GetSeat(rowNumber);

            //validating  seat number
            if (!isValidSeat(rowNumber, seatNumber)) {
                System.out.println("Invalid seat number for row " + rowNumber +
                        ". Please select a seat number between 1 and " + (rowNumber.equalsIgnoreCase("A") || rowNumber.equalsIgnoreCase("D") ? 14 : 12));
                continue; // Repeat the loop to ask for seat number again
            }

            //calculate the array indices for the seats
            int rowIndex = Character.toUpperCase(rowNumber.charAt(0)) - 'A';
            int seatIndex = seatNumber - 1;

            //checking if the seat is already booked
            if (seats[rowIndex][seatIndex] != 0) {
                System.out.println("Sorry!! Seat is Already Taken. Please select another Seat.");
            } else {
                //booking the seat
                seats[rowIndex][seatIndex] = 1;

                //getting inputs from the user
                System.out.print("Enter your First Name:");
                String name = scanner.next();
                System.out.print("Enter your Sure Name:");
                String surname = scanner.next();
                System.out.print("Enter your email:");
                String email = scanner.next();
                //displaying booking details(optional)
                System.out.println("------------------------------");
                System.out.println("       Booking Details"        );
                System.out.println("------------------------------");
                System.out.println("Name: "+name+" "+surname);
                System.out.println("Email: "+email);
                System.out.println("Seat: Row "+rowNumber+" Seat "+seatNumber);
                System.out.println("------------------------------");
                System.out.println("Seat "+rowNumber+seatNumber+ " Booked Successfully.");

                //creating a Person object
                Person person = new Person(name, surname, email);

                // Calculate the price using the calculatePrice method
                double price = calculatePrice(rowNumber, seatNumber);

                //creating a  Ticket object
                Ticket ticket = new Ticket(rowNumber, seatNumber, price, person);

                //Store the ticket in the soldTickets array
                soldTickets[rowIndex][seatIndex] = ticket;
                ticket.save(); //saving ticket informations in a file

                // Exit the loop if the seat is successfully booked
            }
            break;
        }
    }

    //Checks if the input row number is valid (A, B, C, or D).
    private boolean isValidRow(String rowNumber) {
        return rowNumber.equalsIgnoreCase("A") || rowNumber.equalsIgnoreCase("B") || rowNumber.equalsIgnoreCase("C") || rowNumber.equalsIgnoreCase("D");
    }

    // Gets a valid seat number for the given row.
    private int GetSeat(String rowNumber) {
        int seatNumber= 0;
        scanner.nextLine();
        try{
            seatNumber = scanner.nextInt();
        } catch (Exception e) {
            // Catch any exception that occurs during input reading
            // Display an error message and prompt the user for a valid seat number
            System.out.println("Invalid seat number for row " + rowNumber +
                    ". Please select a seat number between 1 and " + (rowNumber.equalsIgnoreCase("A") || rowNumber.equalsIgnoreCase("D") ? 14 : 12));
            System.out.print("Enter Seat Number: ");
            // Recursive call to get a valid seat number
            return GetSeat(rowNumber);
        }
        // Return the valid seat number
        return seatNumber;
    }

    // Checks if the input seat number is valid for the given row.
    private boolean isValidSeat(String rowNumber, int seatNumber) {
        return (rowNumber.equalsIgnoreCase("A") || rowNumber.equalsIgnoreCase("D")) && seatNumber >= 1 && seatNumber <= 14 ||
                (rowNumber.equalsIgnoreCase("B") || rowNumber.equalsIgnoreCase("C")) && seatNumber >= 1 && seatNumber <= 12;
    }

    //Allows the user to cancel a seat reservation, deleting the ticket information file.
    private void cancel_seat(Scanner scanner) {
        while (true) {
            System.out.print("Enter row Letter (A,B,C,D): ");
            String row = scanner.next().toUpperCase(); // Convert row input to uppercase

            if (!isValidRow(row)) {
                System.out.println("Invalid row number. Please enter between A,B,C,D.");
                continue; // Repeat the loop to ask for row number again
            }

            System.out.print("Enter Seat Number: ");
            int seat = GetSeat(row);

            if (!isValidSeat(row, seat)) {
                System.out.println("Invalid seat number for row " + row +
                        ". Please select a seat number between 1 and " + (row.equalsIgnoreCase("A") || row.equalsIgnoreCase("D") ? 14 : 12));
                continue; // Repeat the loop to ask for seat number again
            }

            int rowIndex = row.charAt(0) - 'A';  // Convert row letter to index
            int seatIndex = seat - 1;  // Convert seat number to index

            if (seats[rowIndex][seatIndex] == 1) {
                seats[rowIndex][seatIndex] = 0;
                System.out.println("Seat " + row + seat + " has been canceled.");

                soldTickets[rowIndex][seatIndex].deleteFile();

                soldTickets[rowIndex][seatIndex] = null;
            } else {
                System.out.println("Seat " + row + seat + " is already available.");
            }

            break; // Exit the loop if the seat is successfully canceled
        }
    }

    //Finds and prints the first available seat.
    private static void find_first_available(Scanner scanner) {
        boolean found = false;
        char[] rows = {'A', 'B', 'C', 'D'};

        // Loop through each row and seat in the seating plan
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                // Check if the seat is available (0 represents an available seat)
                if (seats[i][j] == 0) {
                    System.out.println("The first available seat is: Row " + rows[i] + ", Seat " + (j + 1));
                    found = true;
                    break;// Exit the inner loop after found the first available seat
                }
            }
            if (found) {
                break;// Exit the outer loop after found the first available seat
            }
        }


        if (!found) {
            System.out.println("No available seats found.");
        }
    }

    //Displays the current seating plan, showing 'X' for booked seats and 'O' for available seats.
    private static void display_seat(Scanner scanner) {
        // Loop through each row and seat in the seating plan
        for (int i = 0; i < seats.length; i++) {
            System.out.println("");
            for (int j = 0; j < seats[i].length; j++) {
                // Check if the seat is booked (1 represents a booked seat) and print 'X', otherwise print 'O'
                if(seats[i][j] == 1){
                    System.out.print("X ");
                }else {
                    System.out.print("O ");
                }
            }
        }
    }

    //Prints information about all sold tickets, including total sales.
    public void print_tickets_info() {
        double totalPrice = 0.0;
        System.out.println("Tickets Information:");
        // Loop through each row and seat in the soldTickets array
        for (int i = 0; i < soldTickets.length; i++) {
            for (int j = 0; j < soldTickets[i].length; j++) {
                Ticket ticket = soldTickets[i][j]; // Get the ticket object at the current position
                if (ticket != null) { // Check if a ticket exists at this position
                    String row = ticket.getRow(); //get inputs from ticket class
                    int seat = ticket.getSeat();
                    double price = ticket.getPrice();
                    totalPrice += price;
                    // Print ticket details including row, seat, and price
                    System.out.println("Row: " + row + ", Seat: " + seat + ", Price: " + price);
                }
            }
        }
        System.out.println("Total Sales: £" + totalPrice);
    }

    //Give user a search option for get the details of the ticket
    private void search_ticket(Scanner scanner) {
        while (true) {
            System.out.print("Enter ROW Letter (A,B,C,D): ");
            String rowNumber = scanner.next().toUpperCase(); // Convert input to uppercase for consistency

            //checking inputs are correct
            if (!isValidRow(rowNumber)) {
                System.out.println("Invalid row number. Please select between (A,B,C,D).");
                continue; // Repeat the loop to ask for row number again
            }

            System.out.print("Enter a SEAT Number: ");
            int seatNumber = GetSeat(rowNumber);

            //validating  seat number
            if (!isValidSeat(rowNumber, seatNumber)) {
                System.out.println("Invalid seat number for row " + rowNumber +
                        ". Please select a seat number between 1 and " + (rowNumber.equalsIgnoreCase("A") || rowNumber.equalsIgnoreCase("D") ? 14 : 12));
                continue; // Repeat the loop to ask for seat number again
            }

            //calculate the array indices for the seats
            int rowIndex = Character.toUpperCase(rowNumber.charAt(0)) - 'A';
            int seatIndex = seatNumber - 1;

            if (soldTickets[rowIndex][seatIndex] == null){
                System.out.println("Ticket not found.");
            }else{
                soldTickets[rowIndex][seatIndex].printInfo();
            }

                // Exit the loop if the seat is successfully booked
                break;
        }
    }

    public static void main(String[] args) {
        PlaneManagement planeManagement = new PlaneManagement();
        // Repeat displaying the menu, getting user choice, and performing actions until the user chooses to quit
        do {
            planeManagement.printMenu();
            planeManagement.choice = planeManagement.getChoice();
            planeManagement.performAction(planeManagement.choice);
        } while (planeManagement.choice != 0);
        planeManagement.scanner.close();
    }
}
