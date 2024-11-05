package asteroids;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {

    private Polygon character;
    private Point2D movement;
    private boolean aliveStatus;

    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x); // initializes the position in x-axis
        this.character.setTranslateY(y); // initializes the position in y-axis

        this.movement = new Point2D(0,0); // initializes the point to where the polygon will move once set
        this.aliveStatus = true; // monitor existence in the pane for tracking
    }

    public Polygon getCharacter() {
        return this.character;
    }

    public void turnLeft() {

        // getRotate() is a built-in method of Polygon to get current axis of rotation
        // setRotate() is a built-in method of Polygon to set  new axis of rotation

        this.character.setRotate(this.character.getRotate() - 5);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }

    public void move() {

        // incrementally changes both x and y position of polygon using the Point2D reference

        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());


        // Allows for seamlessly allowing the character to jump from one end of the border to the other

        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + TitleScreen.WIDTH);
        }
        if (this.character.getTranslateX() > TitleScreen.WIDTH) {
            this.character.setTranslateX(this.character.getTranslateX() - TitleScreen.WIDTH); // Try minus if there is a difference over modulo
        }
        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + TitleScreen.HEIGHT);
        }
        if (this.character.getTranslateY() > TitleScreen.HEIGHT) {
            this.character.setTranslateY(this.character.getTranslateY() - TitleScreen.HEIGHT);
        }

    }

    public void accelerate() {

        // this creates a gradual increase in displacement

        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= 0.02;
        changeY *= 0.02;

        this.movement = this.movement.add(changeX, changeY);
    }

    public boolean collide(Character other) {

        // this utilizes the intersect method of Shape wherein it checks the polygon of two characters, say for here is
        // ship and asteroid. Using getBoundsInLocal().getWidth(),
        // this checks collision that if it returns -1, it still hasnt collided
        // if it does not, it will return anything other than -1

        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    public Point2D getMovement() {
        return this.movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    public boolean isAlive() {
        return this.aliveStatus;
    }

    public void setAlive(boolean condition) {
        this.aliveStatus = condition;
    }


}
