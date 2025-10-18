package soundmanager;

import java.util.HashMap;
import javax.sound.sampled.*;
import java.util.Map;
import java.net.URL;

public class SoundManager {
    private static Map<String, Clip> sound = new HashMap<>();

    // tai file am thanh 
    public static void getSound(String name, String soundPath) {
        try {
            URL url = SoundManager.class.getResource(soundPath);
            if (url == null) {
                System.out.println("khong tim thay file");
                return;
            }
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            sound.put(name, clip);
        } catch (Exception e) {
            System.out.println("Khong the tai file am thanh");
        }
    }

    // phat am thanh
    public static void playSound(String name) {
        Clip clip = sound.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
