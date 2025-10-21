package weapon;
import gobj.MovableObject;
import info.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

public class Paddle extends MovableObject {
    private static Paddle instance;
    private float speed;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    private boolean isRecoiling = false;
    private boolean isRecoilDown = false;
    private float startY;
    private static final float RECOIL_DISTANCE = 8.0f;
    private static final float RECOIL_SPEED = 1.5f;

    public Paddle(float x, float y, float w, float h, float speed, String imagePath) {
        super(x, y, w, h, imagePath);
        this.speed = speed;
        instance = this;
        this.startY = y;
    }
    public static Paddle getInstance(){
        return instance;
    }
    public void setUp(float windowWidth, float windowHeight) {
        this.height = 20;
        this.width = (windowWidth / 10) * 2;
        this.x = windowWidth / 2f;
        this.y = windowHeight - this.height / 2f - 50;
        this.startY = this.y;
    }

    /**
     * cập nhật vận tốc theo phím bấm.
     * @param key phím bấm 
     * @param pressed nhấn thì trả về true, còn lại trả về false
     */

    public void handleInput(int key, boolean pressed) {
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            moveLeft = pressed;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            moveRight = pressed;
        }
        if (moveLeft && !moveRight) {
            dx = -speed;
        }
        else if (!moveLeft && moveRight) {
            dx = speed;
        }
        else {
            dx = 0;
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
        if (isRecoiling) {
            float recoilY = startY + RECOIL_DISTANCE;
            if (isRecoilDown) {
                y += RECOIL_SPEED;
                if (y >= recoilY) {
                    y = recoilY;
                    isRecoilDown = false;
                }

            } else {
                y -= RECOIL_SPEED;
                if (y <= startY) {
                    y = startY;
                    isRecoiling = false;
                }
            }
        }
    }

    /**
     * Reset the Paddle instance to its default location.
     */
    public void reset() {
        this.setUp(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        this.setVelocity(0, 0);
    }


    public void recoil() {
        if (!isRecoiling) {
            this.isRecoilDown = true;
            this.isRecoiling = true;
        }
        
    }
}