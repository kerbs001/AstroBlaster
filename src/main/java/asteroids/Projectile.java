package asteroids;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.media.AudioClip;


public class Projectile extends Character {

    private final long startTime;
    private AudioClip shootSound;

    public Projectile(int x, int y, long startTime) {
        super(new Polygon(3, -2, 3, 2, -6, 2, -6, -2), x ,y);
        this.startTime = startTime;
        this.getShape().setFill(Color.LIMEGREEN);
        loadSound();
    }

    public long getStartTime() {
        return this.startTime;
    }
    public void shootSFX() {
        shootSound.play();
    }

    private Polygon getShape() {
        return (Polygon) super.getCharacter();
    }
    private void loadSound() {
        this.shootSound = new AudioClip(getClass().getResource("/sfx/SFXshoot.mp3").toExternalForm());
        this.shootSound.setVolume(0.5);
    }
}
