package brick;

public class NormalBrick extends Brick {
    public static final int ID = 1;

    public NormalBrick(float x, float y, float w, float h) {
        super(x, y, w, h, 1, 0.25f, ID,100);
    }
}
