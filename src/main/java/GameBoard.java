import javax.swing.JPanel;

/**
 * Represents a game board that can contain game entities.
 * This interface defines the basic operations that all game boards must implement.
 */
public interface GameBoard {
    /**
     * Initializes the game board.
     * This should be called before any other operations on the board.
     */
    void initialize();
    
    /**
     * Adds a game entity to the board at the specified position.
     * 
     * @param entity The game entity to add
     * @param row The row position (0-based)
     * @param col The column position (0-based)
     */
    void addEntity(GameEntity entity, int row, int col);
    
    /**
     * Moves a game entity to a new position on the board.
     * 
     * @param entity The game entity to move
     * @param toRow The destination row position (0-based)
     * @param toCol The destination column position (0-based)
     */
    void moveEntity(GameEntity entity, int toRow, int toCol);
    
    /**
     * Checks if a move from one position to another is valid.
     * 
     * @param fromRow The starting row position (0-based)
     * @param fromCol The starting column position (0-based)
     * @param toRow The destination row position (0-based)
     * @param toCol The destination column position (0-based)
     * @return true if the move is valid, false otherwise
     */
    boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol);
    
    /**
     * Gets the visual panel representing the game board.
     * @return The JPanel that visually represents the game board
     */
    JPanel getBoardPanel();
    
    /**
     * Gets the current player.
     * @return The current player
     */
    Player getCurrentPlayer();
    
    /**
     * Gets the turn indicator panel.
     * @return The JPanel that shows whose turn it is
     */
    JPanel getTurnIndicator();
} 