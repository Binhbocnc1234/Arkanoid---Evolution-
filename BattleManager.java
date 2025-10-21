import UI.*;
import brick.*;
import gobj.*;
import info.GameInfo;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import level.LevelManager;
import powerup.*;
import soundmanager.*;
import weapon.*;

enum BattleState {
    Fighting,
    Lose,
    Win
}

public class BattleManager extends JPanel {
    BattleState state = BattleState.Fighting;
    private long loseTimestamp = -1;
    private Timer timer;
    public BattleManager() {
        GameInfo.getInstance().Initialize();
        // tạo paddle
        Paddle paddle = new Paddle(0,0,0,0,12f,"paddle.png");
        paddle.setUp(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        // thêm paddle vào game
        GameInfo.getInstance().addGameObject(paddle);

        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent ev) {
                paddle.handleInput(ev.getKeyCode(), true);
            }
            @Override public void keyReleased(KeyEvent ev) {
                paddle.handleInput(ev.getKeyCode(), false);
            }
        });

        Ball ball = new Ball(paddle.getX(), paddle.getY() - paddle.getHeight(), "Ball.png", 25f, paddle);
        GameInfo.getInstance().getObjects().add(ball);

        /* Initiate the first level to avoid immediately switching to next level */
        LevelManager.getInstance().loadCurrentLevel();

        //PowerUp
        PowerUp amplifyPaddle = new AmplifyPaddle(300, 300, 25, 25, "white square.png");
        GameInfo.getInstance().getObjects().add(amplifyPaddle);
        amplifyPaddle.paddle = paddle;
        TripleBall threeBall = new TripleBall(400, 300, 25, 25, "white square.png");
        threeBall.paddle = paddle;
        threeBall.ball = ball;
        PowerUp tripleBall = threeBall;
        GameInfo.getInstance().getObjects().add(tripleBall);

        //sound
        SoundManager.getSound("wall", "/assets/sound/bounce.wav");
        SoundManager.getSound("paddle", "/assets/sound/bounce.wav");
        SoundManager.getSound("brick", "/assets/sound/brick.wav");
        // Khởi động vòng lặp game
        timer = new Timer(16, e -> gameLoop());
        timer.start();
    }

    private void endBattle() {
        GameManager.instance.switchTo(new Lobby());
        timer.stop();
    }
    private void gameLoop() {
        if (state == BattleState.Lose) {
            long now = System.nanoTime();
            if (now - loseTimestamp >= 3_000_000_000L) {
                endBattle();
            }
        }
        else {
            // Cập nhật tất cả GameObject
            for (int i = 0; i < GameInfo.getInstance().getObjects().size(); i++) {
                GameObject obj = GameInfo.getInstance().getObjects().get(i);
                obj.update();
            }
            GameInfo.getInstance().flushGameObject();
            // Nếu GameObject được đánh dấu là đã chết thì loại nó khỏi dãy
            GameInfo.getInstance().getObjects().removeIf(obj -> obj.isDie());

            /* Check if all bricks has been destroyed, then switch level. */
            boolean allBricksDestroyed = GameInfo.getInstance().getObjects().stream()
                .filter(obj -> obj instanceof Brick)
                .map(obj -> (Brick) obj)
                .filter(brick -> brick.getHp() < Integer.MAX_VALUE)
                .noneMatch(obj -> obj instanceof Brick);
                
            if (allBricksDestroyed) {
                LevelManager.getInstance().switchToNextLevel();

                for (GameObject obj : GameInfo.getInstance().getObjects()) {
                    if (obj instanceof Paddle paddle) {
                        paddle.reset();
                    }
                    if (obj instanceof Ball ball) {
                        ball.reset();
                    }
                }
            }

            /* Kiểm tra nếu Ball bị destroyed toàn bộ thì dẫn đến thua cuộc*/
            boolean haveBall = false;
            for (GameObject obj : GameInfo.getInstance().getCurrentObjects()) {
                if (obj instanceof Ball) {
                    haveBall = true;
                }
            }
            if (haveBall == false) {
                System.out.print("Bạn đã thua, vài giây nữa sẽ quay trở lại màn hình chính");
                state = BattleState.Lose;
                loseTimestamp = System.nanoTime();
                add(new MyLabel("You lose",
                        GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2,
                        300, 60));
            }
            
            /**for (GameObject obj : GameInfo.getInstance().getObjects()) {
                if (obj instanceof Powerup p) {
                    if (((Powerup) obj).isCollected()) {
                        p.ApplyPowerup();
                        //Ball n = new Ball(400, 300, "Ball.png", 25f, paddle);
                        //GameInfo.getInstance().getObjects().add(n);
                        //i = true;
                        p.isCollected = true;
                        //GameInfo.getInstance().getObjects().remove(p);
                    }
                }
            }*/

            GameInfo.getInstance().getObjects().removeIf(obj -> obj instanceof PowerUp && 
                    ((PowerUp) obj).isCollected);
        }

        setBackground(Color.BLACK);
        this.repaint();
    }

    // Hàm render các GameObject lên Panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (GameObject obj : GameInfo.getInstance().getCurrentObjects()) {
            obj.render(g);
        }
    }
}
