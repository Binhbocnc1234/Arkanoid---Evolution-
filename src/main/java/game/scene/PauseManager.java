package game.scene;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

import game.UI.MyButton;
import game.UI.MyLabel;
import game.info.GameInfo;
import game.soundmanager.SoundManager;

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
    private float volumePercent = 50f;


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
        int menuHeight = 480;
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
        
        MyButton volumeButton = new MyButton("Volume", menuWidth / 2, 360, 200, 50);
        pauseMenu.add(volumeButton);
        
        JSlider volumeJs = new JSlider(0, 100,(int) volumePercent);
        volumeJs.setBounds(menuWidth / 2 - 100, 400, 200, 50);
        volumeJs.setMajorTickSpacing(25);
        volumeJs.setMinorTickSpacing(5);
        volumeJs.setPaintTicks(true);
        volumeJs.setPaintLabels(true);
        volumeJs.setVisible(false);
        pauseMenu.add(volumeJs);

        volumeButton.addActionListener(e -> {
            SoundManager.playSound("button");
            volumeJs.setVisible(!volumeJs.isVisible());
        });
        volumeJs.addChangeListener(e -> {
            volumePercent = volumeJs.getValue();
            SoundManager.setSpecificVolume("background", volumePercent);
            System.out.println("Am luong: " + volumePercent + "%");
        });
        
        parent.add(pauseMenu);

        // Create and add pause button (top-right of campaign area)
        pauseButton = new JButton();
        pauseButton.setBounds(GameInfo.CAMPAIGN_WIDTH - 60, 5, 40, 40);
        pauseButton.setIcon(new ImageIcon(getClass().getResource("/img/Pause button.png")));
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusPainted(false);
        pauseButton.addActionListener(e -> {
            SoundManager.playSound("button");
            show();
        });
        parent.add(pauseButton);
    }


    private void show() {
        if (pauseMenu != null) {
            battleManager.state = BattleState.Pause;
            pauseMenu.setVisible(true);
            SoundManager.stopSound("background");
        }
    }

    private void hide() {
        if (pauseMenu != null) {
            battleManager.state = BattleState.Fighting;
            pauseMenu.setVisible(false);
            SoundManager.playSoundLoop("background");
        }
    }

}
