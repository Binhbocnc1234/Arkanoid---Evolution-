package gobj;
public class MovableObject extends GameObject {
    protected float dx, dy;

    public MovableObject(float x, float y, float width, float height, String imagePath) {
        super(x, y, width, height, imagePath);
    }

    public void move() {
        x += dx;
        y += dy;
    }

    // Getter/Setter
    public void setVelocity(float dx, float dy) {
        this.dx = dx; this.dy = dy;
    }
    public float getDx() { return dx; }
    public float getDy() { return dy; }
}
