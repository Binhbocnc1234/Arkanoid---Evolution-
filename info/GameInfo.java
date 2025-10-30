package info;

import gobj.GameObject;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * Đúng như tên của nó, class này chỉ chứa THÔNG TIN và 
 * thông tin đó được sử dụng rộng rãi ở nhiều class, không thực hiện logic game ở đây
 */
public class GameInfo {

    /* static field phục vụ cho coders, nếu bạn muốn chạy game ở chế độ test thì để giá trị là true */
    public static boolean isTesting = true;
    public static final int CAMPAIGN_WIDTH = 600,
            SCREEN_HEIGHT = 700, SCREEN_WIDTH = 900;
    public static final int FPS = 60;
    public static final float SLOWMOTION_RATIO = 3f;
    public boolean isMultiplayer = false;
    public boolean isSlowmotion = false;
    private Font titleFont, smallFont;
    private static GameInfo instance;
    public boolean isPaused = false;
    private final List<GameObject> container;
    private final List<GameObject> pendingGameObjects;
    private int unlockedLevel = 1;

    GameInfo() {
        container = new ArrayList<>();
        pendingGameObjects = new ArrayList<>();
        try {
            titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/Radiant-Kingdom.ttf")).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(titleFont);
        } catch (Exception e) {
            titleFont = new Font("Serif", Font.BOLD, 32);
        }
        try {
            smallFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/Radiant-Kingdom.ttf")).deriveFont(30f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(smallFont);
        } catch (Exception e) {
            smallFont = new Font("Serif", Font.BOLD, 32);
        }
    }
    public void Initialize() {
        
        container.clear();
    }

    public static GameInfo getInstance() {
        if (instance == null) {
            instance = new GameInfo();
        }
        return instance;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public Font getSmallFont() {
        return smallFont;
    }
    /*
    * Trả về danh sách gốc, hàm lỗi thời, khuyên không dùng!
    */
    public List<GameObject> getObjects() {
        return container;
    }

    /*
     * Khi tạo GameObject mới thì luôn phải thêm vào container, nếu không thì GameObject
     * không gọi được hàm update() 
     */
    public void addGameObject(GameObject gameObject) {
        pendingGameObjects.add(gameObject);
    }

    /*
     * Đưa những object ở trạng thái pending thành object chính thức
     * Hiện tại chỉ có BattleManager::gameloop() mới gọi hàm này
     */
    public void flushGameObject() {
        for (GameObject gobj : pendingGameObjects) {
            container.add(gobj);
        }
        pendingGameObjects.clear();
    }
    /*
     * Trả về danh sách copy, phục vụ cho việc duyệt GameObject
     */
    public List<GameObject> getCurrentObjects() {
        List<GameObject> list = new ArrayList<>(container);
        list.sort(Comparator.comparingInt(o -> o.sortingIndex));
        return list;
    }

    public int getUnlockedLevel() {
        return unlockedLevel;
    }

    public void setUnlockedLevel(int level) {
        if (level > unlockedLevel) this.unlockedLevel = level;
    }

    public void resetUnlockedLevel() {
        this.unlockedLevel = 1;
    }

}
