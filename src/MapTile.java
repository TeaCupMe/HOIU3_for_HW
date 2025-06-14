import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MapTile  {

    static final ArrayList<Integer> walkableTiles = new ArrayList<>(Arrays.asList(0, 8, 9));
    Map<Integer, String> tileTypeToDescription = Map.of(
            0, "Empty land",
            1, "Obstruction type 1",
            2, "Obstruction type 2",
            3, "Obstruction type 3",
            8, "Graveyard",
            9, "Road"
    );

    String representation;
    int type;
    String[] decorations = new String[2];

    MapTile(String representation) {
        this.representation = representation;
        decorations[0] = "";
        decorations[1] = "";
    }

    MapTile(int type) {
        this.type = type;
        this.representation = UI.mapLookUpTable.get(type);
        decorations[0] = "";
        decorations[1] = "";
    }

    public String toString() {
        return decorations[0] + representation + decorations[1];
    }

    public String description() {
        return tileTypeToDescription.get(type);
    }

    public boolean isWalkable() {
        return walkableTiles.contains(type);
    }
}
