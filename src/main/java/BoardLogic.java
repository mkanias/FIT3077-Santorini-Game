import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the game logic for the grid game board.
 * This class is responsible for managing game rules, piece movement, and building.
 */
public class BoardLogic {
    private final GridCell[][] cells;
    private final GameConfig config;
    private final GameState gameState;
    private final List<GameEntity> entities;
    private final List<Player> players;
    private final Map<Player, List<Player>> playerPieces;
    private final Map<Player, Integer> piecesPlaced;
    
    /**
     * Creates a new board logic manager.
     * 
     * @param config The game configuration
     * @param cells The grid cells
     * @param gameState The game state
     */
    public BoardLogic(GameConfig config, GridCell[][] cells, GameState gameState) {
        this.config = config;
        this.cells = cells;
        this.gameState = gameState;
        this.entities = new ArrayList<>();
        this.players = new ArrayList<>();
        this.playerPieces = new HashMap<>();
        this.piecesPlaced = new HashMap<>();
        
        // Initialize players
        initializePlayers();
    }
    
    /**
     * Initializes the players and their pieces.
     */
    private void initializePlayers() {
        java.awt.Color[] playerColors = {java.awt.Color.RED, java.awt.Color.BLUE, java.awt.Color.GREEN, 
                                         java.awt.Color.YELLOW, java.awt.Color.MAGENTA, java.awt.Color.CYAN};
        for (int i = 0; i < config.getNumPlayers(); i++) {
            java.awt.Color playerColor = playerColors[i % playerColors.length];
            Player player = new Player(playerColor, "Player " + (i + 1));
            players.add(player);
            
            // Create pieces for this player
            List<Player> playerPieceList = new ArrayList<>();
            for (int j = 0; j < config.getPiecesPerPlayer(); j++) {
                Player piece = new Player(playerColor, "Player " + (i + 1) + " Piece " + (j + 1));
                playerPieceList.add(piece);
            }
            playerPieces.put(player, playerPieceList);
            piecesPlaced.put(player, 0);
        }
        
        // Set the first player as current
        gameState.setCurrentPlayer(players.get(0));
    }
    
    /**
     * Adds an entity to the board.
     * 
     * @param entity The entity to add
     * @param row The row position
     * @param col The column position
     * @return true if the entity was added successfully, false otherwise
     */
    public boolean addEntity(GameEntity entity, int row, int col) {
        if (isValidPosition(row, col) && cells[row][col].getOccupant() == null) {
            // Place the entity on the board
            cells[row][col].setOccupant(entity);
            entity.setPosition(row, col);
            entities.add(entity);
            
            // Update placement state
            updatePlacementState();
            
            return true;
        }
        return false;
    }
    
    /**
     * Updates the placement state after a piece is placed.
     */
    private void updatePlacementState() {
        Player currentPlayer = players.get(gameState.getCurrentPlayerPlacementIndex());
        int piecesPlaced = this.piecesPlaced.get(currentPlayer) + 1;
        this.piecesPlaced.put(currentPlayer, piecesPlaced);
        
        // If all pieces for this player are placed, move to the next player
        if (piecesPlaced == config.getPiecesPerPlayer()) {
            gameState.incrementCurrentPlayerPlacementIndex();
            
            // If all players have placed their pieces, start the game
            if (gameState.getCurrentPlayerPlacementIndex() >= players.size()) {
                startGame();
            }
        }
    }
    
    /**
     * Starts the game after all pieces have been placed.
     */
    public void startGame() {
        gameState.startGame(players, config.getMovesPerTurn());
    }
    
    /**
     * Moves an entity on the board.
     * 
     * @param entity The entity to move
     * @param toRow The target row
     * @param toCol The target column
     * @return true if the entity was moved successfully, false otherwise
     */
    public boolean moveEntity(GameEntity entity, int toRow, int toCol) {
        if (entity == null || !isValidMove(entity.getRow(), entity.getCol(), toRow, toCol)) {
            return false;
        }

        // Get the source and destination cells
        GridCell sourceCell = getCell(entity.getRow(), entity.getCol());
        GridCell destCell = getCell(toRow, toCol);

        // Check for winning condition - moving from lower level to level 3
        if (sourceCell.getBuildingLevel() < 3 && destCell.getBuildingLevel() == 3) {
            // Move the entity first
            sourceCell.setOccupant(null);
            destCell.setOccupant(entity);
            entity.setPosition(toRow, toCol);
            
            // End the game with the current player as winner
            gameState.endGame(gameState.getCurrentPlayer());
            return true;
        }

        // Move the entity
        sourceCell.setOccupant(null);
        destCell.setOccupant(entity);
        entity.setPosition(toRow, toCol);

        // Enter build phase after successful move (only if game is not over)
        if (!gameState.isGameOver()) {
            gameState.setInBuildPhase(true);
        }
        
        return true;
    }
    
    /**
     * Checks if a move is valid.
     * 
     * @param fromRow The starting row
     * @param fromCol The starting column
     * @param toRow The target row
     * @param toCol The target column
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidPosition(toRow, toCol)) return false;
        
        // Check if target cell is occupied or has a dome
        GridCell targetCell = cells[toRow][toCol];
        if (targetCell.getOccupant() != null || targetCell.hasDome()) return false;
        
        // Check if the move is within one cell in any direction
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);
        
        // Get source and target cell heights
        int sourceHeight = cells[fromRow][fromCol].getBuildingLevel();
        int targetHeight = targetCell.getBuildingLevel();
        
        // Valid move is one step in any direction (including diagonally)
        // but not staying in the same place, and can only move up one level
        return (rowDiff <= 1 && colDiff <= 1) && !(rowDiff == 0 && colDiff == 0) 
               && (targetHeight <= sourceHeight + 1);
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
        
        // Exit build phase and switch turns
        gameState.setInBuildPhase(false);
        gameState.switchToNextPlayer(players, config.getMovesPerTurn());
        
        return true;
    }
    
    /**
     * Checks if a position is within the bounds of the grid.
     * @param row The row position
     * @param col The column position
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < config.getGridSize() && col >= 0 && col < config.getGridSize();
    }
    
    /**
     * Gets the cell at the specified position.
     * @param row The row position
     * @param col The column position
     * @return The cell at the specified position
     */
    public GridCell getCell(int row, int col) {
        return cells[row][col];
    }
    
    /**
     * Gets the list of all players.
     * @return The list of players
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * Gets the pieces for a specific player.
     * @param player The player to get pieces for
     * @return The list of pieces for the player
     */
    public List<Player> getPlayerPieces(Player player) {
        return playerPieces.get(player);
    }
    
    /**
     * Gets the number of pieces placed by a specific player.
     * @param player The player to check
     * @return The number of pieces placed by the player
     */
    public int getPiecesPlaced(Player player) {
        return piecesPlaced.getOrDefault(player, 0);
    }
} 