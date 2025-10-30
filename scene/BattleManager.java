package scene;
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
import score.Score;
import soundmanager.*;
import weapon.*;

enum BattleState {
    Fighting,
    Lose,
    Win,
    Pause,
}

public class BattleManager extends JPanel {
    BattleState state = BattleState.Fighting;
    private long loseTimestamp = -1;
    private Score score;
    private Timer timer;
    private JPanel pauseMenu;
    private JButton pauseButton;  // thay đổi kiểu
    private float volumePercent = 50f;

    public BattleManager(boolean isMultiplayer) {
        setLayout(null); // Để có thể set vị trí chính xác cho các component
        GameInfo.getInstance().Initialize();

        // Paddle và Ball
        Ball ball;
        if (isMultiplayer) {
            Paddle paddle1 = new Paddle(0,0,0,0,12f,"paddle.png");
            paddle1.setUp(0, GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT);
            paddle1.setKey(KeyEvent.VK_A, KeyEvent.VK_D);
            GameInfo.getInstance().addGameObject(paddle1);

            Paddle paddle2 = new Paddle(0,0,0,0,12f,"paddle.png");
            paddle2.setUp(GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
            paddle2.setKey(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
            GameInfo.getInstance().addGameObject(paddle2);
            //Tạo keyevent cho Paddle
            addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent ev) {
                    paddle1.handleInput(ev.getKeyCode(), true);
                    paddle2.handleInput(ev.getKeyCode(), true);
                }
                @Override public void keyReleased(KeyEvent ev) {
                    paddle1.handleInput(ev.getKeyCode(), false);
                    paddle2.handleInput(ev.getKeyCode(), false);
                }
                
            });

            ball = new Ball(paddle1.getX(), paddle1.getY() - paddle1.getHeight(), "Ball.png", 25f);
            GameInfo.getInstance().getObjects().add(ball);
        }
        else {
            Paddle paddle = new Paddle(0,0,0,0,12f,"paddle.png");
            paddle.setUp(0, GameInfo.CAMPAIGN_WIDTH, GameInfo.SCREEN_HEIGHT);
            paddle.setKey(KeyEvent.VK_A, KeyEvent.VK_D);
            GameInfo.getInstance().addGameObject(paddle);
            //Tạo keyevent cho Paddle
            addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent ev) {
                    paddle.handleInput(ev.getKeyCode(), true);
                }
                @Override public void keyReleased(KeyEvent ev) {
                    paddle.handleInput(ev.getKeyCode(), false);
                }
                
            });
            // thêm Ball vào game
            ball = new Ball(paddle.getX(), paddle.getY() - paddle.getHeight(), "Ball.png", 25f);
            GameInfo.getInstance().getObjects().add(ball);
            //PowerUp
            PowerUp.setPaddle(paddle);
        }

        /* Initiate the first level to avoid immediately switching to next level */
        LevelManager.getInstance().loadCurrentLevel();

        //sound
        SoundManager.getSound("wall", "/assets/sound/bounce.wav");
        SoundManager.getSound("paddle", "/assets/sound/paddle.wav");
        SoundManager.getSound("brick", "/assets/sound/brick.wav");
        SoundManager.getSound("background", "/assets/sound/backgroundMusic.wav");
        SoundManager.getSound("powerUpCollected", "/assets/sound/powerUpCollected.wav");
        SoundManager.getSound("button", "/assets/sound/button.wav");
        // Khởi động vòng lặp game
        timer = new Timer(1000/GameInfo.FPS, e -> gameLoop());
        timer.start();

        score = new Score();
      
        // Pause button
        pauseButton = new JButton();
        pauseButton.setBounds(GameInfo.CAMPAIGN_WIDTH - 60, 5, 40, 40);
        pauseButton.setIcon(new ImageIcon("assets/img/Pause button.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusPainted(false);
        pauseButton.addActionListener(e -> {
            SoundManager.playSound("button");
            showPauseMenu();
        });
        add(pauseButton);

        SoundManager.playSoundLoop("background");
        // Tạo pause menu
        createPauseMenu();
    }

    private void createPauseMenu() {
        pauseMenu = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(30, 20, 60)); // Tím than
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.drawRect(0, 0, getWidth()-1, getHeight()-1); // Viền trắng
            }
        };
        
        // Set size và position cho pause menu
        int menuWidth = 300;
        int menuHeight = 450;
        pauseMenu.setBounds(
            (GameInfo.CAMPAIGN_WIDTH - menuWidth)/2,
            (GameInfo.SCREEN_HEIGHT - menuHeight)/2,
            menuWidth,
            menuHeight
        );
        pauseMenu.setVisible(false);

        // Thêm label PAUSE
        MyLabel pauseLabel = new MyLabel("PAUSE", menuWidth/2, 80, 200, 48);
        pauseMenu.add(pauseLabel);

        // Thêm nút Continue
        MyButton continueBtn = new MyButton("Continue", menuWidth/2, 200, 200, 50);
        continueBtn.addActionListener(e -> {
            SoundManager.playSound("button");
            hidePauseMenu();
        });
        pauseMenu.add(continueBtn);

        // Thêm nút Return to Lobby
        MyButton returnBtn = new MyButton("Return to Lobby", menuWidth/2, 280, 200, 50);
        returnBtn.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new Lobby());
        } );
        pauseMenu.add(returnBtn);

        MyButton changeSoundBtn = new MyButton("volume", menuWidth / 2, 340, 200,50);
        pauseMenu.add(changeSoundBtn);

        JSlider volumeSilder = new JSlider(0, 100,(int) volumePercent);
        volumeSilder.setBounds(menuWidth / 2 - 100, 400, 200, 50);
        volumeSilder.setMajorTickSpacing(25);
        volumeSilder.setMinorTickSpacing(10);
        volumeSilder.setPaintTicks(true);
        volumeSilder.setPaintLabels(true);
        volumeSilder.setVisible(false);
        pauseMenu.add(volumeSilder);
        changeSoundBtn.addActionListener(e -> {
            SoundManager.playSound("button");
            volumeSilder.setVisible(!volumeSilder.isVisible());
        });

        volumeSilder.addChangeListener(e -> {
            volumePercent = volumeSilder.getValue();
            SoundManager.setSpecificVolume("background", volumePercent);
            System.out.println("Am luong : " + volumePercent + "%");
        });

        add(pauseMenu);
    }

    private void showPauseMenu() {
        state = BattleState.Pause;
        pauseMenu.setVisible(true);
        SoundManager.stopSound("background");
    }

    private void hidePauseMenu() {
        state = BattleState.Fighting;
        pauseMenu.setVisible(false);
        SoundManager.playSoundLoop("background");
    }

    private void endBattle() {
        GameManager.instance.switchTo(new Lobby());
        timer.stop();
        SoundManager.stopSound("background");
    }

    private void gameLoop() {
        if (state == BattleState.Pause) {
            return;
        }
        if (state == BattleState.Lose) {
            long now = System.nanoTime();
            if (now - loseTimestamp >= 3_000_000_000L) {
                endBattle();
            }
        }
        else {
            // Cập nhật tất cả GameObject
            for (GameObject obj : GameInfo.getInstance().getCurrentObjects()) {
                obj.update();
            } 

            /* Update player score */
            for (GameObject obj: GameInfo.getInstance().getCurrentObjects()) {
                if (obj instanceof Brick brick) {
                    if (brick.isDie()) {
                        score.updatePlayerScore(brick.getBrickScore());
                    }
                }
                if (obj instanceof PowerUp powerUp) {
                    if (powerUp.isCollected()) {
                        score.updatePlayerScore(150);
                    }
                }
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
                int currLevel = LevelManager.getInstance().getCurrentLevel();
                GameInfo.getInstance().setUnlockedLevel(currLevel + 1);

                LevelManager.getInstance().switchToNextLevel();

                for (GameObject obj : GameInfo.getInstance().getObjects()) {
                    if (obj instanceof Paddle paddle) {
                        paddle.reset();
                    }
                    if (obj instanceof Ball ball) {
                        ball.reset();
                    }
                    if (obj instanceof PowerUp powerUp) {
                        powerUp.selfDestroy();
                    }
                    if (obj instanceof BrickParticle particle) {
                        particle.selfDestroy();
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
                        GameInfo.CAMPAIGN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2,
                        300, 60));
            }
            
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
        
        // Vẽ các GameObject
        for (GameObject obj : GameInfo.getInstance().getCurrentObjects()) {
            obj.render(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(GameInfo.getInstance().getSmallFont());
        
        /* Rendering score */
        String currentScoreText = String.format("SCORE: %06d", score.getPlayerScore());
        int currentScorePos = (GameInfo.SCREEN_WIDTH * 4 / 3 + g.getFontMetrics().stringWidth(currentScoreText)) / 2;
        g.drawString(currentScoreText, currentScorePos, 300);
        
        // Nếu đang pause, vẽ một lớp overlay tối
        if (state == BattleState.Pause) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, 0.5f)); // màu đen với độ trong suốt 50%
            g2d.fillRect(0, 0, GameInfo.CAMPAIGN_WIDTH, GameInfo.SCREEN_HEIGHT);
        }
    }
}
