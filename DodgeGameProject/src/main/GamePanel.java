package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements KeyListener {
    private int playerX = 250;
    private int playerY = 400;
    private int playerSize = 50; // Size of the player character
    private int score = 0;
    private ArrayList<Rectangle> obstacles;
    private Timer timer;
    private int movementSpeed = 10; // Initial movement speed
    private int speedIncreaseThreshold = 15; // Increase speed every 15 points
    private int speedIncreaseAmount = 2; // Amount to increase speed by
    private boolean gameRunning = false; // Flag to indicate whether the game is running
    Image backgroundImage;
    Image player;

    public GamePanel() {
    	this.setBackground(Color.BLACK);
    	backgroundImage = new ImageIcon("/maps/background.png").getImage();
    	player = new ImageIcon("/player/player.png").getImage();
        obstacles = new ArrayList<>();
        timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveObstacles();
                repaint();
            }
        });
        setFocusable(true);
        addKeyListener(this);

        
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

       Graphics2D g2 = (Graphics2D) g;
       
       g2.drawImage(backgroundImage, 0, 0, null);
       g2.drawImage(player, playerX, playerY, null);

        if (gameRunning) {
            
            g.setColor(Color.RED);
            g.fillRect(playerX, playerY, playerSize, playerSize);

            // Draw obstacles
            g.setColor(Color.BLUE);
            for (Rectangle obstacle : obstacles) {
                g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
            }

            // Display score
            g.setColor(Color.ORANGE);
            g.setFont(new Font("Minecraft", Font.PLAIN, 50));
            g.drawString("SCORE:  " + score, 150, 100);
        } else {
            // Display title screen
            g.setColor(Color.RED);
            g.setFont(new Font("Minecraft", Font.BOLD, 50));
            g.drawString("DODGE GAME", 120, 200);
            g.setFont(new Font("Minecraft", Font.PLAIN, 20));
            g.drawString("PRESS ENTER TO START", 160, 350);
            g.setFont(new Font("Arial", Font.PLAIN, 13));
            g.drawString("GAME DEVELOPER GROUP 8", 200, 550);
        }
    }

    private void moveObstacles() {
        Random rand = new Random();
        int randX = rand.nextInt(getWidth() - 50);

        // Determine how many obstacles to create (between 1 to 3)
        int numObstacles = rand.nextInt(3) + 1;

        for (int i = 0; i < numObstacles; i++) {
            if (obstacles.size() < 5) {
                obstacles.add(new Rectangle(randX, 20, 50, 50)); // Add new obstacle
            }
        }

        for (int i = 0; i < obstacles.size(); i++) {
            Rectangle obstacle = obstacles.get(i);
            obstacle.y += movementSpeed; // Move obstacle down
            if (obstacle.intersects(new Rectangle(playerX, playerY, playerSize, playerSize))) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
                System.exit(0);
            }
            if (obstacle.y > getHeight()) {
                obstacles.remove(i); // Remove obstacle if it's out of bounds
                i--;
                score++; // Increment score when obstacle passes beyond the player
            }
        }

        // Increase movement speed every 15 points
        if (score >= speedIncreaseThreshold) {
            speedIncreaseThreshold += 15; // Increase threshold for the next speed increase
            movementSpeed += speedIncreaseAmount; // Increase movement speed
        }
    }

    public void startGame() {
        gameRunning = true;
        timer.start();
    }

    public void stopGame() {
        gameRunning = false;
        timer.stop();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!gameRunning && keyCode == KeyEvent.VK_ENTER) {
            startGame();
        }
        if (gameRunning) {
            if (keyCode == KeyEvent.VK_LEFT && playerX > 0) {
                playerX -= movementSpeed; // Move left
            } else if (keyCode == KeyEvent.VK_RIGHT && playerX < getWidth() - playerSize) {
                playerX += movementSpeed; // Move right
            }
            repaint();
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}
}
