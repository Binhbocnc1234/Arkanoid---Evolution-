package brick;

public class ObsidianBrick extends Brick {
    public static final int ID = 5;

    public ObsidianBrick(float x, float y, float w, float h) {
        super(x, y, w, h, Integer.MAX_VALUE, 0f, ID, 0);
    }

    @Override
    public void selfDestroy() {
        super.selfDestroy();
        // avoid these fuckers exploding into thousand pieces upon changing level
    }
}
