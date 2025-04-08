import javax.swing.border.LineBorder;
import java.awt.Color;
import java.util.List;

/**
 * Handles the highlighting of cells on the game board.
 * This class is responsible for visually indicating valid moves, selected pieces, and build locations.
 */
public class BoardHighlighting {
    private final GridCell[][] cells;
    private final GameConfig config;
    
    /**
     * Creates a new board highlighting manager.
     * 
     * @param config The game configuration
     * @param cells The grid cells
     */
    public BoardHighlighting(GameConfig config, GridCell[][] cells) {
        this.config = config;
        this.cells = cells;
    }
    
    /**
     * Highlights valid moves on the board.
     * 
     * @param gameState The current game state
     * @param playerPieces The map of players to their pieces
     */
    public void highlightValidMoves(GameState gameState, java.util.Map<Player, List<Player>> playerPieces) {
        // First clear all highlights
        clearAllHighlights();

        if (gameState.isGameStarted()) {
            if (gameState.getSelectedPiece() != null) {
                highlightSelectedPiece(gameState);
            } else {
                highlightCurrentPlayerPieces(gameState, playerPieces);
            }
        }
    }
    
    /**
     * Clears all highlights from the board.
     */
    public void clearAllHighlights() {
        for (int i = 0; i < config.getGridSize(); i++) {
            for (int j = 0; j < config.getGridSize(); j++) {
                cells[i][j].getVisualComponent().setBorder(new LineBorder(Color.BLACK, 1));
            }
        }
    }
    
    /**
     * Highlights the selected piece and its valid moves.
     * 
     * @param gameState The current game state
     */
    private void highlightSelectedPiece(GameState gameState) {
        // Highlight the selected piece with a yellow border
        int selectedRow = gameState.getSelectedPiece().getRow();
        int selectedCol = gameState.getSelectedPiece().getCol();
        cells[selectedRow][selectedCol].getVisualComponent().setBorder(new LineBorder(Color.YELLOW, 3));
        
        // Highlight valid moves from the selected piece
        for (int x = 0; x < config.getGridSize(); x++) {
            for (int y = 0; y < config.getGridSize(); y++) {
                if (isValidMove(selectedRow, selectedCol, x, y)) {
                    cells[x][y].getVisualComponent().setBorder(new LineBorder(Color.GREEN, 3));
                }
            }
        }
    }
    
    /**
     * Highlights the current player's pieces.
     * 
     * @param gameState The current game state
     * @param playerPieces The map of players to their pieces
     */
    private void highlightCurrentPlayerPieces(GameState gameState, java.util.Map<Player, List<Player>> playerPieces) {
        List<Player> currentPlayerPieces = playerPieces.get(gameState.getCurrentPlayer());
        
        for (int i = 0; i < config.getGridSize(); i++) {
            for (int j = 0; j < config.getGridSize(); j++) {
                GameEntity occupant = cells[i][j].getOccupant();
                if (occupant != null && currentPlayerPieces.contains(occupant)) {
                    // Highlight the current player's pieces with a light border
                    cells[i][j].getVisualComponent().setBorder(new LineBorder(gameState.getCurrentPlayer().getColor().brighter(), 2));
                }
            }
        }
    }
    
    /**
     * Highlights valid build locations for a worker.
     * 
     * @param workerRow The row of the worker
     * @param workerCol The column of the worker
     */
    public void highlightValidBuilds(int workerRow, int workerCol) {
        // First clear all highlights
        clearAllHighlights();

        // Highlight valid build locations
        for (int i = 0; i < config.getGridSize(); i++) {
            for (int j = 0; j < config.getGridSize(); j++) {
                if (isValidBuild(workerRow, workerCol, i, j)) {
                    cells[i][j].getVisualComponent().setBorder(new LineBorder(Color.GREEN, 2));
                }
            }
        }
    }
    
    /**
     * Checks if a move is valid.
     * 
     * @param fromRow The starting row
     * @param fromCol The starting column
     * @param toRow The target row
     * @param toCol The target column
     * @return true if the move is valid, false otherwise
     */
    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidPosition(toRow, toCol)) return false;
        
        // Check if target cell is occupied or has a dome
        GridCell targetCell = cells[toRow][toCol];
        if (targetCell.getOccupant() != null || targetCell.hasDome()) return false;
        
        // Check if the move is within one cell in any direction
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);
        
        // Get source and target cell heights
        int sourceHeight = cells[fromRow][fromCol].getBuildingLevel();
        int targetHeight = targetCell.getBuildingLevel();
        
        // Valid move is one step in any direction (including diagonally)
        // but not staying in the same place, and can only move up one level
        return (rowDiff <= 1 && colDiff <= 1) && !(rowDiff == 0 && colDiff == 0) 
               && (targetHeight <= sourceHeight + 1);
    }
    
    /**
     * Checks if a build action is valid.
     * 
     * @param workerRow The row of the worker
     * @param workerCol The column of the worker
     * @param buildRow The row to build on
     * @param buildCol The column to build on
     * @return true if the build is valid, false otherwise
     */
    private boolean isValidBuild(int workerRow, int workerCol, int buildRow, int buildCol) {
        if (!isValidPosition(buildRow, buildCol)) return false;
        
        // Check if target cell is adjacent to worker
        int rowDiff = Math.abs(workerRow - buildRow);
        int colDiff = Math.abs(workerCol - buildCol);
        if (rowDiff > 1 || colDiff > 1 || (rowDiff == 0 && colDiff == 0)) return false;
        
        // Check if target cell is occupied or has a dome
        GridCell targetCell = cells[buildRow][buildCol];
        if (targetCell.getOccupant() != null || targetCell.hasDome()) return false;
        
        // Check if building level is less than 4
        return targetCell.getBuildingLevel() < 4;
    }
    
    /**
     * Checks if a position is within the bounds of the grid.
     * @param row The row position
     * @param col The column position
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < config.getGridSize() && col >= 0 && col < config.getGridSize();
    }
} 