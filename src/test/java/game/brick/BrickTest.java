package game.brick;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BrickTest {
    @Test
    public void takeDamageTest() {
        Brick b = BrickFactory.createBrick(2, 0, 0, 0, 0);
        b.takeDamage(1);
        assertEquals(1, b.hp);
    }

    @Test
    public void obsidianTakeDamageTest () {
        Brick b = BrickFactory.createBrick(5, 0, 0, 0, 0);
        int baseHp = b.hp;
        b.takeDamage(2);
        assertEquals(baseHp, b.hp);
    }
    
    
}
