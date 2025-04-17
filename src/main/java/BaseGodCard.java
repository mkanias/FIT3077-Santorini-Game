/**
 * Base implementation of GodCard that provides default behavior.
 * Most God Cards can extend this class and override only the methods they need to modify.
 */
public class BaseGodCard implements GodCard {
    private final String name;
    private final String description;

    public BaseGodCard(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isValidMove(BoardLogic boardLogic, int fromRow, int fromCol, int toRow, int toCol) {
        // Default behavior delegates to standard game rules
        return boardLogic.isValidMove(fromRow, fromCol, toRow, toCol);
    }

    @Override
    public boolean isValidBuild(BoardLogic boardLogic, int workerRow, int workerCol, int buildRow, int buildCol) {
        // Default behavior delegates to standard game rules
        return boardLogic.isValidBuild(workerRow, workerCol, buildRow, buildCol);
    }
} 