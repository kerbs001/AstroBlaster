package asteroids.package1.models;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Objects;

/**
 * The type Explosion.
 */
public class Explosion {

    private ImageView explosionImageView;
    private Pane gamePane;
    private static final double EXPLOSION_DURATION = 1.0; // in seconds

    /**
     * Instantiates a new Explosion. Called when two objects intersect.
     *
     * @param gamePane the game pane
     * @param x        the x - coordinate as to where explosion occurs
     * @param y        the y - coordinate as to where explosion occurs
     */
    public Explosion(Pane gamePane, double x, double y) {
        this.gamePane = gamePane;

        // Load the explosion image
        Image explosionImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/VFXExplosion.png"))); // Replace with your explosion image path
        explosionImageView = new ImageView(explosionImage);
        explosionImageView.setX(x - 20);
        explosionImageView.setY(y - 20);
        explosionImageView.setFitWidth(50); // Adjust the size as necessary
        explosionImageView.setFitHeight(50); // Adjust the size as necessary

        // Add explosion to the game pane
        gamePane.getChildren().add(explosionImageView);

        // Start explosion animation
        playExplosionAnimation();
    }

    /**
     * Plays an explosion animation wherein the explosion fades from 1.0 to 0.0 wherein it is removed from the pane after.
     */
    private void playExplosionAnimation() {
        // Fade out transition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(EXPLOSION_DURATION), explosionImageView);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(e -> gamePane.getChildren().remove(explosionImageView)); // Remove after fade out
        fadeTransition.play();
    }
}
