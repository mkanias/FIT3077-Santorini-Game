import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

/**
 * Represents a cell in the game grid.
 * Each cell can contain a game entity and has a visual representation.
 */
public class GridCell {
    private final JPanel visualComponent;
    private final int row;
    private final int col;
    private GameEntity occupant;
    private int buildingLevel; // 0-4, where 4 represents a dome

    /**
     * Creates a new grid cell at the specified position.
     * 
     * @param row The row position (0-based)
     * @param col The column position (0-based)
     */
    public GridCell(int row, int col) {
        this.visualComponent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Draw the building level with semi-transparency
                Graphics2D g2d = (Graphics2D) g;
                AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
                g2d.setComposite(alphaComposite);
                drawBuildingLevel(g2d);
            }
        };
        this.visualComponent.setBorder(new LineBorder(Color.BLACK, 1));
        this.visualComponent.setLayout(new BorderLayout());
        this.row = row;
        this.col = col;
        this.buildingLevel = 0;
    }

    /**
     * Draws the building level visualization.
     * @param g The Graphics object to draw with
     */
    private void drawBuildingLevel(Graphics2D g) {
        if (buildingLevel == 0) return;

        int width = visualComponent.getWidth();
        int height = visualComponent.getHeight();
        int padding = 8;
        
        if (buildingLevel == 4) {
            // Draw dome (black circle) with semi-transparency
            g.setColor(new Color(0, 0, 0, 180));
            int circleSize = Math.min(width, height) - 2 * padding;
            g.fillOval(padding, padding, circleSize, circleSize);
        } else {
            // Draw concentric squares for levels 1-3 with semi-transparency
            Color levelColor = new Color(100, 100, 100, 180);
            g.setColor(levelColor);
            for (int i = 0; i < buildingLevel; i++) {
                int size = Math.min(width, height) - 2 * padding - (i * 12);
                int x = padding + (i * 6);
                int y = padding + (i * 6);
                g.fillRect(x, y, size, size);
                g.setColor(new Color(50, 50, 50, 180));
                g.drawRect(x, y, size, size);
            }
        }
    }

    /**
     * Gets the visual component of this cell.
     * @return The JPanel that visually represents this cell
     */
    public JPanel getVisualComponent() {
        return visualComponent;
    }

    /**
     * Gets the row position of this cell.
     * @return The row position (0-based)
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column position of this cell.
     * @return The column position (0-based)
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the occupant of this cell.
     * This will update the visual representation of the cell.
     * 
     * @param entity The game entity to place in this cell, or null to clear the cell
     */
    public void setOccupant(GameEntity entity) {
        this.occupant = entity;
        visualComponent.removeAll();
        if (entity != null) {
            visualComponent.add(entity.getVisualComponent(), BorderLayout.CENTER);
        }
        visualComponent.revalidate();
        visualComponent.repaint();
    }

    /**
     * Gets the current occupant of this cell.
     * @return The game entity currently in this cell, or null if the cell is empty
     */
    public GameEntity getOccupant() {
        return occupant;
    }

    /**
     * Gets the current building level of this cell.
     * @return The building level (0-4, where 4 represents a dome)
     */
    public int getBuildingLevel() {
        return buildingLevel;
    }

    /**
     * Sets the building level of this cell.
     * @param level The building level (0-4, where 4 represents a dome)
     */
    public void setBuildingLevel(int level) {
        if (level >= 0 && level <= 4) {
            this.buildingLevel = level;
            visualComponent.repaint();
        }
    }

    /**
     * Checks if this cell has a dome (level 4).
     * @return true if the cell has a dome, false otherwise
     */
    public boolean hasDome() {
        return buildingLevel == 4;
    }
} 