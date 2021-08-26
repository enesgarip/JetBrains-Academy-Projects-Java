package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static int bullCount = 0;
    static int cowCount = 0;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter the secret code's length:");
        String string = scanner.nextLine();
        validateNumber(string);

        int input = Integer.parseInt(string);
        validateInput(input);
        System.out.println("Input the number of possible symbols in the code:");
        int symbols = scanner.nextInt();
        validateSymbol(input, symbols);
        String secretCode = generateSecretCode(input,symbols);
        preparedSentence(secretCode, symbols);
        System.out.println(secretCode);
        System.out.println("Okay, let's start a game!");
        int turn = 1;
        grader(secretCode,turn);

    }
    private static void validateNumber(String string){
        for (int i = 0; i < string.length(); i++) {
            if (Character.isAlphabetic(string.charAt(i))){
                System.out.println("Error: " +"\""+string+"\" isn't a valid number.");
                System.exit(1);
            }
        }
    }
    private static void validateSymbol(int input, int symbols) {
        if(symbols < input){
            System.out.println("Error: it's not possible to generate a code with a length of "+input+ " with "+symbols+ " unique symbols.");
            System.exit(1);
        }
        if (symbols > 36){
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            System.exit(1);
        }
    }

    private static void preparedSentence(String secretCode, int symbols) {
        String numbers = "0123456789";
        String dictionary = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder rangeOfCode = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < secretCode.length(); i++) {
            stringBuilder.append("*");
        }
        if (symbols > 10){
            rangeOfCode.append("(0-9, ");
            rangeOfCode.append(dictionary.charAt(0));
            if (symbols - 11 != 0) {
                rangeOfCode.append("-");
                rangeOfCode.append(dictionary.charAt(symbols-11));
            }
            rangeOfCode.append(")");
        }else {
            rangeOfCode.append("(0-");
            rangeOfCode.append(numbers.charAt(symbols-1));
            rangeOfCode.append(")");
        }
        System.out.println("The secret is prepared: "+stringBuilder +" "+rangeOfCode);
    }

    private static String generateSecretCode(int input, int symbols) {
        String dictionary = "0123456789abcdefghijklmnopqrstuwxyz";
        StringBuilder secretCode = new StringBuilder();
        Random random = new Random();
        String range = dictionary.substring(0,symbols-1);
        while (true){
            int index = (int) (random.nextFloat() * range.length());
            if (secretCode.indexOf(String.valueOf(range.charAt(index)))>-1){
                continue;
            }else {
                secretCode.append(range.charAt(index));
            }
            if (secretCode.length() == input){
                break;
            }
        }
        return secretCode.toString();
    }

    private static void validateInput(int input) {
        if(input >35){
            System.out.println("Error: can't generate a secret number with a length of 11 because there aren't enough unique digits.");
            System.exit(1);
        }
        if (input <= 0){
            System.out.println("Error:");
            System.exit(1);
        }
    }

    private static void grader(String secretCode, int turn) {
        System.out.println("Turn " + turn + ":");
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true){
            input=scanner.nextLine();
            turn++;
            checker(input, secretCode);
            System.out.println("Turn " + turn + ":");
        }
    }
    private static void checker(String input, String secretCode){
        bullCount = 0;
        cowCount = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == secretCode.charAt(i)) {
                bullCount++;
            } else if (secretCode.contains(input.charAt(i) + "")) {
                cowCount++;
            }
        }
        if(bullCount == 1 && cowCount == 1){
            System.out.println("Grade: "+bullCount+" bull and "+cowCount+" cow");
        }else if(bullCount == secretCode.length()){
            System.out.println("Grade: "+bullCount+" bulls");
            System.out.println("Congratulations! You guessed the secret code.");
            System.exit(0);
        }else if(bullCount > 1 && cowCount == 1){
            System.out.println("Grade: "+bullCount+" bulls and "+cowCount+" cow");
        }else if(bullCount == 1 && cowCount > 1){
            System.out.println("Grade: "+bullCount+" bull and "+cowCount+" cows");
        }else if(bullCount > 1 && cowCount > 1){
            System.out.println("Grade: "+bullCount+" bulls and "+cowCount+" cows");
        }else if(bullCount < 1 && cowCount < 1){
            System.out.println("Grade: None.");
        }else if(bullCount > 1 && cowCount == 0){
            System.out.println("Grade: "+bullCount+" bulls");
        }else if(bullCount == 0 && cowCount > 1){
            System.out.println("Grade: "+cowCount+" cows");
        }
    }
}
