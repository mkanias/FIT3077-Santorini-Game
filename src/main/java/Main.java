import javax.swing.*;
import java.awt.*;

/**
 * Main class that sets up and launches the game.
 * This class is responsible for creating the game window and initializing the game board.
 */
public class Main {
    /**
     * The main entry point for the application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create and configure the main game window
        JFrame frame = createGameWindow();
        
        // Create and initialize the game board
        GameConfig config = new GameConfig(5, 2, 2, 1); // 5x5 grid, 2 players, 2 pieces each, 1 move per turn
        GridGameBoard gameBoard = new GridGameBoard(config);
        gameBoard.initialize();
        
        // Add UI components to the frame
        addTurnIndicator(frame, gameBoard);
        addLegend(frame, gameBoard);
        addGameBoard(frame, gameBoard);
        
        // Display the game window
        frame.setVisible(true);
    }
    
    /**
     * Creates and configures the main game window.
     * 
     * @return The configured JFrame
     */
    private static JFrame createGameWindow() {
        JFrame frame = new JFrame("Grid Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(null);
        return frame;
    }
    
    /**
     * Adds the turn indicator to the game window.
     * 
     * @param frame The game window
     * @param gameBoard The game board
     */
    private static void addTurnIndicator(JFrame frame, GridGameBoard gameBoard) {
        JPanel turnIndicator = gameBoard.getTurnIndicator();
        turnIndicator.setBounds(350, 20, 200, 30);
        frame.add(turnIndicator);
    }
    
    /**
     * Adds the player legend to the game window.
     * 
     * @param frame The game window
     * @param gameBoard The game board containing player information
     */
    private static void addLegend(JFrame frame, GridGameBoard gameBoard) {
        // Create the legend panel
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new GridLayout(gameBoard.getPlayers().size(), 1, 5, 5));
        legendPanel.setBounds(50, 200, 100, gameBoard.getPlayers().size() * 40);
        
        // Create legend items for each player
        for (Player player : gameBoard.getPlayers()) {
            JPanel playerLegend = createLegendItem(player.getColor(), player.getName());
            legendPanel.add(playerLegend);
        }
        
        frame.add(legendPanel);
    }
    
    /**
     * Creates a legend item for a player.
     * 
     * @param color The player's color
     * @param label The player's label
     * @return The legend item panel
     */
    private static JPanel createLegendItem(Color color, String label) {
        JPanel legendItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(color);
        colorPanel.setPreferredSize(new Dimension(20, 20));
        
        legendItem.add(colorPanel);
        legendItem.add(new JLabel(label));
        
        return legendItem;
    }
    
    /**
     * Adds the game board to the game window.
     * 
     * @param frame The game window
     * @param gameBoard The game board
     */
    private static void addGameBoard(JFrame frame, GridGameBoard gameBoard) {
        JPanel boardPanel = gameBoard.getBoardPanel();
        boardPanel.setBounds(185, 100, 500, 500);
        frame.add(boardPanel);
    }
}

