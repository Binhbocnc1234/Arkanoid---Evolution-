package powerup;

import java.util.Random;

public class PowerUpSummoner {
    
    //private static Class<?>[] powerUps = new Class<?>[] {TripleBall.class, AmplifyPaddle.class};
    private static PowerUp[] powerUps = new PowerUp[] {new TripleBall(), new AmplifyPaddle()};
    private static final Random gacha = new Random();

    /**
     * Summon a random PowerUp upon birck breaking hit the chance;
     * @return random new PowerUp
     */
    public static PowerUp summonPowerUp() {
        float g = gacha.nextFloat();
        for (PowerUp p : powerUps) {
            g -= p.getRarity();
            if (g <= 0) {
                return p.summon();
            }
        }
        return powerUps[powerUps.length - 1].summon();
    }
}