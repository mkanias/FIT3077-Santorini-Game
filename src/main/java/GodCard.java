/**
 * Interface defining the contract for God Card abilities.
 * Each God Card implementation will provide specific behavior modifications
 * for game actions like moving and building.
 */
public interface GodCard {
    /**
     * Gets the name of the God Card.
     * @return The name of the God
     */
    String getName();

    /**
     * Gets the description of the God's power.
     * @return The description of the power
     */
    String getDescription();

    /**
     * Checks if a move is valid according to this God's rules.
     * @param boardLogic The current board logic instance
     * @param fromRow Starting row
     * @param fromCol Starting column
     * @param toRow Target row
     * @param toCol Target column
     * @return true if the move is valid under this God's rules
     */
    boolean isValidMove(BoardLogic boardLogic, int fromRow, int fromCol, int toRow, int toCol);

    /**
     * Checks if a build is valid according to this God's rules.
     * @param boardLogic The current board logic instance
     * @param workerRow Worker's row position
     * @param workerCol Worker's column position
     * @param buildRow Build target row
     * @param buildCol Build target column
     * @return true if the build is valid under this God's rules
     */
    boolean isValidBuild(BoardLogic boardLogic, int workerRow, int workerCol, int buildRow, int buildCol);

    /**
     * Performs any special actions before a move.
     * @param boardLogic The current board logic instance
     * @param fromRow Starting row
     * @param fromCol Starting column
     * @param toRow Target row
     * @param toCol Target column
     */
    default void beforeMove(BoardLogic boardLogic, int fromRow, int fromCol, int toRow, int toCol) {}

    /**
     * Performs any special actions after a move.
     * @param boardLogic The current board logic instance
     * @param fromRow Starting row
     * @param fromCol Starting column
     * @param toRow Target row
     * @param toCol Target column
     */
    default void afterMove(BoardLogic boardLogic, int fromRow, int fromCol, int toRow, int toCol) {}

    /**
     * Performs any special actions before a build.
     * @param boardLogic The current board logic instance
     * @param workerRow Worker's row position
     * @param workerCol Worker's column position
     * @param buildRow Build target row
     * @param buildCol Build target column
     */
    default void beforeBuild(BoardLogic boardLogic, int workerRow, int workerCol, int buildRow, int buildCol) {}

    /**
     * Performs any special actions after a build.
     * @param boardLogic The current board logic instance
     * @param workerRow Worker's row position
     * @param workerCol Worker's column position
     * @param buildRow Build target row
     * @param buildCol Build target column
     */
    default void afterBuild(BoardLogic boardLogic, int workerRow, int workerCol, int buildRow, int buildCol) {}
} 