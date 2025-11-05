package game.scene;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import game.info.GameInfo;

public class GameManager extends JFrame {
    public static GameManager instance;

    public GameManager() {
        instance = this;
        setTitle("Arkanoid Evolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
        GameManager.instance.switchTo(new Lobby());
    }

    /* Chuyển sang scene mới và bỏ scene cũ */
    public void switchTo(JPanel panel) {
        JPanel oldPanel = (JPanel) getContentPane();
        if (oldPanel instanceof IDisposable disposable)
            disposable.dispose();
        setContentPane(panel);
        revalidate();
        repaint();
        panel.setFocusable(true);
        panel.setVisible(true);
        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
    }

    /*Điểm bắt đầu của game */
    public static void main(String[] args) {
        // Nó đảm bảo toàn bộ code GUI (JFrame, JPanel, nút, v.v.) chạy trong "Event Dispatch Thread"
        SwingUtilities.invokeLater(() -> new GameManager());
    }
}
