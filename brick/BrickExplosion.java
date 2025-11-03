package brick;

import java.awt.Graphics;

import gobj.GameObject;
import info.GameInfo;

public class BrickExplosion extends GameObject {
    private float sizeW;
    private float sizeH;

    private float aniDur;
    private boolean isDead;

    public BrickExplosion(float x, float y) {
        super(x, y, 160, 40, "explosion.png");
        this.sizeW = GameInfo.CAMPAIGN_WIDTH / 10;
        this.sizeH = 25;

        this.aniDur = 10f;
        this.isDead = false;
    }

    @Override
    public boolean isDie() {
        return isDead;
    }
    
    @Override
    public void update() {
        aniDur--;

        if (aniDur <= 0) {
            isDead = true;
        }
    }

    @Override
    public void selfDestroy() {
        super.selfDestroy();
        isDead = true;
    }

    @Override
    public void render(Graphics g) {
        if (isDead) return;

        g.drawImage(image, (int) (x - sizeW/2), (int) (y - sizeH/2), (int) sizeW, (int) sizeH, null);
    }

}
