package game.scene;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import game.UI.MyLabel;
import game.brick.Brick;
import game.brick.BrickExplosion;
import game.brick.BrickParticle;
import game.gobj.GameObject;
import game.info.GameInfo;
import game.level.LevelManager;
import game.powerup.PowerUp;
import game.score.HighScores;
import game.score.Score;
import game.soundmanager.SoundManager;
import game.weapon.Ball;
import game.weapon.BallTrail;
import game.weapon.Paddle;

enum BattleState {
    Fighting,
    Lose,
    Win,
    Pause,
    Ready,
    LevelComplete,
}

public class BattleManager extends JPanel implements  IDisposable {
    public BattleState state = BattleState.Ready;
    private long loseTimestamp = -1;
    private final Score score;
    private final Timer timer;
    private PauseManager pauseManager;

    private Image rightPanelBackground;
    private Image background;
    private JPanel pauseMenu;
    private JButton pauseButton;  // thay đổi kiểu
    private float volumePercent = 50f;

    private final int MAX_LEVEL = 10;

    public BattleManager(boolean isMultiplayer) {
        // GameInfo.getInstance().isSlowmotion = true;
        GameInfo.getInstance().isMultiplayer = isMultiplayer;
    rightPanelBackground = new ImageIcon(getClass().getResource("/img/background/rightPanel.png")).getImage();
    background = new ImageIcon(getClass().getResource("/img/background/background.jpg")).getImage();
        if (rightPanelBackground.getWidth(null) == -1 || rightPanelBackground.getHeight(null) == -1) {
            System.err.println("Image not found or invalid path");
        }
        setLayout(null); // Để có thể set vị trí chính xác cho các component

        // Paddle và Ball
        Ball ball;
        if (isMultiplayer) {
            // Online multiplayer
            MultiplayerContext ctx = MultiplayerContext.getInstance();
            Paddle localPaddle = new Paddle(0,0,0,0,12f,"paddle.png");
            Paddle remotePaddle = new Paddle(0,0,0,0,12f,"paddle.png");
            
            // Setup paddles based on player ID
            if (ctx.isPlayer1()) {
                localPaddle.setUp(0, GameInfo.CAMPAIGN_WIDTH, GameInfo.SCREEN_HEIGHT);
                remotePaddle.setUp(0, GameInfo.CAMPAIGN_WIDTH, 50); // Remote paddle at top
            } else {
                localPaddle.setUp(0, GameInfo.CAMPAIGN_WIDTH, GameInfo.SCREEN_HEIGHT);
                remotePaddle.setUp(0, GameInfo.CAMPAIGN_WIDTH, 50); // Remote paddle at top
            }
            
            localPaddle.setKey(KeyEvent.VK_A, KeyEvent.VK_D);
            GameInfo.getInstance().addGameObject(localPaddle);
            GameInfo.getInstance().addGameObject(remotePaddle);

            // Only local paddle responds to input
            addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent ev) {
                    // Only player 1 can start the game
                    if (state == BattleState.Ready && ev.getKeyCode() == KeyEvent.VK_SPACE) {
                        if (ctx.isPlayer1()) {
                            state = BattleState.Fighting;
                            // Notify peer that game is starting
                            ctx.getClient().sendStart();
                        }
                    } else if (state == BattleState.Fighting) {
                        localPaddle.handleInput(ev.getKeyCode(), true);
                        // Send position update to peer
                        ctx.getClient().sendPosition(localPaddle.getX(), localPaddle.getY());
                    }
                }
                @Override public void keyReleased(KeyEvent ev) {
                    if (state == BattleState.Fighting) {
                        localPaddle.handleInput(ev.getKeyCode(), false);
                        // Send position update to peer
                        ctx.getClient().sendPosition(localPaddle.getX(), localPaddle.getY());
                    }
                }
            });

            // Ball starts at player 1's paddle
            if (ctx.isPlayer1()) {
                ball = new Ball(localPaddle.getX(), localPaddle.getY() - localPaddle.getHeight(), "Ball.png", 25f);
            } else {
                ball = new Ball(remotePaddle.getX(), remotePaddle.getY() + remotePaddle.getHeight(), "Ball.png", 25f);
            }
            GameInfo.getInstance().addGameObject(ball);
        }
        else {
            Paddle paddle = new Paddle(0,0,0,0,12f,"paddle.png");
            paddle.setUp(0, GameInfo.CAMPAIGN_WIDTH, GameInfo.SCREEN_HEIGHT);
            paddle.setKey(KeyEvent.VK_A, KeyEvent.VK_D);
            GameInfo.getInstance().addGameObject(paddle);
            //Tạo keyevent cho Paddle
            addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent ev) {
                    if (state == BattleState.Ready && ev.getKeyCode() == KeyEvent.VK_SPACE) {
                        state = BattleState.Fighting;
                    } else if (state == BattleState.Fighting) {
                        paddle.handleInput(ev.getKeyCode(), true);
                    }
                }
                @Override public void keyReleased(KeyEvent ev) {
                    if (state == BattleState.Fighting) {
                        paddle.handleInput(ev.getKeyCode(), false);
                    }
                }
                
            });
            // thêm Ball vào game
            ball = new Ball(paddle.getX(), paddle.getY() - paddle.getHeight(), "Ball.png", 25f);
            GameInfo.getInstance().addGameObject(ball);
            //PowerUp
        }

        /* Initiate the first level to avoid immediately switching to next level */
        LevelManager.getInstance().loadCurrentLevel();

        //sound
        SoundManager.getSound("wall", "/sound/bounce.wav");
        SoundManager.getSound("paddle", "/sound/paddle.wav");
        SoundManager.getSound("brick", "/sound/brick.wav");
        SoundManager.getSound("background", "/sound/backgroundMusic.wav");
        SoundManager.getSound("powerUpCollected", "/sound/powerUpCollected.wav");
        SoundManager.getSound("button", "/sound/button.wav");
        SoundManager.getSound("levelComplete", "/sound/levelComplete.wav");
        SoundManager.getSound("gameOver", "/sound/gameOver.wav");

        // Khởi động vòng lặp game
        timer = new Timer(1000/GameInfo.FPS, e -> gameLoop());
        timer.start();

        score = new Score();
        // Pause UI is handled by PauseManager (encapsulates pause menu + button)
        // Áp dụng design pattern bridge
        pauseManager = new PauseManager(this);

        SoundManager.playSoundLoop("background");
        //SoundManager.setSpecificVolume("background", 20f);

        if (GameInfo.getInstance().isMultiplayer) {
            GameInfo.getInstance().setCurrentPlayerName(null);
        }
    }

    // endBattle không còn được sử dụng mà logic cập nhật score di chuyển vào hàm dispose
    private void endBattle() {
        // Save score if applicable
        if (!GameInfo.getInstance().isMultiplayer && (state == BattleState.Lose || state == BattleState.Win)) {
            String playerName = GameInfo.getInstance().getCurrentPlayerName();
            if (playerName != null && !playerName.isEmpty()) {
                HighScores.getInstance().addScore(playerName, score.getPlayerScore());
                // GameInfo.getInstance().setCurrentPlayerName(null);
            }
        }
    }

    @Override //Được gọi khi từ scene BattleManager chuyển sang scene mới
    public void dispose() {
        if (!GameInfo.getInstance().isMultiplayer) {
            String playerName = GameInfo.getInstance().getCurrentPlayerName();
            if (playerName != null && !playerName.isEmpty()) {
                HighScores.getInstance().addScore(playerName, score.getPlayerScore());
                // GameInfo.getInstance().setCurrentPlayerName(null);
            }
        }
        timer.stop();
        GameInfo.getInstance().clear();
        SoundManager.stopSound("background");
        SoundManager.stopSound("levelComplete");
    }

    private void gameLoop() {
        if (state == BattleState.Pause || state == BattleState.LevelComplete) {
            return;
        }

        if (state == BattleState.Lose) {
            long now = System.nanoTime();
            if (now - loseTimestamp >= 3_000_000_000L) {
                // Switch to lobby
                GameManager.instance.switchTo(new Lobby());
            }
        }
        else if (state == BattleState.Fighting) {
            //SoundManager.setSpecificVolume("background", 50f);
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
                    if (powerUp.isCollected) {
                        score.updatePlayerScore(150);
                    }
                }
            }

            // Nếu GameObject được đánh dấu là đã chết thì loại nó khỏi dãy
            //GameInfo.getInstance().getObjects().removeIf(obj -> obj.isDie());

            /* Check if all bricks has been destroyed, then switch level. */
            boolean allBricksDestroyed = GameInfo.getInstance().getObjects().stream()
                .filter(obj -> obj instanceof Brick) 
                .map(obj -> (Brick) obj)
                .filter(brick -> brick.getHp() < Integer.MAX_VALUE)
                .noneMatch(obj -> obj instanceof Brick);
                
            if (allBricksDestroyed) {
                //SoundManager.setSpecificVolume("background", 20f);
                SoundManager.playSound("levelComplete");

                int currLevel = LevelManager.getInstance().getCurrentLevel();
                if (currLevel > GameInfo.getInstance().getUnlockedLevel()) {
                    GameInfo.getInstance().setUnlockedLevel(currLevel + 1);
                }

                // reseting playfield
                for (GameObject obj : GameInfo.getInstance().getObjects()) {
                    if (obj instanceof Paddle paddle) paddle.reset();
                    if (obj instanceof Ball ball) ball.reset();

                    if (obj instanceof PowerUp powerUp) powerUp.selfDestroy();
                    if (obj instanceof BallTrail ballTrail) ballTrail.selfDestroy();
                    if (obj instanceof BrickParticle brickParticle) brickParticle.selfDestroy();
                    if (obj instanceof BrickExplosion brickExplosion) brickExplosion.selfDestroy();
                }

                state = BattleState.LevelComplete;

                // wait 2 seconds, then switch level
                Timer delayTimer = new Timer(2000, e -> {
                    LevelManager.getInstance().switchToNextLevel();
                    state = BattleState.Ready;
                   
                    ((Timer) e.getSource()).stop();
                });

                delayTimer.setRepeats(false);
                delayTimer.start();
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

                SoundManager.stopSound("background");
                SoundManager.playSound("gameOver");

                loseTimestamp = System.nanoTime();
                add(new MyLabel("You lose",
                        GameInfo.CAMPAIGN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2,
                        300, 60));
            }
            
            GameInfo.getInstance().getObjects().removeIf(obj -> obj instanceof PowerUp && 
                    ((PowerUp) obj).isCollected);
            
        }

        GameInfo.getInstance().getObjects().removeIf(obj -> obj.isDie());
        GameInfo.getInstance().flushGameObject();

        setBackground(Color.BLACK);
        this.repaint();
    }

    // Hàm render các GameObject lên Panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Rendering BG (phai render theo thu tu...)
        if (!GameInfo.getInstance().isMultiplayer) {

            g.drawImage(rightPanelBackground, GameInfo.CAMPAIGN_WIDTH, 0,
                    GameInfo.SCREEN_WIDTH-GameInfo.CAMPAIGN_WIDTH, GameInfo.SCREEN_HEIGHT-50, null);
        }

        g.drawImage(background, 0, 0, GameInfo.CAMPAIGN_WIDTH, GameInfo.SCREEN_HEIGHT, null);

        // Vẽ các GameObject
        for (GameObject obj : GameInfo.getInstance().getCurrentObjects()) {
            obj.render(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(GameInfo.getInstance().getSmallFont());

        // Rendering game states
        // chinh ve switch case cho gon
        switch (state) {
            case Ready:
                if (GameInfo.getInstance().isMultiplayer) {
                    MultiplayerContext ctx = MultiplayerContext.getInstance();
                    String readyText = ctx.isPlayer1() ? 
                        "PRESS SPACE TO START" : 
                        "WAITING FOR PLAYER 1 TO START";
                    int readyPos = (GameInfo.CAMPAIGN_WIDTH - g.getFontMetrics().stringWidth(readyText)) / 2;
                    g.drawString(readyText, readyPos, 400);
                } else {
                    String pressToReadyText = "PRESS SPACE TO START";
                    int pressToReadyPos = (GameInfo.CAMPAIGN_WIDTH - g.getFontMetrics().stringWidth(pressToReadyText)) / 2;
                    g.drawString(pressToReadyText, pressToReadyPos, 400);
                }
                break;

            case LevelComplete:
                g.setFont(GameInfo.getInstance().getTitleFont());

                String levelCompleteText = "Level Completed!";
                int levelCompletePos = (GameInfo.CAMPAIGN_WIDTH - g.getFontMetrics().stringWidth(levelCompleteText)) / 2;
                g.drawString(levelCompleteText, levelCompletePos, GameInfo.SCREEN_HEIGHT / 2);

                g.setFont(GameInfo.getInstance().getSmallFont());

                break;

            case Pause:
                // Nếu đang pause, vẽ một lớp overlay tối
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 0.5f)); // màu đen với độ trong suốt 50%
                g2d.fillRect(0, 0, GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);

            default:
                break;
        }   

        // Rendering score
        String currentScoreText = String.format("SCORE: %06d", score.getPlayerScore());
        int currentScorePos = (GameInfo.SCREEN_WIDTH * 4 / 3 + g.getFontMetrics().stringWidth(currentScoreText)) / 2;
        g.drawString(currentScoreText, currentScorePos, 300);
    }
}
