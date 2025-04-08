import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Represents a player piece in the game.
 * Each player has a color, name, and position on the game board.
 */
public class Player implements GameEntity {
    private final JPanel visualComponent;
    private final Color color;
    private final String name;
    private int row;
    private int col;

    /**
     * Creates a new player piece with the specified color and name.
     * 
     * @param color The color of the player piece
     * @param name The name of the player
     */
    public Player(Color color, String name) {
        this.color = color;
        this.name = name;
        this.visualComponent = new JPanel();
        this.visualComponent.setBackground(color);
        this.visualComponent.setPreferredSize(new Dimension(40, 40));
    }

    @Override
    public JPanel getVisualComponent() {
        return visualComponent;
    }

    @Override
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    /**
     * Gets the color of this player piece.
     * @return The color of the player piece
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the name of this player.
     * @return The name of the player
     */
    public String getName() {
        return name;
    }
} 