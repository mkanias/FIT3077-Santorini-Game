import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Represents a player in the game.
 */
public class Player extends GameEntity {
    private final JPanel visualComponent;
    private final Color color;
    private final String name;
    private GodCard godCard;
    private int row;
    private int col;

    /**
     * Creates a new player.
     * @param color The player's color
     * @param name The player's name
     */
    public Player(Color color, String name) {
        this.color = color;
        this.name = name;
        this.godCard = null; // Players start with no God Card
        this.visualComponent = new JPanel();
        this.visualComponent.setBackground(color);
        this.visualComponent.setPreferredSize(new Dimension(40, 40));
    }

    @Override
    public JPanel getVisualComponent() {
        return visualComponent;
    }

    /**
     * Gets the player's color.
     * @return The player's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the player's name.
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's assigned God Card.
     * @return The player's God Card, or null if none assigned
     */
    public GodCard getGodCard() {
        return godCard;
    }

    /**
     * Assigns a God Card to the player.
     * @param godCard The God Card to assign
     */
    public void setGodCard(GodCard godCard) {
        this.godCard = godCard;
    }

    /**
     * Gets the player's current row position.
     * @return The row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the player's current column position.
     * @return The column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the player's position on the board.
     * @param row The row position
     * @param col The column position
     */
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
} 