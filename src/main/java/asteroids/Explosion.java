package asteroids;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Explosion {

    private ImageView explosionImageView;
    private Pane gamePane;
    private static final double EXPLOSION_DURATION = 1.0; // in seconds

    public Explosion(Pane gamePane, double x, double y) {
        this.gamePane = gamePane;

        // Load the explosion image
        Image explosionImage = new Image(getClass().getResourceAsStream("/assets/VFXexplosion.png")); // Replace with your explosion image path
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

    private void playExplosionAnimation() {
        // Fade out transition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(EXPLOSION_DURATION), explosionImageView);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(e -> gamePane.getChildren().remove(explosionImageView)); // Remove after fade out
        fadeTransition.play();
    }
}
