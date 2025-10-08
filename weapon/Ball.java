package weapon;
import brick.*;
import gobj.*;
import info.*;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class Ball extends MovableObject{
    private final float diameter;
    private final Paddle paddle;
    private boolean isPowerUp = false;
    private final ArrayList<float[]> trail = new ArrayList<>();
    private static final int MAX_TRAIL = 6;
    private boolean hasCollidedThisFrame = false;

    public Ball(float x, float y, String imagePath, float diameter, Paddle paddle) {
        super(x, y, diameter, diameter, imagePath);
        this.paddle = paddle;
        this.diameter = diameter;
        this.setVelocity(0, 10f);
    }

    /*
     * Kiểm tra xem Ball va chạm với cạnh nào của vật thể
     * 
     * @param gameObj vật thể cần kiểm tra
     * Trả về Direction.Top nếu va chạm với cạnh trên...
     */
    private Direction intersect(GameObject obj) {
        float rectLeft   = obj.getX() - obj.getWidth() / 2f;
        float rectRight  = obj.getX() + obj.getWidth() / 2f;
        float rectTop    = obj.getY() - obj.getHeight() / 2f;
        float rectBottom = obj.getY() + obj.getHeight() / 2f;

        float radius = this.diameter / 2f;
        float closestX = Math.max(rectLeft, Math.min(this.x, rectRight));
        float closestY = Math.max(rectTop, Math.min(this.y, rectBottom));
        float distX = this.x - closestX;
        float distY = this.y - closestY;
        boolean isCollide = (distX * distX + distY * distY) <= (radius * radius);

        if (!isCollide) return Direction.None;

        // Xác định cạnh va chạm dựa trên hướng di chuyển và vị trí tiếp xúc
        float overlapLeft   = Math.abs((this.x + radius) - rectLeft);
        float overlapRight  = Math.abs((this.x - radius) - rectRight);
        float overlapTop    = Math.abs((this.y + radius) - rectTop);
        float overlapBottom = Math.abs((this.y - radius) - rectBottom);

        float minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

        if (minOverlap == overlapLeft) return Direction.Left;
        if (minOverlap == overlapRight) return Direction.Right;
        if (minOverlap == overlapTop) return Direction.Top;
        return Direction.Down;
    }
    
    @Override public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        float alpha = 0.5f;
        float trail_diameter = diameter ;
        for (int i = trail.size() - 1; i >= 0; i--) {
            float[] pos = trail.get(i);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.CYAN);
            g2d.drawImage(image, (int) (pos[0] - trail_diameter / 2f), (int) (pos[1] - trail_diameter / 2f), (int) trail_diameter, (int) trail_diameter,  null);
            //g2d.fillOval((int) (pos[0] - trail_diameter / 2f), (int) (pos[1] - trail_diameter / 2f),(int) trail_diameter,(int) trail_diameter);
            alpha -= 0.08f;
            trail_diameter -= 1.8f;
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f ));
        super.render(g);
        
    }

    @Override
    public void update() {
        hasCollidedThisFrame = false;
        move();
        trail.add(new float[]{x,y});
        if (trail.size() > MAX_TRAIL) {
            trail.remove(0);
        }

        // nảy khi chạm tường trái hoặc phải
        if (x - diameter / 2f <= 0) {
            BounceOff(Direction.Left);
        }

        if (x + diameter >= GameInfo.SCREEN_WIDTH) {
            BounceOff(Direction.Right);
        }

        // nảy khi chạm trên hoặc dưới
        if (y - diameter / 2f <= 0) {
            BounceOff(Direction.Top);
        }

        if (y + diameter >= GameInfo.SCREEN_HEIGHT) {
            // thêm dừng trò chơi sau
            BounceOff(Direction.Down);
        }

        // kiểm tra khi bóng va chạm với paddle thì bật ra
        if (paddle != null && intersect(paddle) != Direction.None) {
            this.y = paddle.getY() - paddle.getHeight() / 2f - diameter / 2f;
            dy = -Math.abs(dy); // bật trở lại
            //chạm giữa thì bật lại giữa, chạm lệch trái thì bật lệch trái, chạm lệch phải thì bật lệnh phải
            float hitPos = ((this.x - paddle.getX()) / (paddle.getWidth() / 2f)); 
            dx = hitPos * 6.9f;
        }

        // Kiểm tra khi bóng va chạm với Brick
        // TO DO: Bug: Hiện tại collision check không hoạt động khi Ball đi vào chính giữa 2 Brick
        for(GameObject obj : GameInfo.getInstance().getObjects()){
            if (obj instanceof Brick){
                Direction collideAns = intersect(obj);
                Brick brick = (Brick)obj;
                if (!hasCollidedThisFrame && collideAns != Direction.None){
                    brick.takeDamage(1);
                    brick.setInvulnerable(10);   // Added iframe
                    hasCollidedThisFrame = true;
                }

                if (collideAns == Direction.Down){
                    BounceOff(Direction.Top);
                }
                else if (collideAns == Direction.Top){
                    BounceOff(Direction.Down);
                }
                else if (collideAns == Direction.Left){
                    BounceOff(Direction.Right);
                }
                else if (collideAns == Direction.Right){
                    BounceOff(Direction.Left);
                }
            }
        }
    }
    

    public void BounceOff(Direction dir) {

        if (dir == Direction.Top) {
            y += 2;
            dy = -dy;
        }
        else if (dir == Direction.Down) {
            y -= 2;
            dy = -dy;
        }
        else if (dir == Direction.Left) {
            x += 2;
            dx = -dx;
        }
        else {
            x -= 2;
            dx = -dx;
        }
    }

    /**
     * Reset the Ball instance to its default position.
     * 
     */
    public void reset() {
        this.setPosition(GameInfo.SCREEN_WIDTH / 2f, 300);
        this.setVelocity(0, 10f);
    }

}
