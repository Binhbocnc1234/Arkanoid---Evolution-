package game.brick;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import game.info.GameInfo;

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

    /**
     * Test brick nổ với các gạch xung quanh.
     */
    @Test
    public void tntSelfDestroyTest () {
        Brick tnt = new TNTBrick(100, 100, 80, 40);
        Brick diamond = new DiamondBrick(100, 180, 0, 0);
        Brick gold = new GoldBrick(100, 20, 0, 0);
        Brick steel = new SteelBrick(20, 100, 0, 0);
        Brick normal = new NormalBrick(180, 100, 0, 0);
        GameInfo.getInstance().getObjects().add(diamond);
        GameInfo.getInstance().getObjects().add(gold);
        GameInfo.getInstance().getObjects().add(steel);
        GameInfo.getInstance().getObjects().add(normal);
        tnt.selfDestroy();
        GameInfo.getInstance().getObjects().remove(diamond);
        GameInfo.getInstance().getObjects().remove(gold);
        GameInfo.getInstance().getObjects().remove(steel);
        GameInfo.getInstance().getObjects().remove(normal);
        assertEquals(3, diamond.getHp());
        assertEquals(2, gold.getHp());
        assertEquals(1, steel.getHp());
        assertEquals(0, normal.getHp());
    }
}
