package brick;

import gobj.GameObject;
import info.GameInfo;
import java.util.List;
import javax.swing.ImageIcon;

public class TNTBrick extends Brick {
    public static final int ID = 6;

    public TNTBrick(float x, float y, float w, float h) {
        super(x, y, w, h, 1, 0f, ID);
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
    public void onDestroyed() {
        List<GameObject> objects = GameInfo.getInstance().getObjects();

        for (GameObject obj : objects) {
            if (obj instanceof Brick brick) {
                boolean sameRow = (brick.getY() == this.getY());
                boolean sameCol = (brick.getX() == this.getX());

                if ((sameCol || sameRow) && (brick != this) && !brick.isDestroyed()) {
                    brick.takeDamage(1);
                } 
            }
        }
    }
}
