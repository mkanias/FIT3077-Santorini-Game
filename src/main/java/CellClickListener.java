import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Mouse listener for grid cells.
 * Handles mouse clicks on cells for piece placement and movement.
 */
public class CellClickListener extends MouseAdapter {
    private final GridGameBoard gameBoard;
    private final int row;
    private final int col;

    /**
     * Creates a new cell click listener.
     * 
     * @param gameBoard The game board
     * @param row The row position of the cell
     * @param col The column position of the cell
     */
    public CellClickListener(GridGameBoard gameBoard, int row, int col) {
        this.gameBoard = gameBoard;
        this.row = row;
        this.col = col;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameBoard.isGameStarted()) {
            handlePlacementPhase();
        } else {
            handleGamePhase();
        }
    }
    
    /**
     * Handles mouse clicks during the placement phase.
     */
    private void handlePlacementPhase() {
        Player currentPlayer = gameBoard.getPlayers().get(gameBoard.getCurrentPlayerPlacementIndex());
        List<Player> playerPieces = gameBoard.getPlayerPieces(currentPlayer);
        int piecesPlaced = gameBoard.getPiecesPlaced(currentPlayer);
        
        if (piecesPlaced < playerPieces.size()) {
            Player pieceToPlace = playerPieces.get(piecesPlaced);
            gameBoard.addEntity(pieceToPlace, row, col);
        }
    }
    
    /**
     * Handles mouse clicks during the game phase.
     */
    private void handleGamePhase() {
        GridCell clickedCell = gameBoard.getCell(row, col);
        GameEntity occupant = clickedCell.getOccupant();
        
        if (gameBoard.isInBuildPhase()) {
            handleBuildPhase();
        } else if (gameBoard.getSelectedPiece() != null) {
            handleSelectedPieceClick(occupant);
        } else {
            handlePieceSelection(occupant);
        }
    }
    
    /**
     * Handles clicks when a piece is already selected.
     * 
     * @param occupant The occupant of the clicked cell
     */
    private void handleSelectedPieceClick(GameEntity occupant) {
        Player selectedPiece = gameBoard.getSelectedPiece();
        int fromRow = selectedPiece.getRow();
        int fromCol = selectedPiece.getCol();
        
        // Check if we're clicking on a valid move destination
        if (gameBoard.isValidMove(fromRow, fromCol, row, col)) {
            gameBoard.moveEntity(selectedPiece, row, col);
        } 
        // Check if we're clicking on another piece of the current player
        else if (occupant != null && occupant instanceof Player) {
            Player clickedPiece = (Player) occupant;
            boolean isCurrentPlayerPiece = isCurrentPlayerPiece(clickedPiece);
                
            if (isCurrentPlayerPiece) {
                gameBoard.setSelectedPiece(clickedPiece);
                gameBoard.highlightValidMoves();
                gameBoard.updateTurnIndicator();
            }
        }
    }
    
    /**
     * Handles clicks when no piece is selected.
     * 
     * @param occupant The occupant of the clicked cell
     */
    private void handlePieceSelection(GameEntity occupant) {
        if (occupant != null && occupant instanceof Player) {
            Player clickedPiece = (Player) occupant;
            boolean isCurrentPlayerPiece = isCurrentPlayerPiece(clickedPiece);
                
            if (isCurrentPlayerPiece) {
                gameBoard.setSelectedPiece(clickedPiece);
                gameBoard.highlightValidMoves();
                gameBoard.updateTurnIndicator();
            }
        }
    }

    /**
     * Handles clicks during the build phase.
     */
    private void handleBuildPhase() {
        Player selectedPiece = gameBoard.getSelectedPiece();
        if (selectedPiece != null) {
            gameBoard.build(selectedPiece.getRow(), selectedPiece.getCol(), row, col);
        }
    }
    
    /**
     * Checks if a piece belongs to the current player.
     * 
     * @param piece The piece to check
     * @return true if the piece belongs to the current player, false otherwise
     */
    private boolean isCurrentPlayerPiece(Player piece) {
        return gameBoard.getPlayerPieces(gameBoard.getCurrentPlayer()).contains(piece);
    }
} 