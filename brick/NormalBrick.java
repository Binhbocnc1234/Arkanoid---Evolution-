package brick;

import info.GameInfo;

public class NormalBrick extends Brick {
    public static final int ID = 1;

    public NormalBrick(float x, float y, float w, float h) {
        super(x, y, w, h, 1, 0.05f, ID);
    }

    @Override
    public void selfDestroy() {
        super.selfDestroy();
        int count = 3 + (int) (Math.random() * 3);
        System.out.println("Creating " + count + " brick particles...");

        for (int i = 0; i < count; i++) {
                BrickParticle particle = new BrickParticle(x, y);
                GameInfo.getInstance().addGameObject(particle);
                System.out.println("Particle created at (" + x + ", " + y + ")");
        }
    }
}
