package game.scene; 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

/* class xử lý về mặt đồ họa, tạo background vũ
    trụ chứa rất nhiều ngôi sao với kích thước khác nhau*/
public class GalaxyBackground {
    private static class Star {
        float x, y, size, speed;
        int alpha;
    }

    private final int width, height;
    private final java.util.List<Star> stars = new ArrayList<>();
    private Random rand = new Random();

    public GalaxyBackground(int width, int height, int numStars) {
        this.width = width;
        this.height = height;

        for (int i = 0; i < numStars; ++i) {
            Star s = new Star();
            s.x = rand.nextInt(width);
            s.y = rand.nextInt(height);
            s.size = 1.0f + rand.nextInt(100) / 40.0f;
            s.speed = 0.2f + rand.nextInt(100) / 200.0f;
            s.alpha = 120 + rand.nextInt(120);
            stars.add(s);
        }
    }

    /* Cập nhật để các Star di chuyển */
    public void update(Graphics g) {
        // update position
        for (Star s : stars) {
            s.y += s.speed;
            if (s.y > height) {
                s.y = 0;
                s.x = rand.nextInt(width);
            }
        }

        // draw stars
        Graphics2D g2 = (Graphics2D) g;
        for (Star s : stars) {
            g2.setColor(new Color(255, 255, 255, s.alpha));
            g2.fillOval((int) s.x, (int) s.y, (int) s.size, (int) s.size);
        }
    }
}






