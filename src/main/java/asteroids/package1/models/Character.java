package asteroids.package1.models;

import asteroids.Main;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * The type Character.
 */
public abstract class Character {

    private Polygon character;
    private Point2D movement;
    private boolean aliveStatus;

    /**
     * Instantiates a new Character.
     *
     * @param polygon the polygon
     * @param x       the x- coordinate of the character to where it would be initialized.
     * @param y       the y- coordinate of the character to where it would be initialized.
     */
    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x); // initializes the position in x-axis
        this.character.setTranslateY(y); // initializes the position in y-axis

        this.movement = new Point2D(0,0); // initializes the point to where the polygon will move once set
        this.aliveStatus = true; // monitor existence in the pane for tracking
    }

    /**
     * Gets character.
     *
     * @return the character
     */
    public Polygon getCharacter() {
        return this.character;
    }

    /**
     * Turn left.
     */
    public void turnLeft() {

        // getRotate() is a built-in method of Polygon to get current axis of rotation
        // setRotate() is a built-in method of Polygon to set  new axis of rotation

        this.character.setRotate(this.character.getRotate() - 5);
    }

    /**
     * Turn right.
     */
    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }

    /**
     * Move method that incrementally changes both x and y position based off from Point2D class movement. <br>
     * <br>
     * Method implements movement by utilizing a static point with .getTranslate() and adding a
     * dynamic addition with .movement()
     */
    public void move() {

        // incrementally changes both x and y position of polygon using the Point2D reference

        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());

        // Allows for seamlessly allowing the character to jump from one end of the border to the other

        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + Main.WIDTH);
        }
        if (this.character.getTranslateX() > Main.WIDTH) {
            this.character.setTranslateX(this.character.getTranslateX() - Main.WIDTH); // Try minus if there is a difference over modulo
        }
        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + Main.HEIGHT);
        }
        if (this.character.getTranslateY() > Main.HEIGHT) {
            this.character.setTranslateY(this.character.getTranslateY() - Main.HEIGHT);
        }
    }

    /**
     * Accelerate method for the character the model will inherit. Implements polar to cartesian conversion
     * in determining x- and y- coordinates as movement considers rotation <br>
     * <br>
     * Functions: <br>
     * x = r cos(tetha) <br>
     * y = r sin(tetha)
     */
    public void accelerate() {

        // this creates a gradual increase in displacement

        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= 0.02;
        changeY *= 0.02;

        this.movement = this.movement.add(changeX, changeY);
    }

    /**
     * this utilizes the intersect method of Shape wherein it checks the polygon of two characters, say for here is
     * ship and asteroid. Using getBoundsInLocal().getWidth(),
     * this checks collision that if it returns -1, it still hasn't collided
     * if it does not, it will return anything other than -1
     *
     * @param other other characters that are in collision with the shape
     * @return the
     */
    public boolean collide(Character other) {



        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    /**
     * Gets movement.
     *
     * @return the movement
     */
    public Point2D getMovement() {
        return this.movement;
    }

    /**
     * Sets movement considering Point2D.
     *
     * @param movement the movement
     */
    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    /**
     * isAlive boolean for collision checking.
     *
     * @return the boolean
     */
    public boolean isAlive() {
        return this.aliveStatus;
    }

    /**
     * Sets alive condition of a character. Used in removal of object in game pane
     *
     * @param condition the condition
     */
    public void setAlive(boolean condition) {
        this.aliveStatus = condition;
    }

}
