package powerup;

public class AmplifyPaddle extends PowerUp {

    public AmplifyPaddle(float x, float y, float width, float height, String imagePath) {
        super(x, y, width, height, imagePath);
        
    }

    @Override
    public void ApplyPowerup() {
        paddle.setSize(paddle.getWidth() * 1.5f, paddle.getHeight() * 1.5f);
    }
}

// Note: paddle hien thi loi khi sang het ben phai
