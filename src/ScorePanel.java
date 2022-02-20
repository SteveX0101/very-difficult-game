import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class ScorePanel extends JPanel {

    private static ScorePanel instance; // Single instance of ScorePanel

    // FONT COLOR:
    private static final Color TEXT_COLOR = new Color(0x57606f);

    private ScorePanel() {
        setOpaque(false);
        setBounds(0, 0, Window.getWidth(), Window.getHeight());
    }

    // Returns the single instance of score panel
    public static ScorePanel get() {
        if (ScorePanel.instance == null) {
            ScorePanel.instance = new ScorePanel();
        }

        return ScorePanel.instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(TEXT_COLOR);

        drawHighScore(g);
        drawCurrentScore(g);
        drawStartText(g);
        drawExitText(g);

        // Dispose and sync graphics
        g.dispose();
        getToolkit().sync();
    }

    // Shows the high score
    private void drawHighScore(Graphics g) {
        final String text = "High Score: " + Game.highScore;
        FontMetrics metrics = g.getFontMetrics();
        final int x = (getWidth() - metrics.stringWidth(text)) / 2;
        final int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(text, x, y - 24);
    }

    // Shows the current score
    public void drawCurrentScore(Graphics g) {
        final String text = "Current Score: " + Game.currentScore;

        FontMetrics metrics = g.getFontMetrics();
        final int x = (getWidth() - metrics.stringWidth(text)) / 2;
        final int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(text, x, y);
    }

    // Shows how to start the game
    public void drawStartText(Graphics g) {
        final String text = "Press <SPACE> to Start!";

        FontMetrics metrics = g.getFontMetrics();
        final int x = (getWidth() - metrics.stringWidth(text)) / 2;
        final int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(text, x, y + 24);
    }

    // Shows how to quit the game
    public void drawExitText(Graphics g) {
        final String text = "Hit <ESCAPE> to Quit!";

        FontMetrics metrics = g.getFontMetrics();
        final int x = (getWidth() - metrics.stringWidth(text)) / 2;
        final int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(text, x, y + 48);
    }
}
