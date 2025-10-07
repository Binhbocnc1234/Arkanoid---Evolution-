package powerup;
import gobj.*;
import weapon.*;

public class PowerUp extends MovableObject{
    public Paddle paddle;
    //public Ball ball;
    public boolean isCollected = false;

    public PowerUp(float x, float y, float width, float height, String imagePath) {
        super(x, y, width, height, imagePath);
        setVelocity(0, 1.5f);
    }
    @Override
    public void update() {
        //Check collision với Paddle
        move();
        if (isCollected()) {
            ApplyPowerup();
            isCollected = true;
        }
        
    }

    public void ApplyPowerup() {
        
    }

    /**
     toa do tinh tu goc tren trai
     public boolean isCollected() {
        return (x >= paddle.getX() - width && x <= paddle.getX() + paddle.getWidth() &&
                y >= paddle.getY() - height && y <= paddle.getY() + paddle.getHeight());
    }*/

    // neu toa do tinh tu tam
    public boolean isCollected() {
        float w = (width + paddle.getWidth()) / 2;
        float h = (height + paddle.getHeight()) / 2;
        return (x >= paddle.getX() - w && x <= paddle.getX() + w &&
                y >= paddle.getY() - h && y <= paddle.getY() + h);
    }
}