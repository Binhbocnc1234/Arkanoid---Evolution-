import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import gobj.*;

public class Ball extends MovableObject{
    private float diameter;
    private Paddle paddle;

    protected Ball(float x, float y, String imagePath, float diameter, Paddle paddle) {
        super(x, y, diameter, diameter, imagePath);
        this.paddle = paddle;
        this.diameter = diameter;
        this.setVelocity(0, 10f);
        
    }

    private boolean intersect(Paddle paddle) {
        float left = paddle.getX() - paddle.getWidth() / 2f;
        float right  = paddle.getX() + paddle.getWidth() / 2f;
        float bot = paddle.getY() - paddle.getHeight() / 2f;
        float top = paddle.getY() + paddle.getHeight() / 2f;
        float radius = this.diameter / 2f;
        float A_closetX = Math.max(left, Math.min(this.x, right));
        float A_closetY = Math.max(bot, Math.min(this.y, top));
        float focalToA_X = this.x - A_closetX;
        float focalToA_Y = this.y - A_closetY;
        return (focalToA_X * focalToA_X + focalToA_Y * focalToA_Y) <= (radius * radius);
    }

    @Override public void render(Graphics g) {
        if (image != null ) {
            g.drawImage(image, (int) (x - diameter / 2f), (int) (y - diameter / 2f), (int) diameter, (int) diameter, null);
        }
        else {
            g.setColor(Color.BLUE);
            g.fillOval((int) (x - diameter / 2f), (int) (y - diameter / 2f), (int) diameter, (int) diameter);
        }
        
    }

    @Override public void update() {
        move();

        // nảy khi chạm tường trái hoặc phải
        if (x - diameter / 2f <= 0) {
            x = diameter / 2f;
            dx = - dx;
        }

        if (x + diameter / 2f >= 800) {
            x = 800 - diameter / 2f;
            dx = - dx;
        }

        // nảy khi chạm trên hoặc dưới
        if (y - diameter / 2f <= 0) {
            y = diameter / 2f;
            dy = -dy;
        }

        if (y + diameter / 2f >= 600) {
            // thêm dừng trò chơi sau
            //y = paddle.getY() - paddle.getHeight() / 2f - diameter / 2f;
            dy = -dy;
        }

        // kiểm tra khi bóng va chạm với paddle thì bật ra
        if (paddle != null && intersect(paddle)) {
            this.y = paddle.getY() - paddle.getHeight() / 2f - diameter / 2f;
            dy = - Math.abs(dy); // bật trở lại
            float hitPos = ((this.x - paddle.getX()) / (paddle.getWidth() / 2f)); //chạm giữa thì bật lại giữa, chạm lệch trái thì bật lệch trái, chạm lệch phải thì bật lệnh phải
            dx = hitPos * 4f; 
        }
    }
}
