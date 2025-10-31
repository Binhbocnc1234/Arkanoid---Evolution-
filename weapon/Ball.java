package weapon;
import brick.*;
import framework.*;
import gobj.*;
import info.*;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import soundmanager.*;

public class Ball extends MovableObject {
    private final float diameter;
    // private boolean isPowerUp = false;
    // private static final int MAX_TRAIL = 6;
    public static boolean PIERCING = false;
    private long lastTrailStamp = 0;
    private float prevX, prevY;
    
    //private boolean hasLeftPaddleInitially = false;

    public Ball(float x, float y, String imagePath, float diameter) {
        super(x, y, diameter, diameter, imagePath);
        this.diameter = diameter;
        this.setVelocity(0, -10f);
    }

    private boolean isIntersect(GameObject obj) {
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

        return isCollide;
    }
    /*
     * Kiểm tra xem Ball va chạm với cạnh nào của vật thể
     * 
     * @param gameObj vật thể cần kiểm tra
     * Trả về Direction.Top nếu va chạm với cạnh trên...
     */
    private Direction intersect(GameObject obj) {
        if (!isIntersect(obj)) {
            return Direction.None;
        }
        float rectLeft = obj.getX() - obj.getWidth() / 2f;
        float rectRight = obj.getX() + obj.getWidth() / 2f;
        float rectTop = obj.getY() - obj.getHeight() / 2f;
        float rectBottom = obj.getY() + obj.getHeight() / 2f;
        float r = this.diameter / 2f;

        // mở rộng hình chữ nhật thêm bán kính quả bóng
        rectLeft -= r;
        rectRight += r;
        rectTop -= r;
        rectBottom += r;

        Vector2 p0 = new Vector2(this.prevX, this.prevY);
        Vector2 p1 = new Vector2(this.x, this.y);
        Vector2 d = new Vector2(p1.x - p0.x, p1.y - p0.y);

        // kiểm tra thời điểm t khi đoạn thẳng cắt AABB mở rộng
        float tEntryX, tExitX, tEntryY, tExitY;
        if (Math.abs(d.x) < 1e-6f) {
            tEntryX = Float.NEGATIVE_INFINITY;
            tExitX = Float.POSITIVE_INFINITY;
        } else {
            float invDx = 1.0f / d.x;
            float tx1 = (rectLeft - p0.x) * invDx;
            float tx2 = (rectRight - p0.x) * invDx;
            tEntryX = Math.min(tx1, tx2);
            tExitX = Math.max(tx1, tx2);
        }

        if (Math.abs(d.y) < 1e-6f) {
            tEntryY = Float.NEGATIVE_INFINITY;
            tExitY = Float.POSITIVE_INFINITY;
        } else {
            float invDy = 1.0f / d.y;
            float ty1 = (rectTop - p0.y) * invDy;
            float ty2 = (rectBottom - p0.y) * invDy;
            tEntryY = Math.min(ty1, ty2);
            tExitY = Math.max(ty1, ty2);
        }

        float tEntry = Math.max(tEntryX, tEntryY);
        float tExit = Math.min(tExitX, tExitY);

        if (tEntry > tExit || tEntry < 0f || tEntry > 1f)
            return Direction.None; // không va chạm trong đoạn di chuyển

        // xác định cạnh va chạm đầu tiên (theo trục nào chạm trước)
        Direction collideAns = Direction.None;
        if (tEntryX > tEntryY)
            collideAns = (d.x > 0) ? Direction.Left : Direction.Right;
        else
            collideAns = (d.y > 0) ? Direction.Top : Direction.Down;
        if (!PIERCING) {
            if (collideAns == Direction.Down) {
                BounceOff(Direction.Top, rectBottom);
            } else if (collideAns == Direction.Top) {
                BounceOff(Direction.Down, rectTop);
            } else if (collideAns == Direction.Left) {
                BounceOff(Direction.Right, rectLeft);
            } else if (collideAns == Direction.Right) {
                BounceOff(Direction.Left, rectRight);
            }
        }
        return collideAns;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // for (int i = trail.size() - 1; i >= 0; i--) {
        //     float[] pos = trail.get(i);
        //     g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        //     g2d.setColor(Color.CYAN);
        //     g2d.drawImage(image, (int) (pos[0] - trail_diameter / 2f), (int) (pos[1] - trail_diameter / 2f), (int) trail_diameter, (int) trail_diameter,  null);
        //     //g2d.fillOval((int) (pos[0] - trail_diameter / 2f), (int) (pos[1] - trail_diameter / 2f),(int) trail_diameter,(int) trail_diameter);
        //     alpha -= 0.08f;
        //     trail_diameter -= 1.8f;
        // }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        super.render(g);

    }

    @Override
    public void update() {
        move();

        //Tạo hiệu ứng đuôi bóng
        long now = System.nanoTime();
        if (now - lastTrailStamp >= 5_000_000L) {
            lastTrailStamp = now;
            GameInfo.getInstance().getObjects().add(new BallTrail(getX(), getY(), diameter, "Ball.png"));
        }

        // nảy khi chạm tường trái hoặc phải
        if (x - diameter / 2f <= 0) {
            BounceOff(Direction.Left, 0);
            SoundManager.playSound("wall");
        }

        int rightBound = (GameInfo.getInstance().isMultiplayer) ? GameInfo.SCREEN_HEIGHT : GameInfo.CAMPAIGN_WIDTH;
        if (x + diameter/2f >= rightBound) {
            BounceOff(Direction.Right, rightBound);
            SoundManager.playSound("wall");
        }

        // nảy khi chạm trên hoặc dưới
        if (y - diameter / 2f <= 0) {
            BounceOff(Direction.Top, 0);
            SoundManager.playSound("wall");
        }

        if (y - diameter >= GameInfo.SCREEN_HEIGHT) {
            selfDestroy();
        }

        // Kiểm tra khi bóng va chạm với Brick
        // TO DO: Bug: Hiện tại collision check không hoạt động khi Ball đi vào chính giữa 2 Brick
        for (GameObject obj : GameInfo.getInstance().getCurrentObjects()) {
            if (obj instanceof Brick) {
                Direction collideAns = intersect(obj);
                Brick brick = (Brick) obj;

                if (brick.getIFrame() <= 0 && collideAns != Direction.None) {
                    brick.takeDamage(1);
                    if (brick.getHp() == 0) {
                        SoundManager.playSound("brick");
                    }
                    brick.setIFrame(10); // Added iframe
                    break;
                }
            }
            else if (obj instanceof Paddle) {
                Paddle paddle = (Paddle) obj;
                if (isIntersect(paddle)) {
                    // kiểm tra khi bóng va chạm với paddle thì bật ra
                    this.y = paddle.getY() - paddle.getHeight() / 2f - diameter / 2f;
                    dy = -Math.abs(dy); // bật trở lại
                    //chạm giữa thì bật lại giữa, chạm lệch trái thì bật lệch trái, chạm lệch phải thì bật lệnh phải
                    float hitPos = ((this.x - paddle.getX()) / (paddle.getWidth() / 2f));
                    dx = hitPos * 6.9f;
                    /*
                    if (hasLeftPaddleInitially == true) {
                        SoundManager.playSound("paddle");
                    }
                    hasLeftPaddleInitially = true;
                    */
                    paddle.recoil();
                    SoundManager.playSound("paddle");
                }
            }
        }
        prevX = x;
        prevY = y;
    }

    public void BounceOff(Direction dir, float bound) {

        if (dir == Direction.Top) {
            y = bound + diameter/2 + 2;
            dy = -dy;
        } else if (dir == Direction.Down) {
            y = bound - diameter/2 - 2;
            dy = -dy;
        } else if (dir == Direction.Left) {
            x = bound + diameter/2 + 2;
            dx = -dx;
        } else {
            x = bound - diameter/2 - 2;
            dx = -dx;
        }
    }

    /**
     * Reset the Ball instance to its default position. 
     */
    public void reset() {
        this.setPosition(GameInfo.CAMPAIGN_WIDTH / 2f, 300);
        this.setVelocity(0, 10f);
        this.PIERCING = false;
    }

}
