package asteroids;

import java.util.Random;

public class Asteroid extends Character {

    private final double rotationalMovement;

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

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotationalMovement);
    }
}
