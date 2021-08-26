package battleship;

public enum CellState {
    EMPTY("~"),
    FILL("O"),
    MISS("M"),
    HIT("X");

    String state;

    CellState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
