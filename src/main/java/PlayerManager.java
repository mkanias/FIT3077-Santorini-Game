import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages players and their pieces in the game.
 * This class handles player creation, piece management, and player state.
 */
public class PlayerManager {
    private final List<Player> players;
    private final Map<Player, List<Player>> playerPieces;
    private final Map<Player, Integer> piecesPlaced;
    private final int piecesPerPlayer;
    
    /**
     * Creates a new player manager.
     * 
     * @param numPlayers The number of players in the game
     * @param piecesPerPlayer The number of pieces each player has
     */
    public PlayerManager(int numPlayers, int piecesPerPlayer) {
        this.players = new ArrayList<>();
        this.playerPieces = new HashMap<>();
        this.piecesPlaced = new HashMap<>();
        this.piecesPerPlayer = piecesPerPlayer;
        
        initializePlayers(numPlayers);
    }
    
    /**
     * Initializes the players and their pieces.
     * 
     * @param numPlayers The number of players to create
     */
    private void initializePlayers(int numPlayers) {
        Color[] playerColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN};
        for (int i = 0; i < numPlayers; i++) {
            Color playerColor = playerColors[i % playerColors.length];
            Player player = new Player(playerColor, "Player " + (i + 1));
            players.add(player);
            
            // Create pieces for this player
            List<Player> playerPieceList = new ArrayList<>();
            for (int j = 0; j < piecesPerPlayer; j++) {
                Player piece = new Player(playerColor, "Player " + (i + 1) + " Piece " + (j + 1));
                playerPieceList.add(piece);
            }
            playerPieces.put(player, playerPieceList);
            piecesPlaced.put(player, 0);
        }
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
    
    /**
     * Increments the number of pieces placed by a player.
     * @param player The player to increment pieces for
     * @return true if all pieces for this player have been placed, false otherwise
     */
    public boolean incrementPiecesPlaced(Player player) {
        int currentPieces = piecesPlaced.get(player) + 1;
        piecesPlaced.put(player, currentPieces);
        return currentPieces == piecesPerPlayer;
    }
    
    /**
     * Gets the number of pieces each player has.
     * @return The number of pieces per player
     */
    public int getPiecesPerPlayer() {
        return piecesPerPlayer;
    }
} 