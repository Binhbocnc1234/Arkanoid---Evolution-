package weapon;
import gobj.MovableObject;
import info.*;

public class Paddle extends MovableObject {
    private float speed;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    private boolean isRecoiling = false;
    private boolean isRecoilDown = false;
    private int leftBound, rightBound;
    private int leftKey, rightKey;
    
    private float startY;
    private static final float RECOIL_DISTANCE = 8.0f;
    private static final float RECOIL_SPEED = 1.5f;

    public Paddle(float x, float y, float w, float h, float speed, String imagePath) {
        super(x, y, w, h, imagePath);
        this.speed = speed;
        this.startY = y;
    }
    
    /*
     * Căn chỉnh 2 biên mà paddle có thể đi lại
     */
    public void setUp(int leftBound, int rightBound, float windowHeight) {
        this.height = 20;
        this.width = (GameInfo.CAMPAIGN_WIDTH / 10) * 2;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.x = (leftBound + rightBound) / 2f;
        this.y = windowHeight - this.height / 2f - 50;
        this.startY = this.y;
    }

    public void setKey(int leftKey, int rightKey) {
        this.leftKey = leftKey;
        this.rightKey = rightKey;
    }
    /**
     * cập nhật vận tốc theo phím bấm.
     * @param key phím bấm 
     * @param pressed nhấn thì trả về true, còn lại trả về false
     */

    public void handleInput(int key, boolean pressed) {
        if (key == leftKey) {
            moveLeft = pressed;
        }
        if (key == rightKey) {
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

        if (GameInfo.getInstance().isMultiplayer) {
            if (x + width / 2f > GameInfo.SCREEN_WIDTH) {
                x = GameInfo.SCREEN_WIDTH - width / 2;
            }
        } else {
            if (x + width / 2f > GameInfo.CAMPAIGN_WIDTH) {
                x = GameInfo.CAMPAIGN_WIDTH - width / 2;
            }
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
        this.x = (leftBound + rightBound) / 2f;
        this.setVelocity(0, 0);
        this.height = 20;
        this.width = (GameInfo.CAMPAIGN_WIDTH / 10) * 2;
        moveLeft = false;
        moveRight = false;
    }


    public void recoil() {
        if (!isRecoiling) {
            this.isRecoilDown = true;
            this.isRecoiling = true;
        }
        
    }
}