package battleship;

public enum Ship {
    AIRCRAFT("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    String shipName;
    int sells;

    Ship(String shipName, int sells) {
        this.shipName = shipName;
        this.sells = sells;
    }

    public String getShipName() {
        return shipName;
    }

    public int getSells() {
        return sells;
    }
}
