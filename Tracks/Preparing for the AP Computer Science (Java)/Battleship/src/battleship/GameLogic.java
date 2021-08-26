package battleship;

import java.util.*;
import static battleship.CellState.*;

public class GameLogic {
    final static Scanner scanner = new Scanner(System.in);

    private ArrayList<String> makeCoordinatesList() {
        ArrayList<String> coordinates = new ArrayList<>();

        for (char start = 'A'; start <= 'J'; start++) {
            for (int i = 1; i < 11; i++) {
                coordinates.add(String.format("%c%d", start, i));
            }
        }
        return coordinates;
    }

    public void play(Player firstPlayer, Player secondPlayer) {
        arrangeShips(firstPlayer, 1);
        arrangeShips(secondPlayer, 2);
        while (!firstPlayer.isEndOfGame() && !secondPlayer.isEndOfGame()) {
            makeMove(firstPlayer, secondPlayer, 1);
            makeMove(secondPlayer, firstPlayer, 2);
        }
    }

    private void passMove() {
        System.out.println("Press Enter and pass the move to another player\n. . .");
        scanner.nextLine();
    }

    private void arrangeShips(Player player, int number) {
        System.out.printf("Player %d, place your ships on the game field%n", number);
        player.printField();
        for (Ship ship : Ship.values()) {
            System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship.getShipName(), ship.getSells());
            player.placeShip(getCoordinates(player, ship.getShipName(), ship.getSells()));
        }
        passMove();
    }

    private void makeMove(Player player, Player enemy, int number) {
        player.printEmptyField();
        System.out.println("---------------------");
        player.printField();
        System.out.printf("Player %d, it's your turn:%n", number);
        shooting(player, enemy, setShot());
        passMove();
    }

    private void shooting(Player player, Player enemy, int[] shot) {
        int row = shot[0];
        int column = shot[1];

        if (enemy.getOwnCell(row, column).equals(FILL.getState())) {
            System.out.println("You hit a ship!");
            enemy.changeOwnCell(row, column, HIT);
            player.changeEnemyCell(row, column, HIT);
            if (enemy.isEndOfGame()) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                return;
            }
            if (enemy.checkSurroundings(row, column, row, column)) {
                System.out.println("You sank a ship! Specify a new target:");
            }
        } else if (enemy.getOwnCell(row, column).equals(EMPTY.getState())) {
            System.out.println("You missed!");
            enemy.changeOwnCell(row, column, MISS);
            player.changeEnemyCell(row, column, MISS);
        } else {
            System.out.println("You have already shot at this target! Specify a new target:");
        }
    }

    private int[] setShot() {
        try {
            String shot = scanner.nextLine().toUpperCase().trim();
            if (!makeCoordinatesList().contains(shot)) {
                throw new NumberFormatException();
            }
            int row = shot.charAt(0) - 'A' + 1;
            int column = Integer.parseInt(shot.substring(1));
            return new int[]{row, column};
        } catch (NumberFormatException e) {
            System.out.println("Error! You entered wrong coordinates! Try again:");
            return setShot();
        }
    }

    private String[] setCoordinates() {
        String[] shipCoordinates;
        while (true) {
            try {
                shipCoordinates = scanner.nextLine().toUpperCase().split("\\s+");
                for (int i = 0; i < 2; i++) {
                    if (!makeCoordinatesList().contains(shipCoordinates[i])) {
                        System.out.println("Error! Wrong ship location! Try again:");
                        setCoordinates();
                    }
                }
                break;
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error! Wrong ship location! Try again:");
            }
        }
        return shipCoordinates;
    }

    /**
     * Implement a method for check entered coordinates
     * @param sells of ship (5 - aircraft carrier, 4 - battleship, 3 - submarine, 3 - cruiser, 2 - destroyer)
     * @return int[4] with start and final coordinates of ship
     */
    private int[] getCoordinates(Player player, String shipName, int sells) {
        String[]shipCoordinates = setCoordinates();
        String startCoordinate = shipCoordinates[0];
        String finalCoordinate = shipCoordinates[1];

        int startRow = startCoordinate.charAt(0) - 'A' + 1;
        int startColumn = Integer.parseInt(startCoordinate.substring(1));
        int finalRow = finalCoordinate.charAt(0) - 'A' + 1;
        int finalColumn = Integer.parseInt(finalCoordinate.substring(1));

        int[] coordinates = fixDirection(startRow, startColumn, finalRow, finalColumn);

        boolean isCorrectLocation = startRow == finalRow || startColumn == finalColumn;
        System.out.println(isCorrectLocation);
        boolean isRightSize = Math.abs(finalRow - startRow) + 1 == sells || Math.abs(finalColumn - startColumn) + 1 == sells;
        System.out.println(isRightSize);
        boolean isNotTouchOthers = player.checkSurroundings(coordinates);
        System.out.println(isNotTouchOthers);

        if (isCorrectLocation && isRightSize && isNotTouchOthers) {
            return coordinates;
        } else {
            if (!isCorrectLocation) {
                System.out.println("Error! Wrong ship location! Try again:");
            }
            if (!isRightSize) {
                System.out.printf("Error! Wrong length of the %s! Try again:%n", shipName);
            }
            if (!isNotTouchOthers) {
                System.out.println("Error! You placed it too close to another one. Try again:");
            }
            return getCoordinates(player, shipName, sells);
        }
    }

    /**
     * @return coordinates in the right direction
     * Example: E6 D6 -> D6 E6; F7 F3 -> F3 F7
     */
    private int[] fixDirection(int startRow, int startColumn, int finalRow, int finalColumn) {
        return finalRow > startRow ? new int[]{startRow, startColumn, finalRow, finalColumn} :
                finalColumn > startColumn ? new int[]{startRow, startColumn, finalRow, finalColumn} :
                        finalRow < startRow ? new int[]{finalRow, startColumn, startRow, finalColumn} :
                                new int[]{startRow, finalColumn, finalRow, startColumn};
    }
}
