package game.powerup;
import game.gobj.*;
import game.info.GameInfo;
import game.soundmanager.SoundManager;
import game.weapon.*;

public abstract class PowerUp extends MovableObject{
    public boolean isCollected = false;

    public PowerUp(float x, float y, float width, float height, String imagePath) {
        super(x, y, width, height, imagePath);
        setVelocity(0, 1.5f + (float) Math.random() * 4.5f);
    }

    public abstract float getRarity();

    public abstract PowerUp summon();

    @Override
    public void update() {
        //Check collision với Paddle
        move();
        ApplyPowerup();
        if (isCollected) {
            SoundManager.playSound("powerUpCollected");
        }
    }

    public abstract void ApplyPowerup();

    public boolean intersect(Paddle paddle) {
        float w = (width + paddle.getWidth()) / 2;
        float h = (height + paddle.getHeight()) / 2;
        return (x >= paddle.getX() - w && x <= paddle.getX() + w &&
                y >= paddle.getY() - h && y <= paddle.getY() + h);
    }

    /**
     * Find paddle collecting PowerUp.
     * @return paddle that collects powerUp
     */
    public Paddle isCollected() {
        for (GameObject obj : GameInfo.getInstance().getCurrentObjects()) {
            if (obj instanceof Paddle p) {
                if (intersect(p)) {
                    isCollected = true;
                    return p;
                }
            }
        }
        return null;
    }
}