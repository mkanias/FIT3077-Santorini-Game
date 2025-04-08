import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

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
    public void updateTurnIndicator(GameState gameState, List<Player> players, java.util.Map<Player, Integer> piecesPlaced) {
        turnIndicator.removeAll();
        turnIndicator.setLayout(new java.awt.BorderLayout());
        
        if (!gameState.isGameStarted()) {
            updatePlacementTurnIndicator(gameState, players, piecesPlaced);
        } else {
            updateGameTurnIndicator(gameState, players);
        }
        
        turnIndicator.revalidate();
        turnIndicator.repaint();
    }
    
    /**
     * Updates the turn indicator during the placement phase.
     * 
     * @param gameState The current game state
     * @param players The list of players
     * @param piecesPlaced The number of pieces placed by each player
     */
    private void updatePlacementTurnIndicator(GameState gameState, List<Player> players, java.util.Map<Player, Integer> piecesPlaced) {
        Player currentPlayer = players.get(gameState.getCurrentPlayerPlacementIndex());
        int piecesPlacedCount = piecesPlaced.get(currentPlayer);
        
        turnIndicator.setBackground(currentPlayer.getColor());
        JPanel textPanel = new JPanel();
        textPanel.setBackground(currentPlayer.getColor());
        textPanel.setForeground(Color.WHITE);
        
        textPanel.add(new javax.swing.JLabel("Place " + currentPlayer.getName() + 
                                            " (" + (piecesPlacedCount + 1) + " of " + config.getPiecesPerPlayer() + ")"));
        
        turnIndicator.add(textPanel, java.awt.BorderLayout.CENTER);
    }
    
    /**
     * Updates the turn indicator during the game phase.
     * 
     * @param gameState The current game state
     * @param players The list of players
     */
    private void updateGameTurnIndicator(GameState gameState, List<Player> players) {
        Player currentPlayer = gameState.getCurrentPlayer();
        turnIndicator.setBackground(currentPlayer.getColor());
        JPanel textPanel = new JPanel();
        textPanel.setBackground(currentPlayer.getColor());
        textPanel.setForeground(Color.WHITE);
        
        // Show which piece is selected or which piece to select
        if (gameState.isInBuildPhase()) {
            // Show build phase message
            textPanel.add(new javax.swing.JLabel(currentPlayer.getName() + "'s Turn - Select a location to build"));
        } else if (gameState.getSelectedPiece() != null) {
            // Determine which piece is selected
            String pieceNumber = getSelectedPieceNumber(gameState, players);
            textPanel.add(new javax.swing.JLabel(currentPlayer.getName() + "'s Turn - Selected Piece " + pieceNumber + 
                                                " (Moves remaining: " + gameState.getMovesRemaining() + ")"));
        } else {
            // Show which piece to select next
            textPanel.add(new javax.swing.JLabel(currentPlayer.getName() + "'s Turn - Select a piece to move"));
        }
        turnIndicator.add(textPanel, java.awt.BorderLayout.CENTER);
    }
    
    /**
     * Gets the number of the selected piece.
     * 
     * @param gameState The current game state
     * @param players The list of players
     * @return The piece number as a string
     */
    private String getSelectedPieceNumber(GameState gameState, List<Player> players) {
        Player currentPlayer = gameState.getCurrentPlayer();
        Player selectedPiece = gameState.getSelectedPiece();
        
        // Find the index of the selected piece in the current player's pieces
        for (int i = 0; i < players.size(); i++) {
            if (selectedPiece == players.get(i)) {
                return (i + 1) + " of " + players.size();
            }
        }
        return "unknown";
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