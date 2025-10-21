package powerup;
import info.GameInfo;
import weapon.*;

public class TripleBall extends PowerUp{
    public Ball ball;
    
    public TripleBall(float x, float y, float width, float height, String imagePath) {
        super(x, y, width, height, imagePath);
    }

    @Override
    public void ApplyPowerup() {
        GameInfo.getInstance().addGameObject(new Ball(ball.getX()/1.2f, ball.getY()/1.2f,
                                            "Ball.png", 25, paddle));
        GameInfo.getInstance().addGameObject(new Ball(ball.getX()/1.1f, ball.getY()/1.1f,
                                            "Ball.png", 25, paddle));
    }

}