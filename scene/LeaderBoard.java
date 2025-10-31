package scene;

import javax.swing.*;
import java.awt.*;
import UI.MyButton;
import UI.MyLabel;
import java.util.List;

import info.GameInfo;
import score.HighScores;
import soundmanager.SoundManager;

public class LeaderBoard extends JPanel {
    private GalaxyBackground background;
    private Timer animationTimer;

    public LeaderBoard() {
        setLayout(null);
        setBackground(new Color(30, 20, 60));

        // Initialize the galaxy background with 100 stars
        background = new GalaxyBackground(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT, 100);
        
        // Set up animation timer to update background
        animationTimer = new Timer(16, e -> repaint()); // approximately 60 FPS
        animationTimer.start();

        MyLabel title = new MyLabel("High Scores", GameInfo.SCREEN_WIDTH / 2, 100, 500, 100);
        title.setForeground(new Color(180, 160,255));
        add(title);

        List<HighScores.ScoreEntry> scoreList = HighScores.getInstance().getScore();
        int startY = 200;
        int gap = 50;

        if (scoreList.isEmpty()) {
            MyLabel emptyMyLabel = new MyLabel("No scores have been recorded", GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT / 2, 800, 40);
            add(emptyMyLabel);
        } else {
            for (int i = 0; i < scoreList.size(); i++) {
                HighScores.ScoreEntry entry = scoreList.get(i);
                String text = String.format("%d. %-15s %d", (i + 1), entry.getName(), entry.getScore());

                MyLabel scoreLabel = new MyLabel(text, GameInfo.SCREEN_WIDTH / 2, startY + (i * gap), 600, 40);
                scoreLabel.setFont(GameInfo.getInstance().getSmallFont().deriveFont(32f));
                scoreLabel.setForeground(Color.WHITE);
                add(scoreLabel);
            }
        }

        MyButton rtnButton = new MyButton("Return", GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT - 150, 200, 70);
        rtnButton.addActionListener(e -> {
            SoundManager.playSound("button");
            animationTimer.stop();
            GameManager.instance.switchTo(new Lobby());
        });
        add(rtnButton);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        background.update(g);
    }
}
