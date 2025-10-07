import brick.*;
import gobj.*;
import info.GameInfo;
import powerup.*;
import weapon.*;


import java.awt.*;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameManager extends JFrame {
    
    private final GamePanel panel;
    Paddle paddle = new Paddle(0,0,0,0,5f,"VietNam.png");
    Ball ball = new Ball(400, 300, "Ball.png", 25f, paddle);

    public GameManager() {
        setTitle("Arkanoid Evolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        setLocationRelativeTo(null);

        panel = new GamePanel();
        add(panel);

        // tạo paddle
        //Paddle paddle = new Paddle(0,0,0,0,5f,"VietNam.png");
        paddle.setUp(800, 600);
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

        //Ball ball = new Ball(400, 300, "Ball.png", 25f, paddle);
        GameInfo.getInstance().getObjects().add(ball);

        // brick & level reader debug
        List<GameObject> levelBricks = level.LevelLoader.loadLevel(
            "assets/level/level1.txt",
            (GameInfo.SCREEN_WIDTH / 10), 25,
            30, 12
        );
        GameInfo.getInstance().getObjects().addAll(levelBricks);

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

        // Check if brick has been destoryed
        GameInfo.getInstance().getObjects().removeIf(obj -> {
            if (obj instanceof Brick) {
                return ((Brick) obj).isDestroyed();
            }
            return false;
        });

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
