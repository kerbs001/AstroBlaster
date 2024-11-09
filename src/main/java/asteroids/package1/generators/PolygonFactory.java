package asteroids.package1.generators;

import java.util.Objects;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;


/**
 * The type Polygon factory.
 */
public class PolygonFactory {

    /**
     * Create a new polygon that is used for generating the shape of the asteroid. It randomizes the number of
     * sides of an asteroid. Initially it generates a regular polygon, randomization is implemented on the vertices
     * to make it look more natural
     *
     * @return the polygon
     */
    public Polygon createPolygon() {
        Random random = new Random();
        int numSides = 8 + random.nextInt(10); // Random number of sides between 8 and 12 for variation
        double size = 10 + random.nextInt(20);
        Polygon polygon = new Polygon();


        for (int i = 0; i < numSides; i++) {

            /*
             get the angle for each vertex of a polygon with n number of sides
             calculated by 2  * PI * (n / numSides) where 2 * PI is the total angle in radians of a full circle
            */

            double angle = 2 * Math.PI * i / numSides; // Calculate angle for each vertex

            /*
             **NOTE: TETHA IS ALWAYS IN RADIANS
             To get x and y values, use:
            */

            /*
             COS(tetha) = adjacent / hypotenuse -> hypotenuse * SIN(tetha) = adjacent
             where: adjacent = x val
             */
            double x = size * Math.cos(angle);

            /*
             SIN(tetha) = opposite / hypotenuse -> hypotenuse * SIN(tetha) = opposite
             where: opposite = y val
            */
            double y = size * Math.sin(angle);


            polygon.getPoints().addAll(x, y);
        }

        // **POINT RANDOMIZATION
        // Made to add variety instead of just creating REGULAR polygons
        for (int i = 0; i < polygon.getPoints().size(); i++) {

            // randomizer whether to add or subtract to the existing x, y values. this is updated as one when called
            // with .set() method..
            int change = random.nextInt(5) - 2;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }


        // Image map for the asteroids
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/asteroid.jpg")));

        double modifier = random.nextDouble();
        double tileX = 0 + image.getWidth() * modifier;
        double tileY = 0 + image.getHeight() * modifier;
        double tileWidth = 50;
        double tileHeight = 50;

        ImagePattern imagePattern = new ImagePattern(image, tileX, tileY, tileWidth, tileHeight, false);
        polygon.setFill(imagePattern);
        polygon.setStroke(Color.LIGHTGREY);
        polygon.setStrokeWidth(2);
        return polygon;
    }
}
