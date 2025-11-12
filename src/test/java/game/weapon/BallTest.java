package game.weapon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import game.framework.Direction;
import game.info.GameInfo;
import game.brick.*;

public class BallTest {
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
        Paddle p = new Paddle(300, 500, 100, 50, 0, null);
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

    /**
     * Test xuyên nhiều gạch kiểm tra trạng thái bóng và gạch sau khi bóng đi qua gạch.
    */
    @Test
    public void brickPiercingTest() {
        Ball b = new Ball(100, 160, "", 25);
        Ball.PIERCING = true;
        b.setVelocity(0, -10);
        Brick diamond = new DiamondBrick(70, 120, 60, 40);
        Brick gold = new GoldBrick(130, 120, 60, 40);
        Brick steel = new SteelBrick(130, 80, 60, 40);
        Brick normal = new NormalBrick(70, 80, 60, 40);
        GameInfo.getInstance().getObjects().add(diamond);
        GameInfo.getInstance().getObjects().add(gold);
        GameInfo.getInstance().getObjects().add(steel);
        GameInfo.getInstance().getObjects().add(normal);
        for (int i = 0; i < 8; i++) {
            b.update();
        }
        Ball.PIERCING = false;
        GameInfo.getInstance().getObjects().remove(diamond);
        GameInfo.getInstance().getObjects().remove(gold);
        GameInfo.getInstance().getObjects().remove(steel);
        GameInfo.getInstance().getObjects().remove(normal);
        assertEquals(3, diamond.getHp());
        assertEquals(2, gold.getHp());
        assertEquals(1, steel.getHp());
        assertEquals(0, normal.getHp());
        assertEquals(80, b.getY());
        assertEquals(100, b.getX());
        assertEquals(-10, b.getDy());
        assertEquals(0, b.getDx());
    }

    /**
     * Test xuyên 1 gạch.
     */
    @Test
    public void brickPiercingTest1() {
        Ball b = new Ball(70, 160, "", 25);
        Ball.PIERCING = true;
        b.setVelocity(0, -10);
        Brick brick = new DiamondBrick(70, 120, 60, 40);
        GameInfo.getInstance().getObjects().add(brick);
        for (int i = 0; i < 8; i++) {
            b.update();
        }
        Ball.PIERCING = false;
        GameInfo.getInstance().getObjects().remove(brick);
        assertEquals(3, brick.getHp());
        assertEquals(80, b.getY());
        assertEquals(70, b.getX());
        assertEquals(-10, b.getDy());
        assertEquals(0, b.getDx());
    }
}
