package game.score;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScores {
    private static HighScores instance;
    private List<ScoreEntry> scores;
    private static final String HIGHSCORE_FILE = "highscores.dat";
    private static final int MAX_SCORES = 10;

    public static class ScoreEntry implements Serializable, Comparable<ScoreEntry> {
        private static final long serialVersionUID = 1L;
        private final String name;
        private final int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        @Override public int compareTo(ScoreEntry other) {
            return Integer.compare(other.score, this.score);
        }

        @Override public String toString() {
            return name + ": " + score;
        }
    }

    private HighScores() {
        scores = new ArrayList<>();
        loadScores();
    }

    public static HighScores getInstance() {
        if (instance == null) {
            instance = new HighScores();
        }
        return instance;
    }

    public List<ScoreEntry> getScore() {
        return Collections.unmodifiableList(scores);
    }

    public void addScore(String name, int score) {
        scores.add(new ScoreEntry(name, score));
        Collections.sort(scores);

        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }
        saveScores();
    }

    @SuppressWarnings("unchecked") private void loadScores() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
            scores = (ArrayList<ScoreEntry>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("Khong tim thay tep");
            scores = new ArrayList<>();
        } catch (IOException  | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            scores = new ArrayList<>();
        }
    }

    private void saveScores() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            oos.writeObject(scores);
            oos.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
