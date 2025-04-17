import java.util.List;

/**
 * Game state class that manages the current state of the game.
 * Encapsulates all state-related variables and operations.
 */
public class GameState {
    private boolean gameStarted;
    private boolean gameOver;  // New field for game over state
    private Player winner;     // New field for winner
    private int currentPlayerIndex;
    private int currentPlayerPlacementIndex;
    private int movesRemaining;
    private Player currentPlayer;
    private Player selectedPiece;
    private boolean inBuildPhase; // Track if we're in the build phase
    
    /**
     * Creates a new game state.
     */
    public GameState() {
        this.gameStarted = false;
        this.gameOver = false;
        this.winner = null;
        this.currentPlayerIndex = 0;
        this.currentPlayerPlacementIndex = 0;
        this.movesRemaining = 0;
        this.currentPlayer = null;
        this.selectedPiece = null;
        this.inBuildPhase = false;
    }
    
    /**
     * Checks if the game has started.
     * @return true if the game has started, false otherwise
     */
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    /**
     * Starts the game.
     * 
     * @param players The list of players
     * @param movesPerTurn The number of moves per turn
     */
    public void startGame(List<Player> players, int movesPerTurn) {
        gameStarted = true;
        movesRemaining = movesPerTurn;
        currentPlayerIndex = 0;
        currentPlayer = players.get(currentPlayerIndex);
        selectedPiece = null;
        inBuildPhase = false;
    }
    
    /**
     * Gets the current player index.
     * @return The index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    
    /**
     * Gets the current player placement index.
     * @return The index of the current player in placement phase
     */
    public int getCurrentPlayerPlacementIndex() {
        return currentPlayerPlacementIndex;
    }
    
    /**
     * Increments the current player placement index.
     */
    public void incrementCurrentPlayerPlacementIndex() {
        currentPlayerPlacementIndex++;
    }
    
    /**
     * Gets the number of moves remaining.
     * @return The number of moves remaining
     */
    public int getMovesRemaining() {
        return movesRemaining;
    }
    
    /**
     * Sets the number of moves remaining.
     * @param movesRemaining The number of moves remaining
     */
    public void setMovesRemaining(int movesRemaining) {
        this.movesRemaining = movesRemaining;
    }
    
    /**
     * Decrements the number of moves remaining.
     */
    public void decrementMovesRemaining() {
        this.movesRemaining--;
    }
    
    /**
     * Gets the current player.
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Sets the current player.
     * @param currentPlayer The current player
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    /**
     * Gets the selected piece.
     * @return The selected piece
     */
    public Player getSelectedPiece() {
        return selectedPiece;
    }
    
    /**
     * Sets the selected piece.
     * @param selectedPiece The selected piece
     */
    public void setSelectedPiece(Player selectedPiece) {
        this.selectedPiece = selectedPiece;
    }
    
    /**
     * Switches to the next player.
     * 
     * @param players The list of players
     * @param movesPerTurn The number of moves per turn
     */
    public void switchToNextPlayer(List<Player> players, int movesPerTurn) {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        movesRemaining = movesPerTurn;
        selectedPiece = null;
        inBuildPhase = false;
    }
    
    /**
     * Checks if we're in the build phase.
     * @return true if in build phase, false otherwise
     */
    public boolean isInBuildPhase() {
        return inBuildPhase;
    }
    
    /**
     * Sets the build phase state.
     * @param inBuildPhase true if entering build phase, false if exiting
     */
    public void setInBuildPhase(boolean inBuildPhase) {
        this.inBuildPhase = inBuildPhase;
    }
    
    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Sets the game over state and winner.
     * @param gameOver true if game is over, false otherwise
     * @param winner the winning player, or null if no winner
     */
    public void setGameOver(boolean gameOver, Player winner) {
        this.gameOver = gameOver;
        this.winner = winner;
    }
    
    /**
     * Gets the winner of the game.
     * @return the winning player, or null if game is not over
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Ends the game and cleans up the game state.
     * This method should be called when a player wins or the game needs to be terminated.
     * It will prevent further moves and ensure all game state variables are properly reset.
     * 
     * @param winner The player who won the game, or null if there's no winner (e.g., in case of a draw or early termination)
     */
    public void endGame(Player winner) {
        this.gameOver = true;
        this.winner = winner;
        this.movesRemaining = 0;
        this.selectedPiece = null;
        this.inBuildPhase = false;
        this.gameStarted = false;
    }
} 