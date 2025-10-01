package info;

import gobj.GameObject;
import java.util.ArrayList;
import java.util.List;

/*
 * Đúng như tên của nó, class này chỉ chứa THÔNG TIN và 
 * thông tin đó được sử dụng rộng rãi ở nhiều class, không thực hiện logic game ở đây
 */
public class GameInfo {

    /* static field phục vụ cho coders, nếu bạn muốn chạy game ở chế độ test thì để giá trị là true */
    public static boolean isTesting = true;
    public static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 700;
    private static GameInfo instance;
    private final List<GameObject> container;

    GameInfo() {
        container = new ArrayList<>();
    }

    public static GameInfo getInstance() {
        if (instance == null) {
            instance = new GameInfo();
        }
        return instance;
    }

    public List<GameObject> getObjects() {
        return container;
    }
}
