package asteroids;

import asteroids.package1.screens.GameScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.media.AudioClip;

import java.util.Objects;

public class Main extends Application {

    public static int WIDTH = 900;
    public static int HEIGHT = 600;
    private Stage primaryStage;


    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showTitleScreen();
    }

    public void showTitleScreen() {
        BorderPane borderPane = new BorderPane(); // higher level layout

        // LOAD PREREQUISITES
        Font bodyFont = Font.loadFont(getClass().getResourceAsStream("/fonts/BubbleBoddyNeue-Light Trial.ttf"), 15);
        String videoPath = Objects.requireNonNull(getClass().getResource("/videos/Title.mp4")).toExternalForm();
        AudioClip titleBGM = new AudioClip(Objects.requireNonNull(GameScreen.class.getResource("/sfx/TitleBGMusic.mp3")).toExternalForm());

        // Title BGM
        titleBGM.play();
        titleBGM.setCycleCount(AudioClip.INDEFINITE);

        // Media load for background image
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(WIDTH);
        mediaView.setFitHeight(HEIGHT);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);

        // Menu layout
        StackPane menu = new StackPane();

        // Create a VBox for button alignment
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.BOTTOM_LEFT); // Align to bottom left
        buttonContainer.setPadding(new Insets(0, 0, 70, 70)); // Reduced padding for visibility

        // Title
        Button enterGame = new Button("Start Game");
        enterGame.setFont(bodyFont);
        buttonContainer.getChildren().add(enterGame); // Add button to VBox

        enterGame.setOnAction((event) -> {
                titleBGM.stop();
                restartGame();
                }
        );

        // Add video and button container to StackPane
        menu.getChildren().addAll(mediaView, buttonContainer);

        borderPane.setCenter(menu);
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);

        primaryStage.setTitle("Astro Blaster!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void restartGame() {
        GameScreen.resetScore();
        Scene gameScene = GameScreen.createGameScene(this);

        // Set the scene to the primary stage
        primaryStage.setScene(gameScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
