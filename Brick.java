import javax.swing.ImageIcon;

import gobj.*;
import info.GameInfo;

// Temporary brick class
// TODO: Subclasses for other types of bricks.
public class Brick extends GameObject {

    private int hp;
    // private float dropChance;       // TODO
    private boolean destroyed;
    private boolean canCollide;

    /**
     * Brick instance's constructor.
     * 
     * @param x             x Coordinate
     * @param y             y Coordinate
     * @param w             Object's width
     * @param h             Object's height
     * @param hp            Object's hit points
     * @param dropChance    Object's Power-up drop chance
     * @param imagePath     Texture
     */
    public Brick(float x, float y, float w, float h, int hp) {    //TODO: them tham chieu dropchance 
        super(x, y, w, h, "");
        this.hp = hp;
        // this.dropChance = dropChance;
        this.destroyed = false;
        this.canCollide = true;
        updateTexture();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Debug.
     * @param windowHeight
     * @param windowWidth
     */
    public static void debug(float startX, float startY, float brickW, float brickH, float gap) {
        int[] hpList = {1, 2, 3, 4, Integer.MAX_VALUE}; // In accordance to Normal, Steel, Gold, Diamond and Obsidian.
        float x = startX;

        for (int hp : hpList) {
            Brick brick = new Brick(x, startY, brickW, brickH, hp);
            GameInfo.getInstance().getObjects().add(brick);
            x += brickW + gap;
        }
    }

    /**
     * Check collision with Ball object.
     * 
     * @param ball      Ball instance
     */
    private void collide(Ball ball) {
        if (!canCollide) { return; }        // Once collided, turn off collision check for this Brick.

        if (hp < Integer.MAX_VALUE) {       // Obsidian -> indestructable
            hp--;
            updateTexture();
        }


        // Ball collision check and redirection.

        float left   = this.x - this.width / 2f;
        float right  = this.x + this.width / 2f;
        float top    = this.y - this.height / 2f;
        float bottom = this.y + this.height / 2f;

        float ballX = ball.getX();
        float ballY = ball.getY();
        float ballR = ball.getWidth() / 2f;

            // Ball is approaching either left or right side.
        if (ballX < left || ballX > right) {        
            ball.setVelocity(-ball.getDx(), ball.getDy());

                // Push ball outside of Brick, to avoid additional collision.
            if (ballX < left) {        
                ball.setPosition(left - ballR - 1f, ballY);
            } else {
                ball.setPosition(right + ballR + 1f, ballY);
            }
        } else {        // Ball is approaching from either up or down.
            ball.setVelocity(ball.getDx(), -ball.getDy());

                // Push ball outside of Brick, to avoid additional collision.
            if (ballY < top) {
                ball.setPosition(ballX, top - ballR - 1f);
            } else {
                ball.setPosition(ballX, bottom + ballR + 1f);
            }
        }

        canCollide = false;

        if (hp <= 0) {
            destroyed = true;
        }
    }

    /**
     * Override function for the updating of the Brick instance.
     */
    @Override
    public void update() {
        if (destroyed) { return; }

        for (GameObject obj : GameInfo.getInstance().getObjects()) {
            if (obj instanceof Ball) {
                Ball ball = (Ball) obj;
                if (this.isCollide(ball)) {
                    collide(ball);
                } else {
                    canCollide = true;      // Reset collision once ball left the Brick
                }
            }
        }
    }

    /**
     * Update the brick's hp in accordance to it's remaining HP.
     */
    private void updateTexture() {
        String textureName = null;

        if (hp == Integer.MAX_VALUE) {
            textureName = "brick_obsidian.png";
        } else {
            switch (hp) {
                case 4: textureName = "brick_diamond.png"; break;
                case 3: textureName = "brick_gold.png"; break;
                case 2: textureName = "brick_steel.png"; break;
                case 1: textureName = "brick_normal.png"; break;
                default: textureName = null; 
            }
        }

        if (textureName != null) {
            this.image = new ImageIcon("assets/img/brick/" + textureName).getImage();
        } else {
            this.image = null;
        }
    }
}