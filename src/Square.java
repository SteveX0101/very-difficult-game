import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public final class Square extends JComponent implements MouseListener {

    // Color of the square object
    private Color color;

    // Position
    private int x, y;

    // Velocity
    private int speedX, speedY;

    // Direction
    private int dirX, dirY;

    // Value
    private final int ORDER;

    // Timer
    private float time;

    // Hidden:
    public static boolean HIDDEN;

    // Velocity Constant:
    private static final int MIN_SPEED = 2;
    private static final int MAX_SPEED = 4;

    // Size of a square object
    private static final int SIZE = 128;

    // Random:
    private static final Random RAND = new Random();

    // Font:
    private static final Font FONT = new Font("Arial", Font.PLAIN, 64);

    // Font Color:
    private static final Color FONT_COLOR = new Color(0xfcfcfc);

    // Colors from:
    // https://flatuicolors.com/palette/ca
    private static final Color COLOR_1 = new Color(0x54a0ff);
    private static final Color COLOR_2 = new Color(0x2e86de);
    private static final Color COLOR_3 = new Color(0x5f27cd);
    private static final Color COLOR_4 = new Color(0x341f97);

    public Square(int order) {
        setVisible(false); // Hides square until the square is finished initializing

        this.ORDER = order; // Set the order of the square

        addMouseListener(this); // Add the mouse listener

        // Randomizes the position, velocity, and direction
        randomPosition();
        randomVelocity();
        randomDirection();

        setBounds(x, y, SIZE, SIZE); // Sets the size of the square

        randomColor(); // Randomizes the color of the square

        setVisible(true); // Shows the square
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (color != null) {
            final String order = "" + (this.ORDER + 1);

            // Square:
            g.setColor(color);
            g.fillRect(0, 0, SIZE, SIZE);

            // Border:
            final int STROKE_SIZE = 2;

            g.setColor(FONT_COLOR);
            g.fillRect(0, 0, SIZE, STROKE_SIZE);
            g.fillRect(SIZE - STROKE_SIZE, 0, STROKE_SIZE, SIZE);
            g.fillRect(0, SIZE - STROKE_SIZE, SIZE, STROKE_SIZE);
            g.fillRect(0, 0, STROKE_SIZE, SIZE);

            // Order number:
            if (!HIDDEN) {
                g.setFont(FONT);

                g.setColor(FONT_COLOR);
                FontMetrics metrics = g.getFontMetrics();
                final int x = (getWidth() - metrics.stringWidth(order)) / 2;
                final int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

                g.drawString(order, x, y);
            }
        }

        // Dispose and sync graphics
        g.dispose();
        getToolkit().sync();
    }

    // Update the square
    public void update(float delta) {
        time += delta;

        final float actionTime = 1f / 60f; // ~60 updates per second

        if (time >= actionTime) {
            time = 0f;

            // Put updates here:
            collision(); // Check for collision

            moveSquare(); // Move the square

            randomizeSeed(); // Have a new random seed
        }
    }

    // Update the position of the square
    private void moveSquare() {
        x += (speedX * dirX);
        y += (speedY * dirY);

        setBounds(
                x,
                y,
                SIZE,
                SIZE
        );
    }

    // Check for collision with the screen borders
    private void collision() {
        final int posX = getX();
        final int posY = getY();
        final int windowWidth = Window.getWidth();
        final int windowHeight = Window.getHeight();

        // If the square hits the left or right border of the screen
        if (posX < 0 || posX + SIZE > windowWidth) {
            dirX *= -1; // Inverse the x direction

            // Randomizes the direction, velocity, and color of the square
            randomDirectionY();
            randomVelocity();
            randomColor();

            // Prevents the square from being stuck by check if
            // the new position would be outside the border of the screen
            final int newPosX = posX + (speedX * dirX);

            if (newPosX < 0) {
                this.x = 0;
            }
            if (newPosX + SIZE > windowWidth) {
                this.x = windowWidth - SIZE;
            }

            randomizeSeed(); // MORE RANDOM!
        }

        if (posY < 0 || posY + SIZE > windowHeight) {
            dirY *= -1; // Inverse the y direction

            // Randomizes the direction, velocity, and color of the square
            randomDirectionX();
            randomVelocity();
            randomColor();

            // Prevents the square from being stuck by check if
            // the new position would be outside the border of the screen
            final int newPosY = posY + (speedY * dirY);

            if (newPosY < 0) {
                this.y = 0;
            }
            if (newPosY + SIZE > windowHeight) {
                this.y = windowHeight - SIZE;
            }

            randomizeSeed(); // MORE RANDOM!
        }
    }

    // Randomizes the position of the square
    public void randomPosition() {
        randomizeSeed();
        this.x = RAND.nextInt(Window.getWidth() - SIZE);
        this.y = RAND.nextInt(Window.getHeight() - SIZE);
    }

    // Randomizes the velocity of the square
    public void randomVelocity() {
        randomizeSeed();
        this.speedX = MIN_SPEED + RAND.nextInt(MAX_SPEED - MIN_SPEED + 1);
        this.speedY = MIN_SPEED + RAND.nextInt(MAX_SPEED - MIN_SPEED + 1);
    }

    // Randomizes the directions of the square
    public void randomDirection() {
        randomizeSeed();
        randomDirectionX();
        randomDirectionY();
    }

    // Randomizes the x direction
    public void randomDirectionX() {
        this.dirX = (RAND.nextInt(2) == 0) ? -1 : 1;
    }

    // Randomizes the y direction
    public void randomDirectionY() {
        this.dirY = (RAND.nextInt(2) == 0) ? -1 : 1;
    }

    // Randomizes the color of the square
    public void randomColor() {
        randomizeSeed();

        int random = RAND.nextInt(4); // [0 ~ 3]

        switch (random) {
            case 0:
                color = COLOR_1;
                break;
            case 1:
                color = COLOR_2;
                break;
            case 2:
                color = COLOR_3;
                break;
            case 3:
                color = COLOR_4;
                break;
        }
    }

    // Randomizes the seed of the random object
    public static void randomizeSeed() {
        RAND.setSeed((long) (System.nanoTime() + System.currentTimeMillis() * Math.random()));
    }

    // Detects if the square has been pressed
    @Override
    public void mousePressed(MouseEvent e) {
        if (isVisible()) {
            Game.selectSquare(ORDER);

            setVisible(false);
        }
    }

    // Unused mouse events
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
