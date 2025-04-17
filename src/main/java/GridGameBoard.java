import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Main game board class that coordinates the game logic, UI, and highlighting components.
 */
public class GridGameBoard implements GameBoard {
    private final BoardUI boardUI;
    private final BoardLogic boardLogic;
    private final BoardHighlighting boardHighlighting;
    private final GameConfig config;
    private final GameState gameState;
    private final JPanel boardPanel;
    private final JPanel turnIndicator;
    private boolean gameStarted;
    private int currentPlayerIndex;
    
    /**
     * Creates a new game board with the specified configuration.
     * 
     * @param config The game configuration
     */
    public GridGameBoard(GameConfig config) {
        this.config = config;
        this.gameStarted = false;
        this.currentPlayerIndex = 0;
        
        // Initialize UI components
        this.boardPanel = new JPanel(new GridLayout(config.getGridSize(), config.getGridSize()));
        this.boardPanel.setBorder(new LineBorder(Color.BLACK, 2));
        this.turnIndicator = new JPanel();
        
        // Initialize components
        GridCell[][] cells = new GridCell[config.getGridSize()][config.getGridSize()];
        this.gameState = new GameState();
        this.boardUI = new BoardUI(config, cells);
        this.boardLogic = new BoardLogic(config, cells, gameState);
        this.boardHighlighting = new BoardHighlighting(config, cells);
        
        // Initialize UI
        boardUI.initialize(this);
    }

    @Override
    public void initialize() {
        // Create grid cells and add click listeners
        for (int i = 0; i < config.getGridSize(); i++) {
            for (int j = 0; j < config.getGridSize(); j++) {
                CellClickListener clickListener = new CellClickListener(this, i, j);
                GridCell cell = boardLogic.getCell(i, j);
                cell.getVisualComponent().addMouseListener(clickListener);
            }
        }
        
        // Update the turn indicator
        updateTurnIndicator();
    }

    /**
     * Handles a cell click at the specified position.
     * 
     * @param row The row position
     * @param col The column position
     */
    public void handleCellClick(int row, int col) {
        if (!gameStarted) {
            handlePlacementPhase(row, col);
        } else {
            handleGamePhase(row, col);
        }
    }
    
    /**
     * Handles cell clicks during the placement phase.
     * 
     * @param row The row position
     * @param col The column position
     */
    private void handlePlacementPhase(int row, int col) {
        Player currentPlayer = getPlayers().get(getCurrentPlayerPlacementIndex());
        List<Player> playerPieces = getPlayerPieces(currentPlayer);
        int piecesPlaced = getPiecesPlaced(currentPlayer);
        
        if (piecesPlaced < playerPieces.size()) {
            Player pieceToPlace = playerPieces.get(piecesPlaced);
            addEntity(pieceToPlace, row, col);
        }
    }
    
    /**
     * Handles cell clicks during the game phase.
     * 
     * @param row The row position
     * @param col The column position
     */
    private void handleGamePhase(int row, int col) {
        // If game is over, ignore all clicks and clear highlights
        if (gameState.isGameOver()) {
            clearHighlights();
            return;
        }

        GridCell clickedCell = getCell(row, col);
        GameEntity occupant = clickedCell.getOccupant();
        
        if (isInBuildPhase()) {
            handleBuildPhase(row, col);
        } else if (getSelectedPiece() != null) {
            handleSelectedPieceClick(row, col, occupant);
        } else {
            handlePieceSelection(row, col, occupant);
        }

        // Check if game is over after the move
        if (gameState.isGameOver()) {
            clearHighlights();
            updateTurnIndicator(); // This will show the winner
        }
    }
    
    /**
     * Handles clicks when a piece is already selected.
     * 
     * @param row The row position
     * @param col The column position
     * @param occupant The occupant of the clicked cell
     */
    private void handleSelectedPieceClick(int row, int col, GameEntity occupant) {
        Player selectedPiece = getSelectedPiece();
        int fromRow = selectedPiece.getRow();
        int fromCol = selectedPiece.getCol();
        
        // Check if we're clicking on a valid move destination
        if (isValidMove(fromRow, fromCol, row, col)) {
            moveEntity(selectedPiece, row, col);
        } 
        // Check if we're clicking on another piece of the current player
        else if (occupant != null && occupant instanceof Player) {
            Player clickedPiece = (Player) occupant;
            boolean isCurrentPlayerPiece = isCurrentPlayerPiece(clickedPiece);
                
            if (isCurrentPlayerPiece) {
                clearHighlights(); // Clear previous highlights before selecting new piece
                setSelectedPiece(clickedPiece);
                highlightValidMoves();
                updateTurnIndicator();
            } else {
                // If clicking on opponent's piece, deselect and clear highlights
                clearHighlights();
                setSelectedPiece(null);
                updateTurnIndicator();
            }
        } else {
            // If clicking on an empty invalid cell, deselect and clear highlights
            clearHighlights();
            setSelectedPiece(null);
            updateTurnIndicator();
        }
    }
    
    /**
     * Handles clicks when no piece is selected.
     * 
     * @param row The row position
     * @param col The column position
     * @param occupant The occupant of the clicked cell
     */
    private void handlePieceSelection(int row, int col, GameEntity occupant) {
        if (occupant != null && occupant instanceof Player) {
            Player clickedPiece = (Player) occupant;
            boolean isCurrentPlayerPiece = isCurrentPlayerPiece(clickedPiece);
                
            if (isCurrentPlayerPiece) {
                setSelectedPiece(clickedPiece);
                highlightValidMoves();
                updateTurnIndicator();
            }
        }
    }

    /**
     * Handles clicks during the build phase.
     * 
     * @param row The row position
     * @param col The column position
     */
    private void handleBuildPhase(int row, int col) {
        Player selectedPiece = getSelectedPiece();
        if (selectedPiece != null) {
            build(selectedPiece.getRow(), selectedPiece.getCol(), row, col);
        }
    }
    
    /**
     * Checks if a piece belongs to the current player.
     * 
     * @param piece The piece to check
     * @return true if the piece belongs to the current player, false otherwise
     */
    private boolean isCurrentPlayerPiece(Player piece) {
        return getPlayerPieces(getCurrentPlayer()).contains(piece);
    }

    /**
     * Checks if the game has started.
     * @return true if the game has started, false otherwise
     */
    public boolean isGameStarted() {
        return gameState.isGameStarted();
    }

    /**
     * Gets the current player index.
     * @return The index of the current player
     */
    public int getCurrentPlayerIndex() {
        return gameState.getCurrentPlayerIndex();
    }
    
    /**
     * Gets the number of pieces placed by a specific player.
     * @param player The player to check
     * @return The number of pieces placed by the player
     */
    public int getPiecesPlaced(Player player) {
        return boardLogic.getPiecesPlaced(player);
    }
    
    /**
     * Gets the current player placement index.
     * @return The index of the current player in placement phase
     */
    public int getCurrentPlayerPlacementIndex() {
        return gameState.getCurrentPlayerPlacementIndex();
    }

    /**
     * Gets the number of moves remaining for the selected piece.
     * @return The number of moves remaining
     */
    public int getMovesRemaining() {
        return gameState.getMovesRemaining();
    }

    /**
     * Sets the number of moves remaining for the selected piece.
     * @param movesRemaining The number of moves remaining
     */
    public void setMovesRemaining(int movesRemaining) {
        gameState.setMovesRemaining(movesRemaining);
    }

    /**
     * Gets the currently selected piece.
     * @return The selected piece, or null if no piece is selected
     */
    public Player getSelectedPiece() {
        return gameState.getSelectedPiece();
    }

    /**
     * Sets the currently selected piece.
     * @param selectedPiece The piece to select
     */
    public void setSelectedPiece(Player selectedPiece) {
        gameState.setSelectedPiece(selectedPiece);
    }

    /**
     * Starts the game after all pieces have been placed.
     */
    public void startGame() {
        boardLogic.startGame();
        highlightValidMoves();
        updateTurnIndicator();
    }

    @Override
    public void addEntity(GameEntity entity, int row, int col) {
        if (boardLogic.addEntity(entity, row, col)) {
            // Update UI
            updateTurnIndicator();
            highlightValidMoves();
        }
    }
    
    @Override
    public void moveEntity(GameEntity entity, int toRow, int toCol) {
        if (boardLogic.moveEntity(entity, toRow, toCol)) {
            // Clear all existing highlights first
            clearHighlights();
            
            // If game is not over, show build phase highlights
            if (!gameState.isGameOver()) {
                highlightValidBuilds(toRow, toCol);
            }
            
            updateTurnIndicator();
        }
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        return boardLogic.isValidMove(fromRow, fromCol, toRow, toCol);
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
        return boardLogic.isValidBuild(workerRow, workerCol, buildRow, buildCol);
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
        if (boardLogic.build(workerRow, workerCol, buildRow, buildCol)) {
            // Update the display
            clearHighlights();
            updateTurnIndicator();
            return true;
        }
        return false;
    }

    @Override
    public JPanel getBoardPanel() {
        return boardUI.getBoardPanel();
    }

    /**
     * Gets the cell at the specified position.
     * @param row The row position
     * @param col The column position
     * @return The cell at the specified position
     */
    public GridCell getCell(int row, int col) {
        return boardLogic.getCell(row, col);
    }

    @Override
    public Player getCurrentPlayer() {
        return gameState.getCurrentPlayer();
    }

    /**
     * Gets the list of all players.
     * @return The list of players
     */
    public List<Player> getPlayers() {
        return boardLogic.getPlayers();
    }
    
    /**
     * Gets the pieces for a specific player.
     * @param player The player to get pieces for
     * @return The list of pieces for the player
     */
    public List<Player> getPlayerPieces(Player player) {
        return boardLogic.getPlayerPieces(player);
    }

    /**
     * Updates the turn indicator to show whose turn it is or who won.
     */
    public void updateTurnIndicator() {
        if (gameState.isGameOver()) {
            Player winner = gameState.getWinner();
            turnIndicator.setBackground(winner.getColor());
            turnIndicator.setToolTipText(winner.getName() + " wins!");
            
            // Show victory message in the center of the screen
            String message = winner.getName() + " wins!";
            JOptionPane.showMessageDialog(boardPanel, 
                message,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            boardUI.updateTurnIndicator(gameState, getPlayers(), getPiecesPlacedMap());
        }
    }
    
    /**
     * Gets the map of players to their pieces placed count.
     * @return The map of players to pieces placed
     */
    private Map<Player, Integer> getPiecesPlacedMap() {
        Map<Player, Integer> piecesPlacedMap = new java.util.HashMap<>();
        for (Player player : getPlayers()) {
            piecesPlacedMap.put(player, getPiecesPlaced(player));
        }
        return piecesPlacedMap;
    }

    /**
     * Highlights valid moves on the board.
     */
    public void highlightValidMoves() {
        boardHighlighting.highlightValidMoves(gameState, getPlayerPiecesMap());
    }
    
    /**
     * Gets the map of players to their pieces.
     * @return The map of players to their pieces
     */
    private Map<Player, List<Player>> getPlayerPiecesMap() {
        Map<Player, List<Player>> playerPiecesMap = new java.util.HashMap<>();
        for (Player player : getPlayers()) {
            playerPiecesMap.put(player, getPlayerPieces(player));
        }
        return playerPiecesMap;
    }

    @Override
    public JPanel getTurnIndicator() {
        return boardUI.getTurnIndicator();
    }

    /**
     * Gets the number of moves each piece can make per turn.
     * @return The number of moves per turn
     */
    public int getMovesPerTurn() {
        return config.getMovesPerTurn();
    }

    /**
     * Checks if the game is in the build phase.
     * @return true if in build phase, false otherwise
     */
    public boolean isInBuildPhase() {
        return gameState.isInBuildPhase();
    }

    /**
     * Highlights valid build locations for a worker.
     * 
     * @param workerRow The row of the worker
     * @param workerCol The column of the worker
     */
    public void highlightValidBuilds(int workerRow, int workerCol) {
        boardHighlighting.highlightValidBuilds(workerRow, workerCol);
    }

    /**
     * Clears all highlights from the board cells.
     */
    private void clearHighlights() {
        boardHighlighting.clearAllHighlights();
    }
} 