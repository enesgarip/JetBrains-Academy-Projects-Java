package numbers;

import java.util.ArrayList;

public final class AmazingNumbers {

    public static boolean checkHappy(long number) {

        ArrayList<Long> sequence = new ArrayList<>();
        sequence.add(number);

        if (number != 1) {
            for (int i = 0; i < sequence.size(); i++) {
                long sum = 0;
                if (sequence.get(i) == 1) { return true; }

                long check = sequence.get(i);
                while (check != 0) {
                    sum += Math.pow(check % 10, 2);
                    check /= 10;
                }

                if (sum == 145 || sum == 3 || sum == 4 || sum == 5 || sum == 6) { return false; }

                sequence.add(sum);
            }
        }
        return true;
    }

    public static boolean checkJumping (long number) {
        ArrayList<Long> digits = new ArrayList<>();

        while (number != 0) {
            digits.add(number % 10);
            number /= 10;
        }

        return isFlagJumping(digits);
    }

    private static boolean isFlagJumping(ArrayList<Long> digits) {
        boolean flag = true;
        for (int i = 0; i < digits.size() - 1; i++) {
            if (Math.abs(digits.get(i) - digits.get(i + 1)) != 1) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean checkGapful (long number) {
        if (!(number < 100)) {
            String numberToString = Long.toString(number);
            String firstLastDigit = numberToString.charAt(0) + String.valueOf(numberToString.charAt(numberToString.length()-1));
            long divisor = Long.parseLong(firstLastDigit);
            return number % divisor == 0;
        }
        return false;
    }

    public static boolean checkPalindrome(long number) {
        long reversedNumber = 0;
        long check = number;
        while(check != 0) {
            long remainder = check % 10;
            reversedNumber = reversedNumber * 10 + remainder;
            check /= 10;
        }
        return reversedNumber == number;
    }

    public static boolean checkSpy (long number) { // if the sum of all digits is equal to the product of all digits
        long sum = 0;
        long product = 1;
        while (number != 0) {
            long digit = number % 10;
            sum += digit;
            product *= digit;
            number /= 10;
        }
        return sum == product;
    }

    public static boolean checkSquare (long number) { return number == ((long) Math.sqrt(number) * Math.sqrt(number)); }
    public static boolean checkParity(long number) { return number % 2 == 0; }
    public static boolean checkBuzz(long number) { return ((number % 10) == 7) || number % 7 == 0; }
    public static boolean checkDuck(long number) { return Long.toString(number).substring(1).contains("0"); }
}
