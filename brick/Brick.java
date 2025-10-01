package brick;

import gobj.*;
import javax.swing.ImageIcon;

public abstract class Brick extends GameObject {

    protected int hp;
    protected int id;
    // private float dropChance;       // TODO
    protected boolean isDestroyed;
    protected boolean canCollide;

    /**
     * Brick instance's constructor.
     * 
     * @param x             x Coordinate
     * @param y             y Coordinate
     * @param w             Object's width
     * @param h             Object's height
     * @param hp            Object's hit points
     * @param id            Object's ID    
     */
    public Brick(float x, float y, float w, float h, int hp, int id) {    //TODO: them tham chieu dropchance 
        super(x, y, w, h, "");
        this.hp = hp;
        this.id = id;
        // this.dropChance = dropChance;
        this.isDestroyed = false;
        this.canCollide = true;
        updateTexture();
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public int getBrickId() {
        return id;
    }

    /**
     * Handling damage taken.
     * 
     * @param amount      amount of damage dealt
     */
    public void takeDamage(int amount) {
        if (hp < Integer.MAX_VALUE) {       // Obsidian -> indestructable
            hp-= amount;
            updateTexture();
        }

        if (hp <= 0) {
            isDestroyed = true;
            onDestroyed();
            // Tô đức anh viết
            // cam on thay to
        }
    }

    /**
     * Special effects upon destroying the Brick instance.
     * Only serves for TNT Brick for now.
     */
    public void onDestroyed() {};

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
    protected void updateTexture() {
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