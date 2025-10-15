import info.GameInfo;
import javax.swing.*;

public class GameManager extends JFrame {
    public static GameManager instance;

    public GameManager() {
        instance = this;
        setTitle("Arkanoid Evolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);


        // Xử lý khi nhấn Play
        Lobby lobby = new Lobby();
        GameManager.instance.switchTo(lobby);
        lobby.playButton.addActionListener(e -> {
            lobby.setVisible(false);
            switchTo(new BattleManager());
        });
    }

    public void switchTo(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
        panel.setFocusable(true);
        // panel.requestFocusWindow();
    }

    public static void main(String[] args) {
        // Nó đảm bảo toàn bộ code GUI (JFrame, JPanel, nút, v.v.) chạy trong "Event Dispatch Thread"
        GameManager.instance = new GameManager();
    }
}
