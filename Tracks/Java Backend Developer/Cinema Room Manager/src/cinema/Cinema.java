package cinema;

import java.util.Objects;
import java.util.Scanner;

public class Cinema {
    static int totalIncome=0;
    static int possibleIncome=0;
    public static void main(String[] args) {
        // Write your code here
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the number of rows:");
        int rows = input.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int seats = input.nextInt();
        setPossibleIncome(rows,seats);
        String[][] cinema = new String[rows][seats];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                cinema[i][j]="S";
            }
        }
        while (true){
            System.out.println("\n1. Show the seats\n" +
                    "2. Buy a ticket\n" +
                    "3. Statistics\n" +
                    "0. Exit");
            int choice = input.nextInt();
            switch (choice){
                case 1:
                    printCinema(rows,seats,cinema);
                    break;
                case 2:
                    buyTicket(input,rows,seats,cinema);
                    break;
                case 3:
                    statistics(cinema,rows,seats);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid Choice!");
                    break;
            }
        }
    }
    public static void printCinema(int rows, int seats, String[][] cinema){
        System.out.println("\nCinema:");
        System.out.print(" ");
        for (int i = 0; i < seats; i++) {
            System.out.print(" "+(i + 1));
        }
        System.out.println();
        for (int i = 0; i < rows; i++) {
            System.out.print(i+1);
            for (int j = 0; j < seats; j++) {
                System.out.print(" "+cinema[i][j]);
            }
            System.out.println();
        }
    }
    public static int ticketPrice(int rows, int seats, int rowChoice, int seatChoice){
        int price;
        int totalSeats=rows * seats;
        if (totalSeats <= 60){
            price = 10;
//            possibleIncome = totalSeats*10;
        }
        else {
            int frontHalf=rows/2;
//            int backHalf=rows-frontHalf;
//            possibleIncome += frontHalf*seats*8;
//            possibleIncome += backHalf*seats*10;
            if (rowChoice > frontHalf){
                price = 8;
            }
            else price = 10;
        }
        return price;
    }
    public static void buyTicket(Scanner input, int rows, int seats, String[][] cinema){
        while (true) {
            System.out.println("Enter a row number:");
            int rowChoice = input.nextInt();
            System.out.println("Enter a seat number in that row:");
            int seatChoice = input.nextInt();
            if (rowChoice > rows || seatChoice > seats){
                System.out.println("Wrong input!");
            }
            else if (!"B".equals(cinema[rowChoice - 1][seatChoice - 1])) {
                cinema[rowChoice - 1][seatChoice - 1] = "B";
                int price = ticketPrice(rows, seats, rowChoice, seatChoice);
                System.out.println("Ticket price: $" + price);
                totalIncome += price;
                break;
            } else {
                System.out.println("That ticket has already been purchased!");
            }
        }
    }
    public static void statistics(String[][] cinema, int rows, int seats){
        int soldTicket=0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                if (cinema[i][j].equals("B")){
                    soldTicket+=1;
                }
            }
        }
        double percentage = ((double) soldTicket / (rows*seats))*100;
        System.out.println("Number of purchased tickets: "+soldTicket);
        System.out.println("Percentage: "+ String.format("%.2f",percentage) + "%");
        System.out.println("Current income: $" + totalIncome);
        System.out.println("Total income: $" + possibleIncome);
    }
    public static void setPossibleIncome(int rows, int seats){
        int totalSeats=rows * seats;
        if (totalSeats <= 60){
            possibleIncome = totalSeats * 10;
        }
        else {
            int frontHalf=rows/2 + 1;
            int backHalf=rows-frontHalf;
            possibleIncome += frontHalf*seats*8;
            possibleIncome += backHalf*seats*10;
        }
    }
}