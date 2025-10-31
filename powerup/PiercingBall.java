package powerup;

import weapon.Ball;

public class PiercingBall extends PowerUp {
    private static float rarity = 0.4f;

    public PiercingBall() {
        super(0, 0, 25, 25, "Powerup/Strikethrough Powerup.png");
    }

    public PiercingBall(float x, float y, float width, float height, String imagePath) {
        super(x, y, width, height, imagePath);
        
    }

    public float getRarity() {
        return rarity;
    }

    public PowerUp summon() {
        return new PiercingBall();
    }

    @Override
    public void ApplyPowerup() {
        Ball.PIERCING = true;
    }
}
