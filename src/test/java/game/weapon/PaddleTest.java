package game.weapon;
import org.junit.jupiter.api.Test;

import game.info.GameInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaddleTest {

    @Test
    public void moveBoundTest () {
        Paddle p = new Paddle(70, 500, 120, 0, 0, null);
        p.setVelocity(-20, 0);
        p.setUp(0, GameInfo.CAMPAIGN_WIDTH, GameInfo.SCREEN_HEIGHT);
        p.setPosition(70, 500);
        p.update();
        assertEquals(p.getWidth() / 2, p.getX());
    }
}
