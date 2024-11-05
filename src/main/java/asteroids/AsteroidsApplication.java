package asteroids;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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


public class AsteroidsApplication {

    private static final long PROJECTILE_LIFETIME = 2000; // in milliseconds
    private static boolean canShoot = true;
    private static double oddIncrement = 0;
    private static int score = 0;
    private static final List<Projectile> projectiles = new ArrayList<>();
    private static final List<Asteroid> asteroids = new ArrayList<>();
    private static Ship ship;

    private static TitleScreen titleScreen;


    public static Scene createGameScene(TitleScreen titleScreen) {
        AsteroidsApplication.titleScreen = titleScreen;

        // Reset game objects
        projectiles.clear();
        asteroids.clear();
        score = 0;

        // Reinitialize ship and other variables if needed
        ship = new Ship(TitleScreen.WIDTH / 2, TitleScreen.HEIGHT / 2);


        // **Import from resources folder
        Font ScoreFont = Font.loadFont(AsteroidsApplication.class.getResourceAsStream("/fonts/Death Star.otf"), 25);
        Image backgroundImage = new Image(Objects.requireNonNull(AsteroidsApplication.class.getResourceAsStream("/assets/background.jpg")));
        AudioClip bgMusic = new AudioClip(Objects.requireNonNull(AsteroidsApplication.class.getResource("/sfx/BGMusic.mp3")).toExternalForm());
        AudioClip explode = new AudioClip(Objects.requireNonNull(AsteroidsApplication.class.getResource("/sfx/explosion.wav")).toExternalForm());
        AudioClip boost = new AudioClip(Objects.requireNonNull(AsteroidsApplication.class.getResource("/sfx/boost.mp3")).toExternalForm());

        // **Setup main scene
        Pane pane = new Pane();
        pane.setPrefSize(TitleScreen.WIDTH, TitleScreen.HEIGHT);

        // Main Scene BG
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(TitleScreen.WIDTH); // Set to your desired width
        backgroundImageView.setFitHeight(TitleScreen.HEIGHT); // Set to your desired height
        backgroundImageView.setPreserveRatio(true);
        pane.getChildren().add(backgroundImageView);

        // Main Scene BGM
        bgMusic.play();
        bgMusic.setVolume(0.2);
        bgMusic.setCycleCount(AudioClip.INDEFINITE);

        // Dynamic changing of points
        AtomicInteger points = new AtomicInteger();

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


        // Animation Timer
        HBox scoreboard = new HBox();
        scoreboard.setAlignment(Pos.TOP_LEFT);
        scoreboard.setPadding(new Insets(10, 10, 10, 10));

        Text text = new Text(10, 20, "Points: 0");
        text.setFont(ScoreFont);
        text.setFill(Color.WHITE);

        scoreboard.getChildren().add(text);

        // **Placed here so it is on top of all sprites
        pane.getChildren().add(scoreboard);

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
            if (event.getCode() == KeyCode.UP && boost.getVolume() == 0) { // Check if volume is already 0
                boost.setVolume(0.5);
                boost.play();
            }
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
            if (event.getCode() == KeyCode.UP && boost.getVolume() > 0) { // Check if volume is already > 0
                boost.setVolume(0);
                boost.stop();
            }
        });
        new AnimationTimer() {

            @Override
            public void handle(long now) {

                // SCORING
                scoreboard.toFront();

                shipMovement(pane, pressedKeys);

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
                        stop();
                        new EndScreen(pane, getScore(), AsteroidsApplication.titleScreen);
                    }
                });

                // checking collision of projectile and asteroid
                projectileCheck(pane, text, explode, points);

                // removes all characters where isAive status is false;
                removeDeadEntities(pane);

                // ** ADDING DIFFICULTY FEATURE
                // adding 0.5% chance of spawning new asteroid. runs by 60 frames per second so chance is reasonable.
                addDifficulty(pane);

            }
        }.start();

        return scene;

    }
    public static int getScore() {
        return score;
    }
    public static void resetScore() {
        score = 0;
    }

    private static void spawnAsteroids(Pane pane, Random random) {
        for (int i = 0; i < 5; i++) {
            Asteroid asteroid = new Asteroid(random.nextInt(TitleScreen.WIDTH / 3), random.nextInt(TitleScreen.HEIGHT / 2));
            asteroids.add(asteroid);
            pane.getChildren().add(asteroid.getCharacter());
        }
    }

    private static void shipMovement(Pane pane , Map<KeyCode, Boolean> pressedKeys) {
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
            projectile.setMovement(projectile.getMovement().normalize().multiply(2));

            pane.getChildren().add(projectile.getCharacter());

            canShoot = false;
        }

        // reset canShoot mechanic
        if (!pressedKeys.getOrDefault(KeyCode.SPACE, false)) {
            canShoot = true;
        }
    }

    private static void projectileCheck(Pane pane, Text text, AudioClip explode, AtomicInteger points) {
        projectiles.forEach(projectile -> {
            asteroids.forEach(asteroid -> {
                if(projectile.collide(asteroid))  {

                    // VFX & SFX INTERACTION
                    explode.play();
                    new Explosion(pane, asteroid.getCharacter().getTranslateX(), asteroid.getCharacter().getTranslateY());

                    // UPDATE ALIVE STATUS OF PROJECTILE AND ASTEROID
                    projectile.setAlive(false);
                    asteroid.setAlive(false);

                    // Score increments
                    text.setText("Points: " + points.addAndGet(1000));
                    score += 1000;
                    // increase speed of asteroids
                    oddIncrement += 0.005;
                }
                if((System.currentTimeMillis() - projectile.getStartTime() > PROJECTILE_LIFETIME)) {
                    projectile.setAlive(false);
                }
            });


        });
    }

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

    private static void addDifficulty(Pane pane) {

        // increasing chance of spawning a new asteroid for every point scored
        if(Math.random() < 0.005 + oddIncrement) {
            Asteroid asteroid = new Asteroid(TitleScreen.WIDTH, TitleScreen.HEIGHT);

            if (!asteroid.collide(ship) && asteroids.size() < 15) {     // limit asteroids in pane to be 30 for optimization purposes
                asteroids.add(asteroid);
                pane.getChildren().add(asteroid.getCharacter());
            }
        }
    }

}
