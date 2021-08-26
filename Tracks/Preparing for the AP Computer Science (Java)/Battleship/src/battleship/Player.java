package battleship;

import static battleship.CellState.*;

public class Player {
    protected String[][] ownField = makeField();
    protected String[][] enemyField = makeField();

    public Player() {}

    private String[][] makeField() {
        String[][] field = new String[11][11];
        char start = 'A';
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (i == 0 && j == 0) {
                    field[i][j] = " ";
                }
                else if (i == 0) {
                    field[i][j] = String.valueOf(j);
                } else if (j == 0){
                    field[i][j] = String.valueOf(start++);
                } else {
                    field[i][j] = EMPTY.getState();
                }
            }
        }
        return field;
    }

    public String getOwnCell(int row, int column) {
        return ownField[row][column];
    }

    public void changeOwnCell(int row, int column, CellState cellState) {
        ownField[row][column] = cellState.getState();
    }

    public void changeEnemyCell(int row, int column, CellState cellState) {
        enemyField[row][column] = cellState.getState();
    }

    public void placeShip(int[] coordinates) {
        int startRow = coordinates[0];
        int startColumn = coordinates[1];
        int finalRow = coordinates[2];
        int finalColumn = coordinates[3];

        boolean isHorizontal = startRow == finalRow;
        if (isHorizontal) {
            for (; startColumn <= finalColumn; startColumn++) {
                ownField[startRow][startColumn] = FILL.getState();
            }
        } else {
            for (; startRow <= finalRow; startRow++) {
                ownField[startRow][startColumn] = FILL.getState();
            }
        }

        printField();
    }

    public boolean checkSurroundings(int... coordinates) {
        boolean isFree = true;

        int startRow = coordinates[0] == 1 ? 1 : coordinates[0] - 1;
        int startColumn = coordinates[1] == 1? 1 : coordinates[1] - 1;
        int finalRow = coordinates[2] == 10 ? 10 : coordinates[2] + 1;
        int finalColumn = coordinates[3] == 10 ? 10 : coordinates[3] + 1;

        boolean isHorizontal = startRow == finalRow;
        if (isHorizontal) {
            for (; startColumn <= finalColumn; startColumn++) {
                if (ownField[startRow][startColumn].equals(FILL.getState())) {
                    isFree = false;
                    break;
                }
            }
        } else {
            for (; startRow <= finalRow; startRow++) {
                if (ownField[startRow][startColumn].equals(FILL.getState())) {
                    isFree = false;
                    break;
                }
            }
        }
        return isFree;
    }

    public boolean isEndOfGame() {
        boolean isEnd = true;
        for (String[] x : ownField) {
            for (String y : x) {
                if (y.equals(FILL.getState())) {
                    isEnd = false;
                    break;
                }
            }
        }
        return isEnd;
    }

    public void printField() {
        for (String[] x : ownField) {
            for (String y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }

    public void printEmptyField() {
        for (String[] x : enemyField) {
            for (String y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
}
