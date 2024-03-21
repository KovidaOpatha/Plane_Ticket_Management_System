import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Ticket {
    private String row;
    private int seat;
    private double price;
    private Person person;

    public Ticket(String row, int seat, double price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
        save(); // Call save method when a ticket is created
    }

    public void save() {
        String fileName = row + seat + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Row: " + row);
            writer.println("Seat: " + seat);
            writer.println("Price: " + price);
            writer.println("Person Information:");
            writer.println("Name: " + person.getName());
            writer.println("Surname: " + person.getSurname());
            writer.println("Email: " + person.getEmail());
            //System.out.println("Ticket information saved to " + fileName); //This line is here to debug save method
        } catch (IOException e) {
            //System.out.println("Failed to save ticket information."); //This line is here to debug save method
            e.printStackTrace();
        }
    }

    public void deleteFile() {
        String fileName = row + seat + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
//            System.out.println("Ticket information deleted from " + fileName); //This line is here to debug delete file method
        } else {
            System.out.println("Ticket file not found.");
        }
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    public void printInfo() {
        System.out.println("Row: " + row);
        System.out.println("Seat: " + seat);
        System.out.println("Price: " + price);
        System.out.println("Person Information:");
        person.printInfo();
    }
}
