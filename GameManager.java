import brick.*;
import gobj.*;
import info.GameInfo;
import powerup.*;
import weapon.*;
import level.LevelManager;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameManager extends JFrame {
    
    private final GamePanel panel;
    //Paddle paddle = new Paddle(0,0,0,0,12f,"VietNam.png");
    //Ball ball = new Ball(GameInfo.SCREEN_WIDTH / 2f, 300, "Ball.png", 25f, paddle);

    public GameManager() {
        setTitle("Arkanoid Evolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        setLocationRelativeTo(null);

        panel = new GamePanel();
        add(panel);

        // tạo paddle
        Paddle paddle = new Paddle(0,0,0,0,5f,"VietNam.png");
        paddle.setUp(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        // thêm paddle vào game
        GameInfo.getInstance().getObjects().add(paddle);

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



        setFocusable(true);
        setVisible(true);
        requestFocusInWindow();

        // Khởi động vòng lặp game
        Timer timer = new Timer(16, e -> gameLoop());
        timer.start();
    }

    private void gameLoop() {
        // Cập nhật tất cả GameObject
        for (int i = 0; i < GameInfo.getInstance().getObjects().size(); i++) {
            GameObject obj = GameInfo.getInstance().getObjects().get(i);
            obj.update();
        }

        /* Check if brick has been destoryed */ 
        GameInfo.getInstance().getObjects().removeIf(obj -> {
            if (obj instanceof Brick) {
                return ((Brick) obj).isDestroyed();
            }
            return false;
        });

        /* Check if all bricks has been destroyed, then switch level. */
        boolean allBricksDestroyed = GameInfo.getInstance().getObjects().stream()
            .noneMatch(obj -> obj instanceof Brick);

        if(allBricksDestroyed) {
            LevelManager.getInstance().switchToNextLevel();

            for(GameObject obj : GameInfo.getInstance().getObjects()) {
                if (obj instanceof Paddle paddle) {
                    paddle.reset();
                }
                if (obj instanceof Ball ball) {
                    ball.reset();;
                }
            }
        }

        //panel.repaint();

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
        panel.repaint();
    }

    // JPanel để vẽ các GameObject
    private class GamePanel extends JPanel {
        public GamePanel() {
            setBackground(Color.BLACK);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (GameObject obj : GameInfo.getInstance().getObjects()) {
                // System.out.println("Render");
                obj.render(g);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameManager gm = new GameManager();
            if (GameInfo.isTesting) {
                System.out.println("Welcome to Arkanoid - Cuộc sống cô đơn");
                //Tạo một gameObject kích cỡ vừa phải tại mỗi vị trí click chuột
                // gm.panel.addMouseListener(new java.awt.event.MouseAdapter() {
                //     @Override
                //     public void mouseClicked(java.awt.event.MouseEvent e) {
                //         GameObject.create(
                //             e.getX(), 
                //             e.getY(),
                //             50, 50
                //         );
                //         System.out.println("Create test object");
                //     }
                // });
            }
        });
    }
}
