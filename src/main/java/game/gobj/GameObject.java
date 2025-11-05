package game.gobj;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;



public class GameObject {

    public int sortingIndex = 1;
    protected float x, y;
    protected float width, height;
    protected Image image;
    private boolean isDead = false;

    /**
     * Creates a game object with position, size and image. The pivot of GameObject is in the middle
     *
     * @param x         X coordinate
     * @param y         Y coordinate
     * @param width     object width
     * @param height    object height
     * @param imagePath relative path under {@code /img}, 
     *                  for example, if imagePath is "brick/gold.png" then when fetching the link it will be "/img/brick/gold.png"
     */
    // public static void create(float x, float y, float width, float height, String imagePath) {
    //     GameInfo.getInstance().getObjects().add(new GameObject(x, y, width, height, imagePath));
    // }
    
    // public static void create(float x, float y, float width, float height) {
    //     GameInfo.getInstance().getObjects().add(new GameObject(x, y, width, height, "white square.png"));
    // }

    protected GameObject(float x, float y, float width, float height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        java.net.URL imgUrl = getClass().getResource("/img/" + imagePath);
        if (imgUrl != null) {
            this.image = new ImageIcon(imgUrl).getImage();
        } else {
            // resource not found on classpath; leave image null (caller should handle)
            this.image = null;
            System.err.println("Image resource not found: /img/" + imagePath);
        }
    }

    /*
     * Hàm này sẽ được GameManager goi liên tục khi game chạy
     */
    public void update() {
        
    }

    public void render(Graphics g) {
        // System.out.println("render inside gameobject");
        g.drawImage(image, (int)(x - width/2), (int)(y - height/2), (int) width, (int) height, null);
    }
    
    public void selfDestroy() {
        isDead = true;
    }

    public boolean isDie() {
        return isDead;
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
