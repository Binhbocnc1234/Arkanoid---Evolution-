package weapon;
import gobj.MovableObject;
import info.*;
import java.awt.event.KeyEvent;

public class Paddle extends MovableObject {
    private static Paddle instance;
    private float speed;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public Paddle(float x, float y, float w, float h, float speed, String imagePath) {
        super(x, y, w, h, imagePath);
        this.speed = speed;
        instance = this;
    }
    public static Paddle getInstance(){
        return instance;
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

    /**
     * Handling input of the Paddle instance
     */
    public void handleInput(int key, boolean pressed) {
        /* FIX: Updates velocity outside the function rather than restting directly. */
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = pressed;
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = pressed;
        }
        updateVelocity();
    }

    /**
     * Calcuate velocity of the Paddle instance.
     */
    private void updateVelocity() {
        if (leftPressed && !rightPressed) dx = -speed;
        else if (rightPressed && !leftPressed) dx = speed;
        else dx = 0;
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

    /**
     * Reset the Paddle instance to its default location.
     */
    public void reset() {
        this.setUp(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        this.setVelocity(0, 0);
    }
}