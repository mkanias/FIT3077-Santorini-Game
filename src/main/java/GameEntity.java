import javax.swing.JPanel;

/**
 * Represents an entity that can be placed on the game board.
 * This interface defines the basic properties and behaviors that all game entities must implement.
 */
public interface GameEntity {
    /**
     * Gets the visual representation of this entity.
     * @return The JPanel that visually represents this entity
     */
    JPanel getVisualComponent();
    
    /**
     * Sets the position of this entity on the game board.
     * @param row The row position (0-based)
     * @param col The column position (0-based)
     */
    void setPosition(int row, int col);
    
    /**
     * Gets the current row position of this entity.
     * @return The row position (0-based)
     */
    int getRow();
    
    /**
     * Gets the current column position of this entity.
     * @return The column position (0-based)
     */
    int getCol();
} 