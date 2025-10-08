package brick;

import gobj.*;

import javax.swing.ImageIcon;

public abstract class Brick extends GameObject {

    protected int hp;
    protected int id;
    private float dropChance;       // TODO: Tạo powerup tại vị trí brick 
    protected boolean isDestroyed;
    protected int iFrames = 0;
    /**
     * Brick instance's constructor.
     * 
     * @param x             x Coordinate
     * @param y             y Coordinate
     * @param w             Object's width
     * @param h             Object's height
     * @param hp            Object's hit points
     * @param dropChance    Object's PowerUp drop chance
     * @param id            Object's ID    
     */
    public Brick(float x, float y, float w, float h, int hp, float dropChance, int id) { 
        super(x, y, w, h, "");
        this.hp = hp;
        this.id = id;
        this.dropChance = dropChance;
        this.isDestroyed = false;
        updateTexture();
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public int getBrickId() {
        return id;
    }

    public void setInvulnerable(int frames) {
        this.iFrames = frames;
    }
    
    public boolean isInvulnerable() {
        return iFrames > 0;
    }

    /**
     * Handling damage taken.
     * 
     * @param amount      amount of damage dealt
     */
    public void takeDamage(int amount) {
        if (isInvulnerable()) return;

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
        if (isInvulnerable()) iFrames--;
        if (isDestroyed) return;
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