package gobj;
import info.*;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class GameObject {
    protected float x, y;
    protected float width, height;
    protected Image image;

    /**
     * Creates a game object with position, size and image.
     *
     * @param x         X coordinate
     * @param y         Y coordinate
     * @param width     object width
     * @param height    object height
     * @param imagePath relative path under {@code assets/img}, 
     *                  for example, if imagePath is "brick/gold.png" then when fetching the link it will be "assets/img/brick/gold.png"
     */
    
    public static void create(float x, float y, float width, float height, String imagePath) {
        GameInfo.getInstance().getObjects().add(new GameObject(x, y, width, height, imagePath));
    }
    
    public static void create(float x, float y, float width, float height) {
        GameInfo.getInstance().getObjects().add(new GameObject(x, y, width, height, "white square.png"));
    }

    GameObject(float x, float y, float width, float height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new ImageIcon("assets/img/" + imagePath).getImage();
    }

    public void update() {

    }

    public void render(Graphics g) {
        // System.out.println("render inside gameobject");
        g.drawImage(image, (int)(x - width/2), (int)(y - height/2), (int) width, (int) height, null);
    }
    
    public void selfDestroy() {
        GameInfo.getInstance().getObjects().remove(this);
    }

    // Getter/Setter
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public void setPosition(float x, float y) { this.x = x; this.y = y; }
}
