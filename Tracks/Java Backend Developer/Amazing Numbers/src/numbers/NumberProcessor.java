package numbers;

import java.util.Arrays;

public final class NumberProcessor {
    public static void processOneNumber(long number) {
        if (!(number < 0)) {
            System.out.printf("Properties of %,d%n", number);
            System.out.println("        buzz: " + AmazingNumbers.checkBuzz(number));
            System.out.println("        duck: " + AmazingNumbers.checkDuck(number));
            System.out.println(" palindromic: " + AmazingNumbers.checkPalindrome(number));
            System.out.println("      gapful: " + AmazingNumbers.checkGapful(number));
            System.out.println("         spy: " + AmazingNumbers.checkSpy(number));
            System.out.println("      square: " + AmazingNumbers.checkSquare(number));
            System.out.println("       sunny: " + AmazingNumbers.checkSquare(number+1));
            System.out.println("     jumping: " + AmazingNumbers.checkJumping(number));
            System.out.println("       happy: " + AmazingNumbers.checkHappy(number));
            System.out.println("         sad: " + !AmazingNumbers.checkHappy(number));
            System.out.println("        even: " + AmazingNumbers.checkParity(number));
            System.out.println("         odd: " + !AmazingNumbers.checkParity(number));
        } else {
            System.out.println("The first parameter should be a natural number or zero.");
        }
    }

    public static void processConsecutiveList(long startingNumber, long consecutiveNumber) { // for two numbers
        if (ErrorHandler.checkNumbersErrors(startingNumber, consecutiveNumber)) return; // return if there are any errors
        for (long i = startingNumber; i < startingNumber + consecutiveNumber; i++) {
            StringBuilder numberProperties = checkNumberProperties(i);
            System.out.printf("\t\t\t %,1d is %s%n",i, numberProperties.substring(0, numberProperties.length()-2));
        }
    }

    public static void processQuery(long startingNumber, long howManyNumbers, String[] searchQuery) { // for parameters and a property
        String[] searchParametersArray = Arrays.copyOfRange(searchQuery, 2, searchQuery.length);
        if (ErrorHandler.checkNumbersErrors(startingNumber, howManyNumbers)) return; // return if there are any errors
        if (ErrorHandler.checkProperties(searchParametersArray)) return; // return if there are any error within properties
        printQuery(startingNumber, howManyNumbers, searchParametersArray);
    }

    private static void printQuery(long startingNumber, long howManyNumbers, String[] searchParametersArray) {
        int x = 0;
          while (x < howManyNumbers) {
            StringBuilder numberProperties = checkNumberProperties(startingNumber);

            if (checkParameter(searchParametersArray, numberProperties)) { // there is a property / properties in the number
                System.out.printf("\t\t\t %,1d is %s%n",startingNumber, numberProperties.substring(0, numberProperties.length()-2));
                x++;
            }
            startingNumber++;
        }
    }

    private static boolean checkParameter(String[] searchParameter, StringBuilder numberProperties) { // checks if searched property is available in numberProperties
        boolean flag = true;
        for (String property : searchParameter) {
            if(!property.startsWith("-")) {
                if (!(numberProperties.toString().contains(property.toLowerCase()))) {
                    flag = false;
                    break;
                }
            } else {
                if (numberProperties.toString().contains(property.replace("-","").toLowerCase())) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    private static StringBuilder checkNumberProperties(long i) {
        StringBuilder numberProperties = new StringBuilder();
        if (AmazingNumbers.checkBuzz(i)) numberProperties.append("buzz, ");
        if (AmazingNumbers.checkDuck(i)) numberProperties.append("duck, ");
        if (AmazingNumbers.checkPalindrome(i)) numberProperties.append("palindromic, ");
        if (AmazingNumbers.checkGapful(i)) numberProperties.append("gapful, ");
        if (AmazingNumbers.checkSpy(i)) numberProperties.append("spy, ");
        if (AmazingNumbers.checkSquare(i)) numberProperties.append("square, ");
        if (AmazingNumbers.checkJumping(i)) numberProperties.append("jumping, ");
        if (AmazingNumbers.checkHappy(i)) numberProperties.append("happy, ");
        if (!AmazingNumbers.checkHappy(i)) numberProperties.append("sad, ");
        if (AmazingNumbers.checkSquare(i+1)) numberProperties.append("sunny, ");
        if (AmazingNumbers.checkParity(i)) numberProperties.append("even, ");
        else numberProperties.append("odd, ");
        return numberProperties;
    }
}
