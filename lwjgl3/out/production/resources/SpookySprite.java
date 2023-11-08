import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpookySprite extends JPanel implements ActionListener {
    private int x, y, velX, velY;
    private boolean isJumping;
    private int jumpStartY;  // Variable to hold the y-coordinate where the jump started
    private BufferedImage spookyImage;

    public SpookySprite() {
        x = 50;
        y = 200;
        velX = 0;
        velY = 0;
        isJumping = false;

        try {
            spookyImage = ImageIO.read(new File("spooky_sprite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_LEFT) {
                    velX = -5;
                }

                if (key == KeyEvent.VK_RIGHT) {
                    velX = 5;
                }

                if (key == KeyEvent.VK_UP) {
                    velY = -5;
                }

                if (key == KeyEvent.VK_DOWN) {
                    velY = 5;
                }

                if (key == KeyEvent.VK_SPACE && !isJumping) {
                    jumpStartY = y;  // Save the y-coordinate where the jump starts
                    velY = -15;
                    isJumping = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                    velX = 0;
                }

                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
                    velY = 0;
                }
            }
        });

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        Timer timer = new Timer(50, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(spookyImage, x, y, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isJumping) {
            velY += 1;  // Simulating gravity by incrementing velocity
        }

        x += velX;
        y += velY;

        // Implementing landing point and stopping the jump
        if (isJumping && y >= jumpStartY) {
            y = jumpStartY;  // Reset to jump start point
            isJumping = false;
            velY = 0;
        }

        // Keeping the sprite within frame bounds for x and y coordinates
        if (x < 0) x = 0;
        if (x > getWidth() - spookyImage.getWidth()) x = getWidth() - spookyImage.getWidth();
        if (y < 0) y = 0;
        if (y > getHeight() - spookyImage.getHeight()) y = getHeight() - spookyImage.getHeight();

        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Spooky Moving Sprite");
        SpookySprite spookySprite = new SpookySprite();
        frame.add(spookySprite);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
