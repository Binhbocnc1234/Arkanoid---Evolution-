package game.powerup;

import game.weapon.Paddle;

public class AmplifyPaddle extends PowerUp {
    private static float rarity = 0.3f;
    public static final float MAX_WIDTH = 300;
    public static final float MAX_HEIGHT = 45;

    public AmplifyPaddle() {
        super(0, 0, 25, 25, "Powerup/Extended Paddle Powerup.png");
    }

    public AmplifyPaddle(float x, float y, float width, float height, String imagePath) {
        super(x, y, width, height, imagePath);
        
    }

    public float getRarity() {
        return rarity;
    }

    public PowerUp summon() {
        return new AmplifyPaddle();
    }

    @Override
    public void ApplyPowerup() {
        Paddle paddle = isCollected();
        if (paddle != null) {
            float amplifiedWidth = paddle.getWidth() + 20;
            float amplifiedHeight = paddle.getHeight() + 5f;

            if (amplifiedWidth > MAX_WIDTH) amplifiedWidth = MAX_WIDTH;
            if (amplifiedHeight > MAX_HEIGHT) amplifiedHeight = MAX_HEIGHT;

            paddle.setSize(amplifiedWidth, amplifiedHeight);
        }
    }
}
