package asteroids.package1.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Group;

import java.util.Objects;

/**
 * The type Ship.
 */
public class Ship extends Character {

    private ImageView shipView;
    private ImageView boosterView;
    private Group characterGroup;
    private Group booster;


    /**
     * Instantiates a new Ship. Initializes also the image views to be layered. Polygon shall serve as the hitbox of the
     * ship, hence it is set to TRANSPARENT.
     *
     * @param x the x - coordinate to where it will be initially generated
     * @param y the y - coordinate to where it will be initially generated
     */
    public Ship(int x, int y) {
        super(new Polygon(
                -5, -10, 10, 0, -5, 10
        ), x, y);

        Image shipImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/ship.png")).toExternalForm());

        shipView = new ImageView(shipImage);
        shipView.setFitWidth(20);
        shipView.setFitHeight(20);

        Image shipWithBooster = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/booster.png")));

        boosterView = new ImageView(shipWithBooster);
        this.boosterView.setFitWidth(40);
        this.boosterView.setFitHeight(40);

        Polygon shipModel = super.getCharacter();
        shipModel.setFill(Color.TRANSPARENT);

        booster = new Group(shipModel, boosterView, shipView);
        characterGroup = new Group(shipModel, shipView);
    }

    /**
     * Gets character group.
     *
     * @return the character group
     */
    public Group getCharacterGroup() {
        return characterGroup;
    }
    /**
     * Gets booster.
     *
     * @return the booster
     */
    public Group getBooster() {
        return booster;
    }

    /**
     * Inherits the move from the Character class for how the ship should move within the game pane.
     * Matches the x- and y- coordinates for the booster and ship view to the polygon. x Translation are set to match
     * the size of the polygon.
     */
    @Override
    public void move() {
        super.move(); // Call the move method from the Character class
        // Update the position of the imageView based on the polygon's translation
        boosterView.setTranslateX(super.getCharacter().getTranslateX() - 20);
        boosterView.setTranslateY(super.getCharacter().getTranslateY() - 20);
        shipView.setTranslateX(super.getCharacter().getTranslateX() - 10);
        shipView.setTranslateY(super.getCharacter().getTranslateY() - 10);

    }
    /**
     * Inherits the turnLeft() method from the Character class. Matches rotation of boosterView and shipView from
     * rotation of the character class
     */
    @Override
    public void turnLeft() {
        super.turnLeft(); // Call the turnLeft method from the Character class

        boosterView.setRotate(super.getCharacter().getRotate());
        shipView.setRotate(super.getCharacter().getRotate()); // Sync the image rotation
    }
    /**
     * Inherits the turnRight() method from the Character class. Matches rotation of boosterView and shipView from
     * rotation of the character class
     */
    @Override
    public void turnRight() {
        super.turnRight(); // Call the turnRight method from the Character class

        boosterView.setRotate(super.getCharacter().getRotate());
        shipView.setRotate(super.getCharacter().getRotate()); // Sync the image rotation
    }


}
