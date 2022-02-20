import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public final class Window {
    private static Window instance; // Single instance of the window

    private final JFrame frame; // Using JFrame for the window
    private final JPanel panel; // The score panel

    // Screen size
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    // Title of the window
    private static final String TITLE = "Very Difficult Game";

    private Window() {
        // Initialize the JFrame
        frame = new JFrame();

        // Sets the layout manager to be null
        frame.setLayout(null);

        // Sets the title
        frame.setTitle(TITLE);

        // Sets the JFrame background to be black
        frame.getContentPane().setBackground(Color.BLACK);

        // Sets the window to be full screen windowed
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setPreferredSize(SCREEN_SIZE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
            // Closing the JFrame will quit the game
            @Override
            public void windowClosed(WindowEvent e) {
                Game.stop();
            }
        });

        // Add the two key binds
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                // Hitting <ESCAPE> will quit the game
                if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE) {
                    Game.stop();
                }

                // Hitting <SPACE> will start the game
                if (e.getExtendedKeyCode() == KeyEvent.VK_SPACE) {
                    if (panel != null && panel.isVisible()) {
                        Game.startGame();
                    }
                }
            }
        });

        // Add the score panel to the JFrame
        frame.add(panel = ScorePanel.get());

        // Sets the JFrame to be visible
        frame.setVisible(true);
    }

    // Returns the single instance of the Window object
    public static Window get() {
        if (Window.instance == null) {
            Window.instance = new Window();
        }

        return Window.instance;
    }

    // Sets the visible of the score panel
    public void hideScorePanel() {
        panel.setVisible(false);
    }
    public void showScorePanel() {
        panel.setVisible(true);
    }


    // Dispose the window
    public void dispose() {
        frame.dispose();
    }

    // Returns the dimensions of screen
    public static int getWidth() {
        return SCREEN_SIZE.width;
    }
    public static int getHeight() {
        return SCREEN_SIZE.height;
    }

    // Add a square object to the JFrame
    public void showSquare(Square square) {
        frame.add(square);
    }
}
