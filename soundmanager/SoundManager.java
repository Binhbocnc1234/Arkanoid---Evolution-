package soundmanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public class SoundManager {
    static class SoundData{
        final byte[] data;
        final AudioFormat format;

        SoundData(byte[] data, AudioFormat format) {
            this.data = data;
            this.format = format;
        }
    }

    private static Map<String, SoundData> sound = new HashMap<>();
    private static Map<String, Clip> clips = new HashMap<>();

    // tai file am thanh 
    public static void getSound(String name, String soundPath) {
        try {
            URL url = SoundManager.class.getResource(soundPath);
            if (url == null) {
                System.out.println("khong tim thay file");
                return;
            }
            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
                AudioFormat format = audioInput.getFormat();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int read;
                while ((read = audioInput.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }
                byte[] data = baos.toByteArray();

                sound.put(name, new SoundData(data, format));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Khong the tai file am thanh");
        }
    }

    // phat am thanh
    public static void playSound(String name) {
        SoundData soundData = sound.get(name);
        if (soundData != null) {
            try {
                InputStream input = new ByteArrayInputStream(soundData.data);
                AudioInputStream audioInput = new AudioInputStream(input, soundData.format, soundData.data.length / soundData.format.getFrameSize());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clips.put(name, clip);
            } catch (Exception e) {
                System.err.println("loi phat am thanh");
            }
            
        }
    }
    // dung sound
    public static void stopSound(String name) {
        Clip clip = clips.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
            clips.remove(name);
        }
    }

    public static void playSoundLoop(String name) {
        SoundData soundData = sound.get(name);
        if (soundData != null) {
            try {
                InputStream input = new ByteArrayInputStream(soundData.data);
                AudioInputStream audioInput = new AudioInputStream(input, soundData.format, soundData.data.length / soundData.format.getFrameSize());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
                clips.put(name, clip);
                if (!clip.isRunning()) {
                    clip.start();
                }
            } catch (Exception e) {
                System.err.println("loi phat am thanh");
            }
            
        }
    }
}
