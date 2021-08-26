package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    static Banking banking;

    public static void main(String[] args) {
        //check if command line supplied
        banking = new Banking(args[1]); //set up database
        //create table
        try {
            banking.processDatabaseConnection(); //connect to the database
            banking.createTable(); //create table if it exists
        } catch (SQLException throwables) {
            System.out.println("DATABASE CONNECTION ERROR :" + throwables.getMessage());
            throwables.printStackTrace();
        }

        boolean isConnected = true;
        while (isConnected) {
            int command = displayWelcome();
            switch (command) {
                case 1:
                    processCreateAccount();
                    break;
                case 2:
                    isConnected = processLoginIntoAccount();
                    break;
                case 0:
                    isConnected = false;
                    break;
                default:
                    System.out.println();
                    System.out.println("Unknown action, please try again.");
                    break;
            }
        }
        processExit();
    }

    private static int displayWelcome() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit\n");
        int action;
        try {
            action = scanner.nextInt();
        } catch (NumberFormatException e) {
            action = -1;
        }
        return action;
    }

    private static void processCreateAccount() {
        try {
            Account account = banking.InsertAccount();

            System.out.println("Your card has been created");
            System.out.println("Your card number:\n" + account.getCard().getCardNumber());
            System.out.println("Your card PIN:\n" + account.getCard().getPin());
            System.out.println();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean diplayLoggedMenu(Account account) throws SQLException {
        boolean loggedIn = true;
        int command;
        while (loggedIn && account != null) {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            try {
                command = scanner.nextInt();
            } catch (NumberFormatException e) {
                command = -1;
            }

            switch (command) {
                case 0:
                    // Exiting system
                    return false;
                case 1:
                    // Print balance
                    System.out.println();
                    try {
                        System.out.println("Balance: " + banking.getAccount(account.getCard().getCardNumber()).getBalance());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    System.out.println();
                    break;
                case 2:
                    loggedIn = true;
                    System.out.println("Enter income:");
                    int income = scanner.nextInt();
                    try {
                        if (banking.deposit(account, income)) {
                            System.out.println("Income was added!\n");
                        } else {
                            System.out.println("Income not added!");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case 3:
                    loggedIn = true;
                    System.out.println("Transfer\n Enter card number :");
                    String number = scanner.next();//read card numbrt
                    //do validation of card
                    if (!Banking.checkCard(number)) { //check if card is valid
                        System.out.println("Probably you made mistake in the card number. Please try again!");
                    } else if (banking.getAccount(number) == null) { //check if card does not exist
                        System.out.println("Such a card does not exist.");
                    } else {
                        System.out.println("Enter how much money you want to transfer:");
                        int amount = scanner.nextInt();
                        //check if the balance is enough
                        Account myAcount = banking.getAccount(account.getCard().getCardNumber());
                        int balance = myAcount.getBalance();
                        if (amount > balance) {
                            System.out.println("Not enough money!\n");
                        } else {
                            //can do better with transactions
                            banking.deposit(myAcount, -amount);//withdraw amount
                            banking.deposit(banking.getAccount(number), amount);//deposit
                            System.out.println("Success!\n");
                        }
                    }
                    break;
                case 4: //closing the banking account
                    loggedIn = false;
                    if (banking.closeAccount(account.getCard().getCardNumber())) {
                        System.out.println("The account has been closed!\n");
                    }
                    break;
                case 5: //closing the banking account
                    loggedIn = false;
                    System.out.println("You have successfully logged out!n");
                    banking.closeConnection();
                    break;
                default:
                    System.out.println("\nUnknown action please try again!\n");
                    break;
            }
        }
        return true;
    }

    private static boolean processLoginIntoAccount() {
        boolean isConnected = false;
        System.out.println();
        System.out.println("Enter your card number:");
        String accountNumber = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();
        try {
            Account account = banking.login(pin, accountNumber);
            if (account != null) {
                System.out.println();
                System.out.println("You have successfully logged in!");
                System.out.println();
                isConnected = diplayLoggedMenu(account);
            } else {
                System.out.println("\nWrong card number or PIN!\n");
                isConnected = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return isConnected;

    }

    private static void processExit() {
        System.out.println("Bye!");
    }
}

class Account {
    private Card card;
    private int balance;

    Account(Card card, int balance) {
        this.card = card;
        this.balance = balance;
    }

    public Card getCard() {
        return card;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int income) {
        balance = balance + income;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getBalance() == account.getBalance() &&
                Objects.equals(getCard(), account.getCard());
    }

    @Override
    public String toString() {
        return "Account{" +
                "card=" + card +
                ", balance=" + balance +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCard(), getBalance());
    }
}

class Card {
    private String pin;
    private final String cardNumber;
    private int id;

    public Card(String pin, String cardNumber) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public Card(String pin, String cardNumber, int id) {
        this(pin, cardNumber);
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(getPin(), card.getPin()) &&
                Objects.equals(getCardNumber(), card.getCardNumber());
    }

    @Override
    public String toString() {
        return "Card{" +
                "pin='" + pin + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPin(), getCardNumber());
    }
}

class Banking {
    private String sqlDbName;

    public Banking(String dbfile) {
        // System.out.println("Database Name : "+dbfile);
        this.sqlDbName = dbfile;
    }

    public Connection processDatabaseConnection() throws SQLException {
        String url = "jdbc:sqlite:" + sqlDbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        return dataSource.getConnection();
    }

    public boolean createTable() {
        boolean isCreated = false;
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	balance INTEGER DEFAULT 0,\n"
                + "	pin TEXT,\n"
                + "	number TEXT,\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT \n"
                + ");";
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = processDatabaseConnection();
            if (connection != null) {
                stmt = connection.createStatement();
                // create a new table
                isCreated = stmt.execute(sql);
            }
            closeDBResources(connection, stmt, null);
        } catch (SQLException e) {
            System.out.println("CREATE TABLE Error : " + e.getMessage());
        } finally {
            closeDBResources(connection, stmt, null);
        }
        return isCreated;
    }

    public Account InsertAccount() throws SQLException {
        String cardNum = createCardNumber();
        Card card = new Card(createPIN(), cardNum);
        Account account = new Account(card, 0);


        String sql = "INSERT INTO card(number, pin,balance) VALUES(?,?,0)";
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = processDatabaseConnection();
            if (connection != null) {
                stmt = connection.prepareStatement(sql);
                // create a new table
                stmt.setString(1, account.getCard().getCardNumber());
                stmt.setString(2, account.getCard().getPin());
                int status = stmt.executeUpdate();
                if (!(status > 0)) {
                    //not inserted
                    account = null;
                }
                closeDBResources(connection, stmt, null);
            }
        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
            throw e;
        } finally {
            closeDBResources(connection, stmt, null);
        }
        return account;
    }

    public Account login(String pin, String number) throws SQLException {
        Account account = null;

        String sql = "SELECT id,number,pin, balance FROM card WHERE pin =? and number = ?;";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = processDatabaseConnection();
            if (connection != null) {
                stmt = connection.prepareStatement(sql);
                // create a new table
                stmt.setString(1, pin);
                stmt.setString(2, number);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    //not inserted
                    Card card = new Card(rs.getString("pin"), rs.getString("number"), rs.getInt("id"));

                    account = new Account(card, rs.getInt("balance"));
                }
                closeDBResources(connection, stmt, rs);
            }
        } catch (SQLException e) {
            System.out.println("LOGIN Error : " + e.getMessage());
            throw e;
        } finally {
            closeDBResources(connection, stmt, rs);
        }
        return account;
    }

    public void closeDBResources(Connection con, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                //stmt.close();
            }
            if (con != null || !con.isClosed()) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB Closing error. " + e.getMessage());
        }
    }

    public static String createPIN() {
        int n = 4;
        int m = (int) Math.pow(10, n - 1);
        int pin = m + new Random().nextInt(9 * m);

        return "" + pin;

    }

    public static String createCardNumber() {
        // The number of random digits that we need to generate is equal to the
        // total length of the card number minus the start digits given by the
        // user, minus the check digit at the end.
        // from https://gist.github.com/josefeg/57818241
        Random random = new Random(System.currentTimeMillis());
        String bin = "400000"; //
        int length = 16;
        int randomNumberLength = length - (bin.length() + 1);

        StringBuilder builder = new StringBuilder(bin);
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        // Do the Luhn algorithm to generate the check digit.
        int checkDigit = generateCheckDigit(builder.toString());
        builder.append(checkDigit);

        return builder.toString();
    }

    public static boolean checkCard(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    static int generateCheckDigit(String number) {
        // Get the sum of all the digits, however we need to replace the value
        // of the first digit, and every other digit, with the same digit
        // multiplied by 2. If this multiplication yields a number greater
        // than 9, then add the two digits together to get a single digit
        // number.
        //
        // The digits we need to replace will be those in an even position for
        // card numbers whose length is an even number, or those is an odd
        // position for card numbers whose length is an odd number. This is
        // because the Luhn algorithm reverses the card number, and doubles
        // every other number starting from the second number from the last
        // position.
        // https://gist.github.com/josefeg/5781824
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {

            // Get the digit at the current position.
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        // The check digit is the number required to make the sum a multiple of
        // 10.
        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }

    public boolean deposit(Account account, int income) throws SQLException {
        String sql = "UPDATE card SET balance =balance + ? WHERE number = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        boolean isUpdated = false;
        try {
            connection = processDatabaseConnection();
            if (connection != null) {
                stmt = connection.prepareStatement(sql);
                // create a new table
                stmt.setInt(1, income);
                stmt.setString(2, account.getCard().getCardNumber());
                int status = stmt.executeUpdate();
                if (status > 0) {
                    //not inserted
                    isUpdated = true;
                }
                closeDBResources(connection, stmt, null);
            }
        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
            throw e;
        } finally {
            closeDBResources(connection, stmt, null);
        }
        return isUpdated;
    }

    public boolean closeAccount(String number) throws SQLException {
        String sql = "DELETE FROM card WHERE number = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        boolean isUpdated = false;
        try {
            connection = processDatabaseConnection();
            if (connection != null) {
                stmt = connection.prepareStatement(sql);
                // create a new table
                stmt.setString(1, number);
                int status = stmt.executeUpdate();
                if (status > 0) {
                    //not inserted
                    isUpdated = true;
                }
                closeDBResources(connection, stmt, null);
            }
        } catch (SQLException e) {
            System.out.println("CLOSE ACCOUNT Error" + e.getMessage());
            throw e;
        } finally {
            closeDBResources(connection, stmt, null);
        }
        return isUpdated;
    }

    public void closeConnection() {
        Connection con;
        try {
            con= processDatabaseConnection();
            if (con != null || !con.isClosed()) {
                con.close();
                con = null;
            } else {
                con = null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            con =null;
        }
    }

    public Account getAccount(String number) throws SQLException {
        String sql = "SELECT id, number, pin, balance FROM card WHERE number = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs;
        Account account = null;
        try {
            connection = processDatabaseConnection();
            if (connection != null) {
                stmt = connection.prepareStatement(sql);
                // create a new table
                stmt.setString(1, number);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    //not inserted
                    account = new Account(new Card(rs.getString(3), rs.getString(2), rs.getInt("id")), rs.getInt(4));
                }
                closeDBResources(connection, stmt, null);
            }
        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
            throw e;
        } finally {
            closeDBResources(connection, stmt, null);
        }
        return account;
    }
}
