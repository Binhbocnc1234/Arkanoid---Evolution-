import gobj.MovableObject;

public class Paddle extends MovableObject {

    private float speed;
    public Paddle(float x, float y, float w, float h, float speed, String imagePath) {
        super(x, y, w, h, imagePath);
        this.speed = speed;
    }
}