//Author : Aniruddh Abhisheki
//Roll No : 7
//Title : Dots and Boxs
//Start Date : 14/08/2024
//Modified Date : 04/09/2024
//Description : "Dots and Boxes" is a classic two-player pencil-and-paper game where players take turns drawing lines between adjacent dots to form boxes.
// The game is played on a grid of dots, and the goal is to complete as many boxes as possible to score points.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EnhancedDotNBoxs extends JPanel {
    private static final int SIZE = 5; // Grid size
    private static final int DOT_SIZE = 10; // Dot size
    private static final int GRID_SPACING = 40; // Space between dots

    private boolean[][] horizontalLines = new boolean[SIZE][SIZE - 1];
    private boolean[][] verticalLines = new boolean[SIZE - 1][SIZE];
    private int[][] boxes = new int[SIZE - 1][SIZE - 1];
    private int[][] horizontalLineOwners = new int[SIZE][SIZE - 1]; // Tracks line ownership
    private int[][] verticalLineOwners = new int[SIZE - 1][SIZE];   // Tracks line ownership
    private int currentPlayer = 1;
    private int[] scores = new int[2]; // Scores for Player 1 and Player 2
    private boolean gameOver = false;

    public EnhancedDotNBoxs() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver) return;

                int offsetX = (getWidth() - (SIZE - 1) * GRID_SPACING) / 2;
                int offsetY = (getHeight() - (SIZE - 1) * GRID_SPACING) / 2;

                int x = e.getX() - offsetX;
                int y = e.getY() - offsetY;

                int col = x / GRID_SPACING;
                int row = y / GRID_SPACING;

                if (x % GRID_SPACING < GRID_SPACING / 4 && row < SIZE - 1 && !verticalLines[row][col]) {
                    verticalLines[row][col] = true;
                    verticalLineOwners[row][col] = currentPlayer;  // Set owner of the line
                    if (!checkAndClaimBox(row, col, false)) currentPlayer = 3 - currentPlayer;
                } else if (y % GRID_SPACING < GRID_SPACING / 4 && col < SIZE - 1 && !horizontalLines[row][col]) {
                    horizontalLines[row][col] = true;
                    horizontalLineOwners[row][col] = currentPlayer; // Set owner of the line
                    if (!checkAndClaimBox(row, col, true)) currentPlayer = 3 - currentPlayer;
                }
                repaint();
                checkGameOver();
            }
        });

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        add(restartButton, BorderLayout.SOUTH);
    }

    private boolean checkAndClaimBox(int row, int col, boolean isHorizontal) {
        boolean claimed = false;

        if (isHorizontal) {
            if (row > 0 && horizontalLines[row][col] && horizontalLines[row - 1][col]
                    && verticalLines[row - 1][col] && verticalLines[row - 1][col + 1]) {
                boxes[row - 1][col] = currentPlayer;
                scores[currentPlayer - 1]++;
                claimed = true;
            }
            if (row < SIZE - 1 && horizontalLines[row][col] && horizontalLines[row + 1][col]
                    && verticalLines[row][col] && verticalLines[row][col + 1]) {
                boxes[row][col] = currentPlayer;
                scores[currentPlayer - 1]++;
                claimed = true;
            }
        } else {
            if (col > 0 && verticalLines[row][col] && verticalLines[row][col - 1]
                    && horizontalLines[row][col - 1] && horizontalLines[row + 1][col - 1]) {
                boxes[row][col - 1] = currentPlayer;
                scores[currentPlayer - 1]++;
                claimed = true;
            }
            if (col < SIZE - 1 && verticalLines[row][col] && verticalLines[row][col + 1]
                    && horizontalLines[row][col] && horizontalLines[row + 1][col]) {
                boxes[row][col] = currentPlayer;
                scores[currentPlayer - 1]++;
                claimed = true;
            }
        }

        return claimed;
    }

    private void checkGameOver() {
        boolean allBoxesClaimed = true;
        for (int i = 0; i < SIZE - 1; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if (boxes[i][j] == 0) {
                    allBoxesClaimed = false;
                    break;
                }
            }
        }
        gameOver = allBoxesClaimed;
    }

    private void restartGame() {
        horizontalLines = new boolean[SIZE][SIZE - 1];
        verticalLines = new boolean[SIZE - 1][SIZE];
        horizontalLineOwners = new int[SIZE][SIZE - 1]; // Reset line owners
        verticalLineOwners = new int[SIZE - 1][SIZE];   // Reset line owners
        boxes = new int[SIZE - 1][SIZE - 1];
        scores = new int[2];
        currentPlayer = 1;
        gameOver = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        Graphics2D g2d = (Graphics2D) g; // Cast to Graphics2D for line thickness
        g2d.setStroke(new BasicStroke(3)); // Set line thickness
    
        int offsetX = (getWidth() - (SIZE - 1) * GRID_SPACING) / 2;
        int offsetY = (getHeight() - (SIZE - 1) * GRID_SPACING) / 2;
    
        // Draw dots
        g.setColor(Color.BLACK);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                g.fillOval(i * GRID_SPACING + offsetX - DOT_SIZE / 2, j * GRID_SPACING + offsetY - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
            }
        }
    
        // Draw horizontal lines with player-specific colors
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if (horizontalLines[i][j]) {
                    g2d.setColor(horizontalLineOwners[i][j] == 1 ? Color.RED : Color.BLUE);  // Color based on owner
                    g2d.drawLine(j * GRID_SPACING + offsetX, i * GRID_SPACING + offsetY,
                            (j + 1) * GRID_SPACING + offsetX, i * GRID_SPACING + offsetY);
                }
            }
        }
    
        // Draw vertical lines with player-specific colors
        for (int i = 0; i < SIZE - 1; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (verticalLines[i][j]) {
                    g2d.setColor(verticalLineOwners[i][j] == 1 ? Color.RED : Color.BLUE);  // Color based on owner
                    g2d.drawLine(j * GRID_SPACING + offsetX, i * GRID_SPACING + offsetY,
                            j * GRID_SPACING + offsetX, (i + 1) * GRID_SPACING + offsetY);
                }
            }
        }
    
        // Draw filled boxes
        for (int i = 0; i < SIZE - 1; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if (boxes[i][j] != 0) {
                    g.setColor(boxes[i][j] == 1 ? Color.RED : Color.BLUE);
                    g.fillRect(j * GRID_SPACING + offsetX, i * GRID_SPACING + offsetY, GRID_SPACING, GRID_SPACING);
                    g.setColor(Color.BLACK);
                }
            }
        }
    
        // Draw current player and scores
        g.setColor(Color.BLACK);
        g.drawString("Player 1 Score: " + scores[0], 35, SIZE * GRID_SPACING + 150);
        g.drawString("Player 2 Score: " + scores[1], 175, SIZE * GRID_SPACING + 150);
        g.drawString("Player " + currentPlayer + "'s turn", 300, SIZE * GRID_SPACING + 150);
    
        // Define color box size
        int boxSize = 20;
        int yOffset = SIZE * GRID_SPACING + 130; // Position for color boxes
    
        // Draw color box for Player 1
        g.setColor(Color.RED);
        g.fillRect(10, yOffset, boxSize, boxSize);
        g.setColor(Color.BLACK);
        g.drawRect(10, yOffset, boxSize, boxSize);
    
        // Draw color box for Player 2
        g.setColor(Color.BLUE);
        g.fillRect(150, yOffset, boxSize, boxSize);
        g.setColor(Color.BLACK);
        g.drawRect(150, yOffset, boxSize, boxSize);
    
        // Draw thick lines next to the score
        g2d.setStroke(new BasicStroke(5)); // Thicker lines for score display
        g2d.setColor(Color.RED);
        g2d.drawLine(10 + boxSize + 10, SIZE * GRID_SPACING + 170, 50 + boxSize + 10, SIZE * GRID_SPACING + 170); // Player 1 color line
    
        g2d.setColor(Color.BLUE);
        g2d.drawLine(150 + boxSize + 10, SIZE * GRID_SPACING + 170, 190 + boxSize + 10, SIZE * GRID_SPACING + 170); // Player 2 color line
    
        if (gameOver) {
            g.drawString("Game Over! \n Player 1 Score : " + scores[0], 10, SIZE * GRID_SPACING + 120);
            g.drawString(" Player 2 Score : " + scores[1], 200, SIZE * GRID_SPACING + 120);
        }
    }
    

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((SIZE - 1) * GRID_SPACING + 400, (SIZE - 1) * GRID_SPACING + 200);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dots and Boxes");
        EnhancedDotNBoxs panel = new EnhancedDotNBoxs();
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
