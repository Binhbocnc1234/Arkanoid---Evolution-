package brick;

import gobj.GameObject;
import info.GameInfo;
import javax.swing.ImageIcon;

public class TNTBrick extends Brick {
    public static final int ID = 6;

    public TNTBrick(float x, float y, float w, float h) {
        super(x, y, w, h, 1, 0f, ID, 100);
        updateTexture();
    }

    @Override
    public void updateTexture() {
        if (!isDestroyed()) {
            this.image = new ImageIcon("assets/img/brick/brick_tnt.png").getImage();
        } else {
            this.image = null;
        }

    }

    @Override
    public void selfDestroy() {
        super.selfDestroy();

        for (GameObject obj : GameInfo.getInstance().getCurrentObjects()) {
            if (obj instanceof Brick brick) {
                boolean sameRow = (brick.getY() == this.getY());
                boolean sameCol = (brick.getX() == this.getX());

                if ((sameCol || sameRow) && (brick != this) && !brick.isDestroyed()) {
                    brick.takeDamage(1);
                    GameInfo.getInstance().addGameObject(new BrickExplosion(brick.getX(), brick.getY()));
                } 
            }
        }
    }
}
