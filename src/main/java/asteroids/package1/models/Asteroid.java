package asteroids.package1.models;

import asteroids.package1.generators.PolygonFactory;

import java.util.Random;

/**
 * The type Asteroid.
 */
public class Asteroid extends Character {

    private final double rotationalMovement;

    /**
     * Instantiates a new Asteroid.
     *
     * @param x the x - coordinate asteroid is initially generated
     * @param y the y - coordinate asteroid is initially generated
     */
    public Asteroid(int x, int y) {
        super(new PolygonFactory().createPolygon(), x, y);

        Random random = new Random();

        super.getCharacter().setRotate(random.nextInt(360));

        int accelerationAmount = 1 + random.nextInt(25);

        // Add 0.005 chance of having accelerationAmount of 100;

        if (Math.random() < 0.05) {
            accelerationAmount = 200;
        }

        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        this.rotationalMovement = 0.5 - random.nextDouble();

    }

    /**
     * Inherits the move method in Character class and then randomizes the .setRotate() method for asteroid generation
     */
    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotationalMovement);
    }
}
