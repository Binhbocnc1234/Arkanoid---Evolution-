package weapon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import framework.Direction;
import info.GameInfo;
import brick.*;

public class BallTest {
    //Ball b = new Ball(50, 50, "", 25);
    
    @Test
    public void moveTest() {
        Ball b = new Ball(50, 50, "", 25);
        b.move();
        assertEquals(50, b.getX());
        assertEquals(40, b.getY());
    }

    @Test
    public void paddleCollisionTest() {
        Ball b = new Ball(300, 490, "", 25);
        b.setVelocity(0, 20);
        Paddle p = new Paddle(300, 500, 0, 0, 0, null);
        Direction dir = b.intersect(p);
        assertEquals(Direction.Top, dir);
        assertEquals(-20, b.getDy());
        assertEquals(0, b.getDx());
    }

    @Test
    public void collideTopBrickTest() {
        Ball b = new Ball(100, 110, "", 25);
        b.setVelocity(0, 10);
        b.setPrevX(b.getX());
        b.setPrevY(b.getY());
        b.move();
        Brick bk = BrickFactory.createBrick(1, 100, 150, 60, 40); 
        assertEquals(Direction.Top, b.intersect(bk));
        assertEquals(-10, b.getDy());
        assertEquals(0, b.getDx());
    }

    @Test
    public void collideDownBrickTest() {
        Ball b = new Ball(0, 190, "", 25);
        b.setVelocity(0, -10);
        b.setPrevX(b.getX());
        b.setPrevY(b.getY());
        b.move();
        //assertEquals(190, b.getY());
        Brick bk = new NormalBrick(0, 150, 60, 40); 
        assertEquals(Direction.Down, b.intersect(bk));
        assertEquals(10, b.getDy());
        assertEquals(0, b.getDx());
    }

@Test
    public void collideLeftBrickTest() {
        Ball b = new Ball(50, 130, "", 25);
        b.setVelocity(10, 10);
        b.setPrevX(b.getX());
        b.setPrevY(b.getY());
        b.move();
        Brick bk = BrickFactory.createBrick(2, 100, 150, 60, 40); 
        assertEquals(Direction.Left, b.intersect(bk));
        assertEquals(10, b.getDy());
        assertEquals(-10, b.getDx());
    }

    @Test
    public void collideRightBrickTest() {
        Ball b = new Ball(150, 170, "", 25);
        b.setVelocity(-10, -10);
        b.setPrevX(b.getX());
        b.setPrevY(b.getY());
        b.move();
        Brick bk = BrickFactory.createBrick(2, 100, 150, 60, 40); 
        assertEquals(Direction.Right, b.intersect(bk));
        assertEquals(-10, b.getDy());
        assertEquals(10, b.getDx());
    }

    @Test
    public void brickPiercingTest() {
        Ball b = new Ball(140, 140, "", 25);
        Ball.PIERCING = true;
        b.setVelocity(-10, -10);
        Brick diamond = new DiamondBrick(70, 120, 60, 40);
        Brick gold = new GoldBrick(130, 120, 60, 40);
        Brick steel = new SteelBrick(130, 80, 60, 40);
        Brick normal = new NormalBrick(70, 80, 60, 40);
        GameInfo.getInstance().addGameObject(diamond);
        GameInfo.getInstance().addGameObject(gold);
        GameInfo.getInstance().addGameObject(steel);
        GameInfo.getInstance().addGameObject(normal);
        for (int i = 0; i < 8; i++) {
            /**b.setPrevX(b.getX());
            b.setPrevY(b.getY());
            b.move();
            b.intersect(bk1);
            b.intersect(bk2);
            b.intersect(bk3);
            b.intersect(bk4);*/
            b.update();
        }
        assertEquals(3, diamond.getHp());
        assertEquals(2, gold.getHp());
        assertEquals(1, steel.getHp());
        assertEquals(0, normal.getHp());
    }
}
