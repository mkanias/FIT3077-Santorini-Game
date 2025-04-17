

import javax.swing.JPanel;
import java.awt.Color;

/**
 * Base class for all game entities that can occupy cells on the board.
 */
public abstract class GameEntity {
    protected JPanel visualComponent;

    /**
     * Gets the visual component representing this entity.
     * @return The visual component
     */
    public JPanel getVisualComponent() {
        return visualComponent;
    }

    /**
     * Gets the row position of this entity.
     * @return The row position
     */
    public abstract int getRow();

    /**
     * Gets the column position of this entity.
     * @return The column position
     */
    public abstract int getCol();

    /**
     * Sets the position of this entity.
     * @param row The row position
     * @param col The column position
     */
    public abstract void setPosition(int row, int col);

    /**
     * Gets the color of this entity.
     * @return The color of the entity
     */
    public abstract Color getColor();
} 