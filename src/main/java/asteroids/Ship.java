package asteroids;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Group;

import java.util.Objects;

public class Ship extends Character {

    private ImageView imageView;
    private Group characterGroup;

    public Ship(int x, int y) {
        super(new Polygon(
                -5, -10, 10, 0, -5, 10
        ), x, y);

        Image shipImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/ship.png")).toExternalForm());
        imageView = new ImageView(shipImage);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);

        characterGroup = new Group(super.getCharacter(), imageView);
        super.getCharacter().setFill(Color.TRANSPARENT);
    }

    public Group getCharacterGroup() {
        return characterGroup;
    }

    @Override
    public void move() {
        super.move(); // Call the move method from the Character class
        // Update the position of the imageView based on the polygon's translation
        imageView.setTranslateX(super.getCharacter().getTranslateX() - 10);
        imageView.setTranslateY(super.getCharacter().getTranslateY() - 10);
    }

    @Override
    public void turnLeft() {
        super.turnLeft(); // Call the turnLeft method from the Character class
        imageView.setRotate(super.getCharacter().getRotate()); // Sync the image rotation
    }

    @Override
    public void turnRight() {
        super.turnRight(); // Call the turnRight method from the Character class
        imageView.setRotate(super.getCharacter().getRotate()); // Sync the image rotation
    }
}
