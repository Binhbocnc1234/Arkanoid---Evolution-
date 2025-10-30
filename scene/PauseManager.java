package scene;

import UI.*;
import info.GameInfo;
import java.awt.*;
import javax.swing.*;
import soundmanager.SoundManager;

/**
 * Encapsulates the pause menu UI and pause button.
 * The class does not change game state itself; callers should
 * provide callbacks for continue/return actions so game logic
 * (e.g. stopping/starting sounds or changing state) remains in
 * the caller (BattleManager).
 */
public class PauseManager {
    private final BattleManager battleManager;
    private final JPanel parent;
    private JPanel pauseMenu;
    private JButton pauseButton;

    public PauseManager(BattleManager battleManager) {
        this.battleManager = battleManager;
        this.parent = battleManager;
        createPauseMenu();
    }

    private void createPauseMenu() {
        pauseMenu = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(30, 20, 60)); // dark purple overlay
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };

        int menuWidth = 300;
        int menuHeight = 400;
        pauseMenu.setBounds((GameInfo.CAMPAIGN_WIDTH - menuWidth) / 2,
                (GameInfo.SCREEN_HEIGHT - menuHeight) / 2,
                menuWidth,
                menuHeight);
        pauseMenu.setVisible(false);

        MyLabel pauseLabel = new MyLabel("PAUSE", menuWidth / 2, 80, 200, 48);
        pauseMenu.add(pauseLabel);

        MyButton continueBtn = new MyButton("Continue", menuWidth / 2, 200, 200, 50);
        continueBtn.addActionListener(e -> {
            SoundManager.playSound("button");
            hide();
        });
        pauseMenu.add(continueBtn);

        MyButton returnBtn = new MyButton("Return to Lobby", menuWidth / 2, 280, 200, 50);
        returnBtn.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new Lobby());
        });
        pauseMenu.add(returnBtn);

        parent.add(pauseMenu);

        // Create and add pause button (top-right of campaign area)
        pauseButton = new JButton();
        pauseButton.setBounds(GameInfo.CAMPAIGN_WIDTH - 60, 5, 40, 40);
        pauseButton.setIcon(new ImageIcon("assets/img/Pause button.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusPainted(false);
        pauseButton.addActionListener(e -> {
            SoundManager.playSound("button");
            show();
        });
        parent.add(pauseButton);
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public void show() {
        if (pauseMenu != null) {
            battleManager.state = BattleState.Pause;
            pauseMenu.setVisible(true);
            SoundManager.stopSound("background");
        }
    }

    public void hide() {
        if (pauseMenu != null) {
            battleManager.state = BattleState.Fighting;
            pauseMenu.setVisible(false);
            SoundManager.playSoundLoop("background");
        }
    }

    public boolean isVisible() {
        return pauseMenu != null && pauseMenu.isVisible();
    }
}
