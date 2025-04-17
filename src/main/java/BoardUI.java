import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

/**
 * Handles the UI components and rendering for the game board.
 * This class is responsible for creating and managing the visual elements of the game.
 */
public class BoardUI {
    private final JPanel boardPanel;
    private final JPanel turnIndicator;
    private final GridCell[][] cells;
    private final GameConfig config;
    
    /**
     * Creates a new board UI manager.
     * 
     * @param config The game configuration
     * @param cells The grid cells
     */
    public BoardUI(GameConfig config, GridCell[][] cells) {
        this.config = config;
        this.cells = cells;
        
        // Initialize UI components
        this.boardPanel = new JPanel(new GridLayout(config.getGridSize(), config.getGridSize()));
        this.boardPanel.setBorder(new LineBorder(Color.BLACK, 2));
        this.turnIndicator = new JPanel();
    }
    
    /**
     * Initializes the board UI by creating and configuring all cells.
     * 
     * @param gameBoard The game board instance
     */
    public void initialize(GridGameBoard gameBoard) {
        // Create grid cells
        for (int i = 0; i < config.getGridSize(); i++) {
            for (int j = 0; j < config.getGridSize(); j++) {
                GridCell cell = new GridCell(i, j);
                cells[i][j] = cell;
                CellClickListener clickListener = new CellClickListener(gameBoard, i, j);
                cell.getVisualComponent().addMouseListener(clickListener);
                boardPanel.add(cell.getVisualComponent());
            }
        }
    }
    
    /**
     * Updates the turn indicator to show whose turn it is.
     * 
     * @param gameState The current game state
     * @param players The list of players
     * @param piecesPlaced The number of pieces placed by each player
     */
    public void updateTurnIndicator(GameState gameState, List<Player> players, Map<Player, Integer> piecesPlaced) {
        if (gameState.isGameOver()) {
            Player winner = gameState.getWinner();
            turnIndicator.setBackground(winner.getColor());
            turnIndicator.setToolTipText(winner.getName() + " wins by reaching level 3!");
            return;
        }

        if (!gameState.isGameStarted()) {
            // Show placement phase info
            Player currentPlayer = players.get(gameState.getCurrentPlayerPlacementIndex());
            turnIndicator.setBackground(currentPlayer.getColor());
            int placed = piecesPlaced.get(currentPlayer);
            turnIndicator.setToolTipText(currentPlayer.getName() + " placing piece " + (placed + 1));
        } else {
            // Show game phase info
            Player currentPlayer = gameState.getCurrentPlayer();
            turnIndicator.setBackground(currentPlayer.getColor());
            String phase = gameState.isInBuildPhase() ? "Build Phase" : "Move Phase";
            turnIndicator.setToolTipText(currentPlayer.getName() + " - " + phase);
        }
    }
    
    /**
     * Gets the board panel.
     * @return The board panel
     */
    public JPanel getBoardPanel() {
        return boardPanel;
    }
    
    /**
     * Gets the turn indicator panel.
     * @return The turn indicator panel
     */
    public JPanel getTurnIndicator() {
        return turnIndicator;
    }
} 