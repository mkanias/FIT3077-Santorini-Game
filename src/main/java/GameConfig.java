
/**
 * Configuration class for the game board.
 * Encapsulates all configurable parameters.
 */
public class GameConfig {
    private final int gridSize;
    private final int numPlayers;
    private final int piecesPerPlayer;
    private final int movesPerTurn;
    
    /**
     * Creates a new game configuration.
     * 
     * @param gridSize The size of the grid (gridSize x gridSize)
     * @param numPlayers The number of players in the game
     * @param piecesPerPlayer The number of pieces each player has
     * @param movesPerTurn The number of moves each piece can make per turn
     */
    public GameConfig(int gridSize, int numPlayers, int piecesPerPlayer, int movesPerTurn) {
        this.gridSize = gridSize;
        this.numPlayers = numPlayers;
        this.piecesPerPlayer = piecesPerPlayer;
        this.movesPerTurn = movesPerTurn;
    }
    
    /**
     * Gets the grid size.
     * @return The grid size
     */
    public int getGridSize() {
        return gridSize;
    }
    
    /**
     * Gets the number of players.
     * @return The number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }
    
    /**
     * Gets the number of pieces per player.
     * @return The number of pieces per player
     */
    public int getPiecesPerPlayer() {
        return piecesPerPlayer;
    }
    
    /**
     * Gets the number of moves per turn.
     * @return The number of moves per turn
     */
    public int getMovesPerTurn() {
        return movesPerTurn;
    }
} 