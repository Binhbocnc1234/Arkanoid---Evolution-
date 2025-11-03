package game.powerup;

import java.util.Random;

public class PowerUpSummoner {
    private static final PowerUp[] POWER_UPS = 
        new PowerUp[] {new TripleBall(), new AmplifyPaddle(), new PiercingBall()};
    private static final Random gacha = new Random();

    /**
     * Summon a random PowerUp upon birck breaking hit the chance;
     * @return random new PowerUp
     */
    public static PowerUp summonPowerUp() {
        float g = gacha.nextFloat();
        for (PowerUp p : POWER_UPS) {
            g -= p.getRarity();
            if (g <= 0) {
                return p.summon();
            }
        }
        return POWER_UPS[POWER_UPS.length - 1].summon();
    }
}