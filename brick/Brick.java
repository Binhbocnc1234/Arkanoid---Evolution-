package brick;

import gobj.*;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;

public abstract class Brick extends GameObject {

    protected int hp;
    protected int id;
    private float dropChance;       // TODO: Tạo powerup tại vị trí brick 
    protected boolean isDestroyed;
    protected boolean isHit;
    protected int iFrames;
    protected int aniTimer;
    public static final int ANI_DURATION = 5;
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
        this.isHit = false;
        this.isDestroyed = false;
        updateTexture();
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public int getBrickId() {
        return id;
    }

    public int getIFrame() {
        return iFrames;
    }

    public void setIFrame(int iFrames) {
        this.iFrames = iFrames;
    }

    public int getHp() {
        return hp;
    }

    /**
     * Check whether the Brick instance is in invulnerable state.
     * @return      a boolean
     */
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
            isHit = true;
            aniTimer = ANI_DURATION;
        }
        
        if (hp <= 0) {
            isDestroyed = true;
            onDestroyed();
            selfDestroy(); // Cường nên sử dụng hàm selfDestroy() để xóa GameObject
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
        /* Update timer for damage animation */
        if (isHit) {
            aniTimer--;
            if (aniTimer <= 0) {
                isHit = false;
            }
        }

        /* Update timer for iframes */
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

    /**
     * Renders hit animation for Brick instance.
     * Only works with Bricks with HP > 2 for now.
     * TODO: add explosion effect for destroyed bricks
     * TODO: add effect for TNT bricks 
     */
    @Override
    public void render(Graphics g) {
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;
    
            if (isHit) {
                g2d.setComposite(AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER, 0.25f));
            }
    
            g.drawImage(image, (
                        int)(x - width / 2f), (int)(y - height / 2f), 
                        (int)width, (int)height, 
                        null);
    
            if (isHit) {
                g.setColor(new Color(255, 255, 255, 150));
                g.fillRect((int)(x - width / 2f), (int)(y - height / 2f), 
                            (int)width, (int)height);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        }
    }
}