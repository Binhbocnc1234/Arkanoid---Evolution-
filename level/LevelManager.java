package level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

import brick.Brick;
import gobj.*;
import info.GameInfo;

public class LevelManager {

    private static LevelManager instance;
    private final ArrayList<String> levelPaths;
    private int currentLevel;

    /**
     * Constructor.
     */
    private LevelManager() {
        levelPaths = new ArrayList<>();
        preloadLevels("assets/level");
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
        File folder = new File(folderPath);

        File[] files = folder.listFiles(        // Filter out all files with .txt
            (dir, name) -> name.toLowerCase().endsWith(".txt"));


        /*  
         * Sort the files list by the # of level.
         * FYI, the level file format is "level#.txt".
         */
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File fileA, File fileB) {
                int numA = Integer.parseInt(
                    fileA.getName().replaceAll("\\D+", ""));   // Replace all level with blank
                int numB = Integer.parseInt(
                    fileB.getName().replaceAll("\\D+", ""));

                return Integer.compare(numA, numB);
            }
        });

        for (File file : files) {
            levelPaths.add(file.getPath());
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

        List<GameObject> levelBricks = level.LevelLoader.loadLevel(
            levelPaths.get(currentLevel),
            (GameInfo.CAMPAIGN_WIDTH / 10f), 25f,
            30f, 12f
        );

        for (GameObject brick : levelBricks) {
            GameInfo.getInstance().addGameObject(brick);
        }
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
        currentLevel = level;
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