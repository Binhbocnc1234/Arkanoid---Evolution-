package scene;
import info.GameInfo;
import javax.swing.*;
import scene.Lobby;

public class GameManager extends JFrame {
    public static GameManager instance;
    private JPanel activePanel;
    public GameManager() {
        instance = this;
        setTitle("Arkanoid Evolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
        
        GameManager.instance.switchTo(new Lobby());
    }

    public void switchTo(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
        panel.setFocusable(true);
        panel.setVisible(true);
        activePanel = panel;
        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
    }

    public static void main(String[] args) {
        // Nó đảm bảo toàn bộ code GUI (JFrame, JPanel, nút, v.v.) chạy trong "Event Dispatch Thread"
        SwingUtilities.invokeLater(() -> new GameManager());
    }
}
