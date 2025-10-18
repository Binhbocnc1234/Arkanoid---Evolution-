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
    public static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 700;
    private Font mainFont;
    private static GameInfo instance;
    private final List<GameObject> container;

    GameInfo() {
        container = new ArrayList<>();
        try {
            mainFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/Radiant-Kingdom.ttf")).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(mainFont);
        } catch (Exception e) {
            mainFont = new Font("Serif", Font.BOLD, 32);
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

    public Font getFont() {
        return mainFont;
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
        container.add(gameObject);
    }

    /*
     * Trả về danh sách copy, phục vụ cho việc duyệt GameObject
     */
    public List<GameObject> getCurrentObjects() {
        List<GameObject> list = new ArrayList<>(container);
        list.sort(Comparator.comparingInt(o -> o.sortingIndex));
        return list;
    }

}
