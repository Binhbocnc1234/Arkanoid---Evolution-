import gobj.*;
import info.GameInfo;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class GameManager extends JFrame {
    
    private final GamePanel panel;

    public GameManager() {
        setTitle("Arkanoid Evolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // có thể chỉnh kích thước
        setLocationRelativeTo(null);

        panel = new GamePanel();
        add(panel);

        // tạo paddle
        Paddle paddle = new Paddle(0,0,100,25,5f,null);
        paddle.setUp(800, 600);
        GameObject.create(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

        // gắn KeyListener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ev) {
                paddle.handleInput(ev.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent ev) {
                paddle.handleInput(ev.getKeyCode(), false);
            }
        });

        setFocusable(true);
        requestFocusInWindow();
        
        setVisible(true);

        // Khởi động vòng lặp game
        Timer timer = new Timer(16, e -> gameLoop());
        timer.start();
    }

    private void gameLoop() {
        // Cập nhật tất cả GameObject
        for (GameObject obj : GameInfo.getInstance().getObjects()) {
            obj.update();
        }
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
