package game.weapon;
import game.gobj.*;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class BallTrail extends  GameObject{
    // private static final float existTime = 0.5f;
    private float alpha = 0.7f, trailDiameter;


    protected BallTrail(float x, float y, float initialDiameter, String imagePath){
        super(x, y, initialDiameter, initialDiameter, imagePath);
        trailDiameter = initialDiameter;
    }
    @Override public void update(){
        alpha -= 0.08f;
        trailDiameter -= 1.8f;
        if (trailDiameter <= 0){
            selfDestroy();
        }
        if (alpha <= 0){
            alpha = 0;
        }
    }
    @Override public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(image, (int) (x - trailDiameter / 2f), (int) (y - trailDiameter / 2f), (int) trailDiameter, (int) trailDiameter,  null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}