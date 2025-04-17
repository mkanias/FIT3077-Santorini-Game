/**
 * Demeter God Card implementation.
 * Demeter's power: Your worker may build one additional time, but not on the same space.
 */
public class DemeterGodCard extends BaseGodCard {
    private boolean hasBuiltOnce;
    private int lastBuildRow;
    private int lastBuildCol;

    public DemeterGodCard() {
        super("Demeter",
              "Your worker may build one additional time, but not on the same space.");
        resetTurnState();
    }

    @Override
    public boolean isValidBuild(BoardLogic boardLogic, int workerRow, int workerCol, int buildRow, int buildCol) {
        // If this is the second build, check that it's not on the same space
        if (hasBuiltOnce && buildRow == lastBuildRow && buildCol == lastBuildCol) {
            return false;
        }

        return super.isValidBuild(boardLogic, workerRow, workerCol, buildRow, buildCol);
    }

    @Override
    public void afterBuild(BoardLogic boardLogic, int workerRow, int workerCol, int buildRow, int buildCol) {
        if (!hasBuiltOnce) {
            // First build - record the location and offer a second build
            hasBuiltOnce = true;
            lastBuildRow = buildRow;
            lastBuildCol = buildCol;
            
            // Don't end the build phase yet
            boardLogic.getGameState().setInBuildPhase(true);
        } else {
            // Second build - reset state and proceed normally
            resetTurnState();
        }
    }

    /**
     * Resets the turn state when the turn ends.
     */
    private void resetTurnState() {
        hasBuiltOnce = false;
        lastBuildRow = -1;
        lastBuildCol = -1;
    }
} 