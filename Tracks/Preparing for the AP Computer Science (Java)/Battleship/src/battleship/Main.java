package battleship;

public class Main {

    public static void main(String[] args) {
        Player firstPlayer = new Player();
        Player secondPlayer = new Player();
        GameLogic game = new GameLogic();
        game.play(firstPlayer, secondPlayer);
    }
}