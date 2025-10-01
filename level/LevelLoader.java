package level;

import brick.*;
import gobj.GameObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    public static List<GameObject> loadLevel(String filePath, float brickW, float brickH, float startX, float startY) {
        List<GameObject> brickList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
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
