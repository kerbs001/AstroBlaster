package asteroids.package1.screens;

import asteroids.Main;
import asteroids.package1.models.Asteroid;
import asteroids.package1.models.Explosion;
import asteroids.package1.models.Projectile;
import asteroids.package1.models.Ship;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.AnimationTimer;
import javafx.scene.media.AudioClip;


/**
 * The type Asteroids application.
 */
public class GameScreen {

    private static final long PROJECTILE_LIFETIME = 2000; // in milliseconds
    private static boolean canShoot = true;
    private static double addIncrement = 0;
    private static int score = 0;
    private static final AtomicInteger magazineCount = new AtomicInteger(5);
    private static final List<Projectile> projectiles = new ArrayList<>();
    private static final List<Asteroid> asteroids = new ArrayList<>();
    private static Ship ship;
    private static Main main;


    /**
     * Create the game scene. .
     *
     * @param main the main
     * @return the scene
     */
    public static Scene createGameScene(Main main) {
        GameScreen.main = main;

        resetParameters();
        ship = new Ship(Main.WIDTH / 2, Main.HEIGHT / 2);

        // **Import from resources folder
        Font ScoreFont = Font.loadFont(GameScreen.class.getResourceAsStream("/fonts/Death Star.otf"), 25);
        Image backgroundImage = new Image(Objects.requireNonNull(GameScreen.class.getResourceAsStream("/assets/background.jpg")));
        AudioClip bgMusic = new AudioClip(Objects.requireNonNull(GameScreen.class.getResource("/sfx/BGMusic.mp3")).toExternalForm());
        AudioClip explode = new AudioClip(Objects.requireNonNull(GameScreen.class.getResource("/sfx/explosion.wav")).toExternalForm());
        AudioClip boost = new AudioClip(Objects.requireNonNull(GameScreen.class.getResource("/sfx/boost.mp3")).toExternalForm());

        // **Setup main scene
        Pane pane = new Pane();
        pane.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // Main Scene BG
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(Main.WIDTH); // Set to your desired width
        backgroundImageView.setFitHeight(Main.HEIGHT); // Set to your desired height
        backgroundImageView.setPreserveRatio(true);
        pane.getChildren().add(backgroundImageView);

        // Main Scene BGM
        bgMusic.play();
        bgMusic.setVolume(0.2);
        bgMusic.setCycleCount(AudioClip.INDEFINITE);

        // Dynamic changing of points and magazine count
        AtomicInteger points = new AtomicInteger();
        AtomicInteger magazineCount = new AtomicInteger();

        // Only one random instance outside
        Random random = new Random();

        // spawn the asteroids into the pane
        spawnAsteroids(pane, random);

        // Spawn the ship
        pane.getChildren().add(ship.getCharacterGroup());




        // Show Main scene
        Scene scene = new Scene(pane);

        // **Recording of Inputs

        // Using of HashMap to store key inputs and boolean values if pressed and released

        // Play boost but set volume to 0;

        boost.setCycleCount(AudioClip.INDEFINITE);
        boost.setVolume(0);
        boost.play();

        // Scoreboard
        HBox scoreboard = new HBox();
        scoreboard.setPadding(new Insets(10, 10, 10, 10));

        Text pointsText = new Text(10, 20, "Points: 0");
        pointsText.setFont(ScoreFont);
        pointsText.setFill(Color.WHITE);

        scoreboard.getChildren().add(pointsText);



        // Magazine Counter
        HBox magazineBox = new HBox();
        magazineBox.setPadding(new Insets(10, 10, 10, 10));

        Text magazineCountText = new Text(10, 20, "Magazine: 5");
        magazineCountText.setFont(ScoreFont);
        magazineCountText.setFill(Color.WHITE);

        magazineBox.getChildren().add(magazineCountText);
        magazineBox.setTranslateX(Main.WIDTH - 180);
        magazineBox.setTranslateY(Main.HEIGHT - 50);

        // **Placed here so it is on top of all sprites
        pane.getChildren().addAll(scoreboard, magazineBox);

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
            if (event.getCode() == KeyCode.UP && boost.getVolume() == 0) { // Check if volume is already equals to 0
                pane.getChildren().add(ship.getBooster());
                boost.setVolume(0.5);
                boost.play();

            }
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
            if (event.getCode() == KeyCode.UP && boost.getVolume() > 0) { // Check if volume is already > 0
                pane.getChildren().remove(ship.getBooster());
                boost.setVolume(0);
                boost.stop();
            }
        });
        new AnimationTimer() {

            @Override
            public void handle(long now) {

                // SCORING
                scoreboard.toFront();
                magazineBox.toFront();

                shipMovement(pane, pressedKeys, magazineCountText);

                // Ship, asteroid, and projectiles movement
                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());

                // Collision of ship to asteroid checking
                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {

                        new Explosion(pane, ship.getCharacter().getTranslateX(), ship.getCharacter().getTranslateY());
                        explode.play();
                        bgMusic.stop();
                        pane.getChildren().remove(scoreboard);
                        stop();
                        new EndScreen(pane, getScore(), GameScreen.main);
                    }
                });

                // checking collision of projectile and asteroid
                projectileCheck(pane, pointsText, magazineCountText, explode, points);

                // removes all characters where isAive status is false;
                removeDeadEntities(pane);


                addDifficulty(pane, random);

            }
        }.start();

        return scene;

    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public static int getScore() {
        return score;
    }

    /**
     * Resets score.
     */
    public static void resetScore() {
        score = 0;
    }

    /**
     * Reset list of asteroids and projectiles, game score, and ship location.
     */
    public static void resetParameters () {
        projectiles.clear();
        asteroids.clear();
        score = 0;
        magazineCount.set(5);
    }

    /**
     * Generate asteroids and add it to the list for monitoring. Asteroids list check isAlive() status to check
     * whether asteroids should still be in game pane or not.
     * @param pane game pane
     * @param random random object for asteroid generation
     */
    private static void spawnAsteroids(Pane pane, Random random) {

        for (int i = 0; i < 5; i++) {
            Asteroid asteroid = new Asteroid(random.nextInt(Main.WIDTH / 3), random.nextInt(Main.HEIGHT / 2));
            asteroids.add(asteroid);
            pane.getChildren().add(asteroid.getCharacter());
        }
    }

    /**
     * Evaluates input values for ship acceleration, turning, and shooting.
     *
     * @param pane        game pane
     * @param pressedKeys HashMap containing input values with Boolean values showing it being pressed currently or not
     * @param magazineCountText text displaying magazine count
     */
    private static void shipMovement(Pane pane , Map<KeyCode, Boolean> pressedKeys, Text magazineCountText) {
        if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
            ship.turnLeft();
        }

        if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
            ship.turnRight();
        }

        if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
            ship.accelerate();
        }

        if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 5 && canShoot) {
            // we shoot here

            // projectile starts where the ship is located at the point of pressing space
            Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY(), System.currentTimeMillis());
            projectile.shootSFX();
            // projectile matches the current rotation of the ship to where it is fired;
            projectile.getCharacter().setRotate(ship.getCharacter().getRotate());

            projectiles.add(projectile);

            projectile.accelerate();
            // speed multiplier for projectile
            projectile.setMovement(projectile.getMovement().normalize().multiply(5));

            pane.getChildren().add(projectile.getCharacter());
            if (Integer.valueOf(String.valueOf(magazineCount)) > 0) {
                magazineCountText.setText("Magazine: " + magazineCount.addAndGet(-1));
            }

            canShoot = false;
        }

        // reset canShoot mechanic
        if (!pressedKeys.getOrDefault(KeyCode.SPACE, false)) {
            canShoot = true;
        }
    }

    /**
     * Check projectile and asteroid collision. Initiates also explosion FX and scoring when collision occurs.
     * isAlive status for the affected models are set to false which will be processed by removeDeadEntities() method
     * for model removal in game pane
     *
     * @param pane game pane
     * @param scoreText text displaying the score of the player
     * @param magazineCountText text displaying magazine count
     * @param explode audio clip of explosion initialized in the createGameScene()
     * @param points Used AtomicInteger class for dynamic updating of scores
     */
    private static void projectileCheck(Pane pane, Text scoreText, Text magazineCountText, AudioClip explode, AtomicInteger points) {
        projectiles.forEach(projectile -> {
            asteroids.forEach(asteroid -> {
                if(projectile.collide(asteroid))  {

                    // VFX & SFX INTERACTION
                    explode.play();
                    new Explosion(pane, asteroid.getCharacter().getTranslateX(), asteroid.getCharacter().getTranslateY());

                    // UPDATE ALIVE STATUS OF PROJECTILE AND ASTEROID
                    projectile.setAlive(false);
                    asteroid.setAlive(false);

                    if (Integer.parseInt(String.valueOf(magazineCount)) < 5) {
                        magazineCountText.setText("Magazine: " + magazineCount.addAndGet(1));
                    }

                    // Score increments
                    scoreText.setText("Points: " + points.addAndGet(1000));
                    score += 1000;
                    // increase speed of asteroids
                    addIncrement += 0.005;
                }
                else if((System.currentTimeMillis() - projectile.getStartTime() > PROJECTILE_LIFETIME)) {
                    if (Integer.parseInt(String.valueOf(magazineCount)) < 5) {
                        magazineCountText.setText("Magazine: " + magazineCount.addAndGet(1));
                    }
                    projectile.setAlive(false);
                }
            });


        });
    }

    /**
     * Checks projectiles and asteroids list for each isAlive() status. if status is set to False, projectile/asteroid
     * is removed from the game pane.
     * @param pane game pane
     */
    private static void removeDeadEntities(Pane pane) {
        projectiles.removeIf(projectile -> {
            if (!projectile.isAlive()) {
                pane.getChildren().remove(projectile.getCharacter());
                return true;
            }
            return false;
        });

        asteroids.removeIf(asteroid -> {
            if (!asteroid.isAlive()) {
                pane.getChildren().remove(asteroid.getCharacter());
                return true;
            }
            return false;
        });

    }

    /**
     * DIFFICULTY FEATURE:
     * Increase the chance a new asteroid is generated by increasing chance from initial 0.5%. Increments dictated by
     * addIncrement variable. Limits also asteroid generation by evaluating collision on starting position and
     * existing asteroids in pane.
     *
     * @param pane game pane
     * @param random random method
     */
    private static void addDifficulty(Pane pane, Random random) {

        // increasing chance of spawning a new asteroid for every point scored
        if(Math.random() < 0.005 + addIncrement) {
            Asteroid asteroid = new Asteroid(random.nextInt(Main.WIDTH / 3), random.nextInt(Main.HEIGHT/2));

            if (!asteroid.collide(ship) && asteroids.size() < 15) {     // limit asteroids in pane to be 30 for optimization purposes
                asteroids.add(asteroid);
                pane.getChildren().add(asteroid.getCharacter());
            }
        }
    }

}
