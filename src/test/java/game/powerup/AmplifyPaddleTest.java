package game.powerup;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import game.info.GameInfo;
import game.weapon.Paddle;

public class AmplifyPaddleTest {
    @Test
    public void applyPowerUpTest() {
        Paddle p = new Paddle(200, 500, 120, 30, 0, null);
        Paddle p1 = new Paddle(400, 500, 120, 30, 0, null);
        GameInfo.getInstance().getObjects().add(p);
        GameInfo.getInstance().getObjects().add(p1);
        PowerUp pu = new AmplifyPaddle();
        pu.setPosition(200, 490);
        pu.setVelocity(0, 10);
        pu.update();
        GameInfo.getInstance().getObjects().remove(p);
        GameInfo.getInstance().getObjects().remove(p1);
        assertEquals(140, p.getWidth());
        assertEquals(35, p.getHeight());
        assertEquals(120, p1.getWidth());
        assertEquals(30, p1.getHeight());
    }

    /**
     * Test giới hạn paddle được tăng kích thước khi nhận nhiều amplifyPaddle 
     */
    @Test
    public void maxPowerUpTest () {
        Paddle p = new Paddle(200, 500, 120, 30, 0, null);
        GameInfo.getInstance().getObjects().add(p);
        for (int i = 0; i < 12; i++) {
            PowerUp pu = new AmplifyPaddle();
            pu.setPosition(200, 490);
            pu.setVelocity(0, -10);
            for (int j = 0; j < 2; j++) {
                pu.update();
            }
        }
        GameInfo.getInstance().getObjects().remove(p);
        assertEquals(AmplifyPaddle.MAX_HEIGHT, p.getHeight());
        assertEquals(AmplifyPaddle.MAX_WIDTH, p.getWidth());
    }
}
