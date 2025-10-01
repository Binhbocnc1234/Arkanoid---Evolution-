package brick;
import gobj.*;
import info.GameInfo;
import javax.swing.ImageIcon;

// Temporary brick class
// TODO: Subclasses for other types of bricks.
public class Brick extends GameObject {

    private int hp;
    // private float dropChance;       // TODO
    private boolean isDestroyed;
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
        this.isDestroyed = false;
        this.canCollide = true;
        updateTexture();
    }

    public boolean isDestroyed() {
        return isDestroyed;
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
    public void takeDamage(int amount) {
        if (hp < Integer.MAX_VALUE) {       // Obsidian -> indestructable
            hp-= amount;
            updateTexture();
        }

        if (hp <= 0) {
            isDestroyed = true;
            // Tô đức anh viết 
            
        }
    }

    /**
     * Override function for the updating of the Brick instance.
     */
    @Override
    public void update() {
        if (isDestroyed) { return; }
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