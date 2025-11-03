package brick;

import gobj.*;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class BrickParticle extends MovableObject {
    private float size;
    private float aniDur;
    private boolean isDead;

    public BrickParticle(float x, float y) {
        super(x, y, 10, 10, "particle.png");

        /* Randomize particle's attributes. */

        this.size = 5 + (float) Math.random() * 5;           // size 5px -> 10px

            /* Calculating flying path */
        double angle = Math.random() * 2 * Math.PI;         // angle 0 -> 2pi
        double speed = 3f + (float) Math.random() * 7;      // speed 3f -> 10f

        this.dx = (float) (Math.cos(angle) * speed);
        this.dy = (float) (Math.sin(angle) * speed);

        this.aniDur = 30 + (float) Math.random() * 40;        // 30->40 frames

        this.isDead = false;
    }

    @Override
    public boolean isDie() {
        return isDead;
    }
    
    @Override
    public void update() {
        move();
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

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f ));
        g.drawImage(image, (int)(x - size / 2f), (int)(y - size / 2f), (int)size, (int)size, null);
    }
}
