package game.powerup;
import game.gobj.*;
import game.soundmanager.SoundManager;
import game.weapon.*;

public abstract class PowerUp extends MovableObject{
    protected static Paddle paddle;
    //public Ball ball;
    public boolean isCollected = false;

    public PowerUp(float x, float y, float width, float height, String imagePath) {
        super(x, y, width, height, imagePath);
        setVelocity(0, 1.5f + (float) Math.random() * 4.5f);
    }

    public abstract float getRarity();

    public abstract PowerUp summon();

    public static void setPaddle(Paddle paddle) {
        PowerUp.paddle = paddle;
    }

    @Override
    public void update() {
        //Check collision với Paddle
        move();
        if (isCollected()) {
            SoundManager.playSound("powerUpCollected");
            ApplyPowerup();
            isCollected = true;
        }
        
    }

    public abstract void ApplyPowerup();

    public boolean isCollected() {
        float w = (width + paddle.getWidth()) / 2;
        float h = (height + paddle.getHeight()) / 2;
        return (x >= paddle.getX() - w && x <= paddle.getX() + w &&
                y >= paddle.getY() - h && y <= paddle.getY() + h);
    }
}