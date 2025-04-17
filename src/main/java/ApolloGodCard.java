/**
 * Apollo God Card implementation.
 * Apollo's power: Your worker may move into an opponent worker's space by forcing 
 * their worker to the space yours just vacated.
 */
public class ApolloGodCard extends BaseGodCard {
    
    public ApolloGodCard() {
        super("Apollo",
              "Your worker may move into an opponent worker's space by forcing their worker to the space yours just vacated.");
    }

    @Override
    public boolean isValidMove(BoardLogic boardLogic, int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidPosition(boardLogic, toRow, toCol)) {
            return false;
        }

        // Check if move is to adjacent space
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);
        if (rowDiff > 1 || colDiff > 1 || (rowDiff == 0 && colDiff == 0)) {
            return false;
        }

        GridCell targetCell = boardLogic.getCell(toRow, toCol);
        GameEntity occupant = targetCell.getOccupant();

        // Allow moving to empty spaces normally
        if (occupant == null) {
            return super.isValidMove(boardLogic, fromRow, fromCol, toRow, toCol);
        }

        // Special Apollo power: can swap with opponent's worker
        if (occupant instanceof Player) {
            Player worker = (Player) occupant;
            // Check if it's an opponent's worker (different color)
            if (!worker.getColor().equals(boardLogic.getCell(fromRow, fromCol).getOccupant().getColor())) {
                // Check if height difference is at most 1 level
                int fromHeight = boardLogic.getCell(fromRow, fromCol).getBuildingLevel();
                int toHeight = targetCell.getBuildingLevel();
                return Math.abs(toHeight - fromHeight) <= 1;
            }
        }

        return false;
    }

    @Override
    public void afterMove(BoardLogic boardLogic, int fromRow, int fromCol, int toRow, int toCol) {
        GridCell targetCell = boardLogic.getCell(toRow, toCol);
        GameEntity occupant = targetCell.getOccupant();

        // If we moved into an opponent's space, swap their worker to our old space
        if (occupant instanceof Player) {
            Player worker = (Player) occupant;
            if (!worker.getColor().equals(boardLogic.getCell(fromRow, fromCol).getOccupant().getColor())) {
                // Move opponent's worker to our old space
                worker.setPosition(fromRow, fromCol);
                boardLogic.getCell(fromRow, fromCol).setOccupant(worker);
            }
        }
    }

    private boolean isValidPosition(BoardLogic boardLogic, int row, int col) {
        return row >= 0 && row < boardLogic.getConfig().getGridSize() && 
               col >= 0 && col < boardLogic.getConfig().getGridSize();
    }
} 