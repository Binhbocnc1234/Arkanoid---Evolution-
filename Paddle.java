import gobj.MovableObject;
import info.*;
import java.awt.event.KeyEvent;

public class Paddle extends MovableObject {

    private float speed;

    public Paddle(float x, float y, float w, float h, float speed, String imagePath) {
        super(x, y, w, h, imagePath);
        this.speed = speed;
    }

    public void setUp(float windowWidth, float windowHeight) {
        this.height = 20;
        this.width = (windowWidth / 10) * 2;
        this.x = windowWidth / 2f;
        this.y = windowHeight - this.height / 2f - 50;
    }

    /**
     * cập nhật vận tốc theo phím bấm.
     * @param key phím bấm 
     * @param pressed nhấn thì trả về true, còn lại trả về false
     */

    public void handleInput(int key, boolean pressed) {
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            dx = pressed ? -speed : 0;
        }
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            dx = pressed ? speed : 0;
        }
    }

    @Override
    public void update() {
        move(); // cộng dx, dy vào x,y
        // giới hạn paddle trong màn hình
        if (x - width / 2f < 0) {
            x = width / 2f;
        }
        if (x + width / 2f > GameInfo.SCREEN_WIDTH) {
            x = GameInfo.SCREEN_WIDTH - width / 2;
        }
    }
}