package powerup;

public class AmplifyPaddle extends PowerUp {
    private static float rarity = 0.5f;

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
        paddle.setSize(paddle.getWidth() * 1.5f, paddle.getHeight() * 1.5f);
    }
}

// Note: paddle hien thi loi khi sang het ben phai
