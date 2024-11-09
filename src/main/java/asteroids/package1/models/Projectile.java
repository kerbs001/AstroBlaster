package asteroids.package1.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.media.AudioClip;

import java.util.Objects;


/**
 * The type Projectile.
 */
public class Projectile extends Character {

    private final long startTime;
    private AudioClip shootSound;

    /**
     * Instantiates a new Projectile.
     *
     * @param x         the x - coordinate for initial position of projectile
     * @param y         the y - coordinate for initial position of projectile
     * @param startTime the start time
     */
    public Projectile(int x, int y, long startTime) {
        super(new Polygon(3, -2, 3, 2, -6, 2, -6, -2), x ,y);
        this.startTime = startTime;
        this.getShape().setFill(Color.LIMEGREEN);
        loadSound();
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public long getStartTime() {
        return this.startTime;
    }

    /**
     * Shoot sfx.
     */
    public void shootSFX() {
        shootSound.play();
    }

    /**
     * returns the character of the projectile. Inherits the getCharacter() method from the Character class.
     */
    private Polygon getShape() {
        return super.getCharacter();
    }

    /**
     * initializes the shoot sfx that shall be used when shootSFX() is called.
     */
    private void loadSound() {
        this.shootSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/sfx/SFXshoot.mp3")).toExternalForm());
        this.shootSound.setVolume(0.5);
    }
}
