package game.level;

import java.util.ArrayList;
import java.util.List;

import game.brick.Brick;
import game.gobj.GameObject;
import game.info.GameInfo;
import game.weapon.Ball;

public class LevelManager {

    private static LevelManager instance;
    private final ArrayList<String> levelPaths;
    private int currentLevel;

    /**
     * Constructor.
     */
    private LevelManager() {
        levelPaths = new ArrayList<>();
        preloadLevels("/level");
        currentLevel = 0;
    }

    /**
     * Initiate the LevelManager instance.
     * 
     * @return      a LevelManager instance
     */
    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    /**
     * Preload all the level files into the levelPaths ArrayList once initiate the LevelManager instance.
     * 
     * @param folderPath        folderPath ArrayList
     */
    private void preloadLevels(String folderPath) {
        // When packaged as a JAR (Maven), resources must be loaded via classpath.
        // We attempt to find level files named level1.txt, level2.txt, ... under the given folderPath.
        // Stop after a reasonable upper bound.
        final int MAX_TRY = 10;
        for (int i = 1; i <= MAX_TRY; i++) {
            String resourcePath = String.format("%s/level%d.txt", folderPath, i);
            if (LevelManager.class.getResource(resourcePath) != null) {
                levelPaths.add(resourcePath);
            }
        }
        // If none found, try a fallback: look for a single data.txt
        if (levelPaths.isEmpty()) {
            String single = folderPath + "/data.txt";
            if (LevelManager.class.getResource(single) != null) {
                levelPaths.add(single);
            }
        }
    }

    /**
     * Load the current level.
     */
    public void loadCurrentLevel() {
        for (GameObject obj : GameInfo.getInstance().getObjects()) {
            if (obj instanceof Brick brick) {
                brick.selfDestroy();
            }       // Deleting all remaining Brick instances
        }

        float brickW = 0;
        if (GameInfo.getInstance().isMultiplayer) {
            brickW = GameInfo.SCREEN_WIDTH / 10f;
        } else {
            brickW = GameInfo.CAMPAIGN_WIDTH / 10f;
        }

        List<GameObject> levelBricks = LevelLoader.loadLevel(
            levelPaths.get(currentLevel),
            brickW, 25f,
            brickW / 2, 12.5f
        );

        for (GameObject brick : levelBricks) {
            GameInfo.getInstance().addGameObject(brick);
        }

        Ball.PIERCING = false;      // Safeguard: Revert all Ball's state upon exiting the level.
    }

    /**
     * Switch to the next level.
     */
    public void switchToNextLevel() {
        currentLevel++;

        if (currentLevel >= levelPaths.size()) return;
        loadCurrentLevel();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Set the current level.
     * @param level         level num
     */
    public void setCurrentLevel(int level) {
        currentLevel = level - 1;
    }

    /**
     * Reset level counter back to 0, and unlocked level to 1.
     * Effectively, this becomes a new playthrough.
     */
    public void reset() {
        currentLevel = 0;
        GameInfo.getInstance().resetUnlockedLevel();
    }
}