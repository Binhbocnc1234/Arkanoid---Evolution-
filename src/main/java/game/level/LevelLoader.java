package game.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import game.brick.Brick;
import game.brick.BrickFactory;
import game.gobj.GameObject;

public class LevelLoader {

    public static List<GameObject> loadLevel(String filePath, float brickW, float brickH, float startX, float startY) {
        List<GameObject> brickList = new ArrayList<>();
        
        InputStream in = LevelLoader.class.getResourceAsStream(filePath);
        if (in == null) {
            System.err.println("Level resource not found: " + filePath);
            return brickList;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                int colLength = tokens.length;

                for (int col = 0; col < colLength; col++) {
                    int id = Integer.parseInt(tokens[col]);

                    if (id != 0) {
                        float x = startX + col * brickW;
                        float y = startY + row * brickH;

                        Brick brick = BrickFactory.createBrick(id, x, y, brickW, brickH);
                        if (brick != null) {
                            brickList.add(brick);
                        }
                    }
                }

                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return brickList;
    }
}
