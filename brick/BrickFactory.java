package brick;

public class BrickFactory {
    public static Brick createBrick(int id, float x, float y, float w, float h) {
        switch (id) {
            /*
             * ID 1     Normal
             * ID 2     Steel
             * ID 3     Gold
             * ID 4     Diamond
             * ID 5     Obsidian
             * ID 6     TNT
             */
            case NormalBrick.ID:
                return new NormalBrick(x, y, w, h);
            case SteelBrick.ID:
                return new SteelBrick(x, y, w, h);
            case GoldBrick.ID:
                return new GoldBrick(x, y, w, h);
            case DiamondBrick.ID:
                return new DiamondBrick(x, y, w, h);
            case ObsidianBrick.ID:
                return new ObsidianBrick(x, y, w, h);
            case TNTBrick.ID:
                return new TNTBrick(x, y, w, h);
            default: 
                return null;
        }
    }
}
