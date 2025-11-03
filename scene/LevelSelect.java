package scene;

import UI.*;
import info.GameInfo;
import java.awt.Color;
import javax.swing.JPanel;
import level.LevelManager;
import soundmanager.SoundManager;


public class LevelSelect extends JPanel{
    private final int MAX_LEVEL = 10;
    private final MyButton[] levelButtons;
    private final MyButton returnButton;
    private final MyLabel levelselLabel;
    private boolean isMultiplayer;

    /**
     * Initializing LevelSelect screen.
     * 
     * @param isMultiplayer     boolean
     */
    public LevelSelect(boolean isMultiplayer) {

        this.isMultiplayer = isMultiplayer;

        setLayout(null);
        setBackground(new Color(30, 20, 60));

        /* Adding scene label */
        levelselLabel = new MyLabel("Level Select",
                    GameInfo.SCREEN_WIDTH/2, 100, 500, 100);
        levelselLabel.setForeground(new Color(180, 160, 255));
        add(levelselLabel);
        
        /* Adding level buttons */
        levelButtons = new MyButton[MAX_LEVEL];
        int LevelButtonWidth = 150;
        int LevelButtonHeight = 75;

                //Parameters for setting up button
        int leftSideButtonX = 250;
        int rightSideButtonX = GameInfo.SCREEN_WIDTH - 250;
        int upperBoundButtonY = 200;
        int gap = 100;
        int layer = 0;

        int currLevel = GameInfo.getInstance().getUnlockedLevel();

        for (int i = 0; i < MAX_LEVEL; i++) {
            /* If the level hasn't got cleared, disable that level's button. */
            int level = i + 1;
            Color color = new Color(120, 80, 200);

            if (level > currLevel) color = Color.GRAY;

                // odd = left, even = right
            if (level % 2 == 1) {
                levelButtons[i] = new MyButton(((Integer) level).toString(), 
                                                leftSideButtonX, upperBoundButtonY + gap * layer, 
                                                LevelButtonWidth, LevelButtonHeight,
                                                color);
            }

            else if (level % 2 == 0) {
                levelButtons[i] = new MyButton(((Integer) level).toString(), 
                                                rightSideButtonX, upperBoundButtonY + gap * layer, 
                                                LevelButtonWidth, LevelButtonHeight,
                                                color);
                layer += 1;
            }

            if (level > currLevel) levelButtons[i].setEnabled(false);

            levelButtons[i].addActionListener(e -> {
                LevelManager.getInstance().setCurrentLevel(level);
                GameManager.instance.switchTo(new BattleManager(this.isMultiplayer));
            });

            add(levelButtons[i]);
        }

        returnButton = new MyButton("Return", GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2, 100, 70);
        returnButton.addActionListener(e -> {
            SoundManager.playSound("button");
            GameManager.instance.switchTo(new Lobby());
        });
        add(returnButton);
    }
}
