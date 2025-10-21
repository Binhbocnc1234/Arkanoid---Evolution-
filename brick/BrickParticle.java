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

        this.size = 30 + (float) Math.random() * 10;          // size 5px -> 10px

            /* Calculating flying path */
        double angle = Math.random() * 2 * Math.PI;         // angle 0 -> 2pi
        double speed = 0f;    // speed 3f -> 5f

        this.dx = (float) (Math.cos(angle) * speed);
        this.dy = (float) (Math.sin(angle) * speed);

        this.aniDur = 60f;      // 10->20 frames

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
    public void render(Graphics g) {
        if (isDead) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f ));
        g.drawImage(image, (int)(x - size / 2f), (int)(y - size / 2f), (int)size, (int)size, null);
    }
}
