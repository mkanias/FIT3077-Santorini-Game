import java.util.List;

/**
 * Manages turn-related operations in the game.
 * This class handles turn progression and player turns.
 */
public class TurnManager {
    private int currentPlayerIndex;
    private final List<Player> players;
    private boolean isPlacementPhase;
    private int currentPhase; // 0: move, 1: build
    
    /**
     * Creates a new turn manager.
     * 
     * @param players The list of players
     */
    public TurnManager(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
        this.isPlacementPhase = true;
        this.currentPhase = 0;
    }
    
    /**
     * Gets the current player.
     * 
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Advances to the next player's turn.
     */
    public void nextTurn() {
        if (isPlacementPhase) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            if (currentPhase == 1) { // After build phase
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                currentPhase = 0; // Reset to move phase
            } else {
                currentPhase = 1; // Move to build phase
            }
        }
    }
    
    /**
     * Checks if the game is in the placement phase.
     * 
     * @return true if in placement phase, false otherwise
     */
    public boolean isPlacementPhase() {
        return isPlacementPhase;
    }
    
    /**
     * Sets the game phase to the main game phase.
     */
    public void startMainGame() {
        isPlacementPhase = false;
        currentPhase = 0;
    }
    
    /**
     * Gets the current phase (0 for move, 1 for build).
     * 
     * @return The current phase
     */
    public int getCurrentPhase() {
        return currentPhase;
    }
    
    /**
     * Gets the index of the current player.
     * 
     * @return The current player index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
} 