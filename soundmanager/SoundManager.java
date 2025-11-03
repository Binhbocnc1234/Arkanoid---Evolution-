package soundmanager;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;
import java.util.Map.Entry;

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
    private static float currentVolume = 50f;

    // tai file am thanh 
    public static void getSound(String name, String soundPath) {
        try {
            InputStream is = SoundManager.class.getResourceAsStream(soundPath);

            if (is == null) {
                System.out.println("khong tim thay file");
                return;
            }   

            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
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
                setVolume(clip, currentVolume);
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
                setVolume(clip, currentVolume);
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
    
    public static void setVolume(Clip clip, float volumePercent) {
        if (clip == null) {
            return;
        }
        try {
            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = fc.getMinimum();
            float max = fc.getMaximum();
            float value;
            float gain = volumePercent / 100f;
            if (gain <= 0.0f) {
                value = min;
            } else {
                float db = (float) (Math.log10(gain) * 20.0);
                value = max + db;
            }
            if (value < min) value = min;
            if (value > max) value = max;
            fc.setValue(value);
        } catch (IllegalArgumentException e) {
            System.err.println("khong thay doi duoc am luong");
        }
    }

    public static void setSpecificVolume(String name,float volumePercent) {
        currentVolume = volumePercent;
        setVolume(clips.get(name), currentVolume);
    }

    public static void setAllVolume(float volumePercent) {
        currentVolume = volumePercent;
        for (Clip clip : clips.values()) {
            setVolume(clip, volumePercent);
        }
    }
    
}
