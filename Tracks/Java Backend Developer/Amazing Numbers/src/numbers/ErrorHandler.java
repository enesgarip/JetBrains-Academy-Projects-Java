package numbers;

import java.util.Arrays;

public final class ErrorHandler {

    static final String VALID_PROPERTIES = "even odd buzz duck spy palindromic gapful square sunny jumping happy sad -even -odd -buzz -duck -spy -palindromic -gapful -square -sunny -jumping -happy -sad";

    public static boolean checkNumbersErrors(long number1, long number2) { // return true if there are any errors
        boolean flag = false;
        if (number1 < 0) {
            System.out.println("The first parameter should be a natural number or zero.");
            flag = true;
        }
        if(number2 < 1) {
            System.out.println("The second parameter should be a natural number");
            flag = true;
        }
        return flag;
    }

    public static boolean checkProperties(String[] searchProperties) {
        String[] invalidProperties = new String[searchProperties.length];
        int numberOfInvalidProperties = getNumberOfInvalidProperties(searchProperties, invalidProperties);

        if (numberOfInvalidProperties == 1) {
            System.out.println("The property [" + invalidProperties[0] + "] is wrong.");
            printAvailableProperties();
            return true;
        } else if (numberOfInvalidProperties > 1) {
            System.out.println("The properties " + Arrays.toString(invalidProperties) + " are wrong.");
            printAvailableProperties();
            return true;
        }

        if (searchProperties.length > 1) { return areMutuallyExclusive(searchProperties); }

        return false;
    }

    private static int getNumberOfInvalidProperties(String[] searchProperties, String[] invalidProperties) {
        int numberOfInvalidProperties = 0;
        for (String searchProperty : searchProperties) {
            if (!(isParameterValid(searchProperty))) {
                invalidProperties[numberOfInvalidProperties] = searchProperty.toUpperCase();
                numberOfInvalidProperties++;
            }
        }
        return numberOfInvalidProperties;
    }

    private static void printAvailableProperties() {
        System.out.println("Available properties: [EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]");
    }

    private static boolean areMutuallyExclusive (String[] searchProperties) {
        if (isContain(searchProperties,"EVEN") && isContain(searchProperties,"ODD") || (isContain(searchProperties,"-EVEN") && isContain(searchProperties,"-ODD"))) {
            System.out.println("The request contains mutually exclusive properties: [EVEN, ODD]");
            System.out.println("There are no numbers with these properties.");
            return true;
        }
        if (isContain(searchProperties,"SUNNY") && isContain(searchProperties,"SQUARE") ||  (isContain(searchProperties,"-SUNNY") && isContain(searchProperties,"-SQUARE"))){
            System.out.println("The request contains mutually exclusive properties: [SUNNY, SQUARE]");
            System.out.println("There are no numbers with these properties.");
            return true;
        }
        if (isContain(searchProperties,"DUCK") && isContain(searchProperties,"SPY") || (isContain(searchProperties,"-DUCK") && isContain(searchProperties,"-SPY"))){
            System.out.println("The request contains mutually exclusive properties: [DUCK, SPY]");
            System.out.println("There are no numbers with these properties.");
            return true;
        }
        if (isContain(searchProperties,"HAPPY") && isContain(searchProperties,"SAD") || (isContain(searchProperties,"-HAPPY") && isContain(searchProperties,"-SAD"))) {
            System.out.println("The request contains mutually exclusive properties: [HAPPY, SAD]");
            System.out.println("There are no numbers with these properties.");
            return true;
        }

        return findOppositeProperties(searchProperties);
    }

    private static boolean findOppositeProperties(String[] searchProperties) {
        String[] oppositeProperties = new String[2];
        int k = 0;

        for (String i : searchProperties) {
            for (String j : searchProperties) {
                if (i.equalsIgnoreCase(oppositePair(j))) {
                    oppositeProperties[k++] = i;
                    break;
                }
            }
        }

        if (k == 2) {
            System.out.println("The request contains mutually exclusive properties: " + Arrays.toString(oppositeProperties).toUpperCase());
            System.out.println("There are no numbers with these properties.");
            return true;
        }

        return false;
    }

    private static boolean isParameterValid(String searchParameter) { // checks if searched property is valid, does it exist
        return VALID_PROPERTIES.contains(searchParameter) || VALID_PROPERTIES.toUpperCase().contains(searchParameter);
    }

    private static boolean isContain(String[] source, String subItem) {
        boolean hasTerm = false;
        for (String word : source) {
            if (word.toLowerCase().equals(subItem.toLowerCase())) {
                hasTerm = true;
                break;
            }
        }
        return hasTerm;
    }

    private static String oppositePair (String property) {
        if(property.startsWith("-"))
            return property.replace("-","");

        return "-" + property;
    }
}
