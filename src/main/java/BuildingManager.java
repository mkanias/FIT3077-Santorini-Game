
/**
 * Manages building operations in the game.
 * This class handles building validation and execution.
 */
public class BuildingManager {
    private final GridCell[][] cells;
    private final int gridSize;
    
    /**
     * Creates a new building manager.
     * 
     * @param cells The grid cells
     * @param gridSize The size of the grid
     */
    public BuildingManager(GridCell[][] cells, int gridSize) {
        this.cells = cells;
        this.gridSize = gridSize;
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
    public boolean isValidBuild(int workerRow, int workerCol, int buildRow, int buildCol) {
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
     * Builds on a cell.
     * 
     * @param workerRow The row of the worker
     * @param workerCol The column of the worker
     * @param buildRow The row to build on
     * @param buildCol The column to build on
     * @return true if the build was successful, false otherwise
     */
    public boolean build(int workerRow, int workerCol, int buildRow, int buildCol) {
        if (!isValidBuild(workerRow, workerCol, buildRow, buildCol)) return false;
        
        GridCell targetCell = cells[buildRow][buildCol];
        int currentLevel = targetCell.getBuildingLevel();
        targetCell.setBuildingLevel(currentLevel + 1);
        
        return true;
    }
    
    /**
     * Checks if a position is within the bounds of the grid.
     * @param row The row position
     * @param col The column position
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < gridSize && col >= 0 && col < gridSize;
    }
} 