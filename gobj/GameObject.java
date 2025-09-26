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
     * Creates a game object with position, size and image. The pivot of GameObject is in the middle
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

    protected GameObject(float x, float y, float width, float height, String imagePath) {
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

    public boolean isCollide(GameObject other) {
        float leftA = x - width / 2;
        float rightA = x + width / 2;
        float topA = y - height / 2;
        float bottomA = y + height / 2;

        float leftB = other.x - other.width / 2;
        float rightB = other.x + other.width / 2;
        float topB = other.y - other.height / 2;
        float bottomB = other.y + other.height / 2;
        // Kiểm tra giao nhau
        return !(rightA < leftB || leftA > rightB || bottomA < topB || topA > bottomB);
    }

    // Getter/Setter
    public float getX() {
        return x;
    }
    

    public float getY() {
        return y;
    }
    

    public float getWidth() {
        return width;
    }
    

    public float getHeight() {
        return height;
    }
    
    public void setSize(float w, float h) {
        width = w;
        height = h;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
}
