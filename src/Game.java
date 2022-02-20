import java.util.List;
import java.util.Vector;

public final class Game {

    public static Window window; // The single instance of the window

    // Start time of the game
    public static final float initTime = System.nanoTime();

    // List that contains all the square objects
    public static final List<Square> objects = new Vector<>();

    // Check if the game is running (Used for the game loop)
    public static boolean running;

    // Count how many square there should be
    public static int score = 0;

    // Score display by the score panel
    public static int currentScore = 0;
    public static int highScore = 0;

    // Count how many square how been pressed
    public static int count = 0;

    // Check if is in game (Not in score panel)
    public static boolean inGame;

    private Game() {} // We do not need any instances of Game

    // Running the game
    public static void run() {
        Game.window = Window.get(); // Creates and gets the single instance of Window

        update(); // Start the game loop
    }

    // Ends the game
    public static void stop() {
        running = false;
        window.dispose();
        System.exit(0);
    }

    // Game loop
    private static void update() {
        running = true;
        new Thread(() -> {
            float startTime = getTime();
            float endTime;
            float delta = -1f;

            while(running) {
                if (delta >= 0) {
                    // Update here:
                    updateObjects(delta);
                }

                endTime = getTime();
                delta = endTime - startTime;
                startTime = endTime;
            }
        }).start();
    }

    // Returns the current time
    public static float getTime() {
        return (float) ((System.nanoTime() - initTime) * 1E-9);
    }

    // Updates the square objects
    public static void updateObjects(float delta) {
        if (objects.isEmpty() || !inGame) {
            return;
        }
        for (final Square sq : new Vector<>(objects)) {
            if (sq.isVisible()) { // Update only the visible squares
                sq.update(delta);
            }
        }
    }

    // Starts the game
    public static void startGame() {
        Game.window.hideScorePanel();

        count = 0;
        Square.HIDDEN = false; // Show the order numbers
        clearObjects();
        ++score;

        for (int i = 0; i < score; ++i) {
            if (i < objects.size()) { // Reuse square object if possible
                objects.get(i).setVisible(true);
            } else { // Creates a new square object
                final Square square = new Square(i);
                objects.add(square);
                Game.window.showSquare(square);
            }
        }

        inGame = true;
    }

    // Ends the game
    public static void endGame() {
        inGame = false;
        count = 0;
        clearObjects();
        currentScore = 0;
        if (highScore < score) {
            highScore = score;
        }
        score = 0;

        Game.window.showScorePanel();
    }

    // Shows the score panel, and update the current scores and high score
    public static void showScore() {
        currentScore = score;
        if (highScore < score) {
            highScore = score;
        }
        Game.window.showScorePanel();
    }

    // Check if the correct square has been pressed
    public static void selectSquare(int order) {
        if (order == count) {
            ++count;
        } else {
            endGame();
        }

        if (count == score) {
            showScore();
        } else {
            Square.HIDDEN = true; // Hide the order numbers
        }
    }

    public static void clearObjects() {
        for (final Square sq : objects) {
            sq.setVisible(false);
        }
    }
}
