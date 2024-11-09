package asteroids.package1.screens;

import asteroids.Main;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;

public class EndScreen{

    private static Pane gamePane;
    private Main titleScreen;

    public EndScreen(Pane gamePane, int score, Main main) {
        EndScreen.gamePane = gamePane;
        this.titleScreen = main;

        //Overlay
        Rectangle rectangle = new Rectangle(Main.WIDTH, Main.HEIGHT);
        StackPane stack = new StackPane(); // main layout
        rectangle.setOpacity(0.5);
        stack.getChildren().add(rectangle);
        stack.setPrefSize(Main.WIDTH, Main.HEIGHT);

        // game options after end screen
        VBox gameOptions = new VBox();

        Font textFont = Font.loadFont(GameScreen.class.getResourceAsStream("/fonts/Death Star.otf"), 25);

        Label gameOver = new Label("Game over!");
        Label totalScore = new Label(String.valueOf(GameScreen.getScore()));
        gameOver.setFont(textFont);
        totalScore.setFont(textFont);
        gameOver.setTextFill(Color.WHITE);
        totalScore.setTextFill(Color.WHITE);

        Button restart = new Button("Play again!");
        Button exit = new Button("Exit");

        restart.setOnAction(event -> main.restartGame());

        exit.setOnAction(event -> Platform.exit());

        gameOptions.setAlignment(Pos.CENTER);
        gameOptions.setPadding(new Insets(20, 20, 20, 20));
        gameOptions.setSpacing(10);

        gameOptions.getChildren().addAll(gameOver, totalScore, restart, exit);

        stack.getChildren().add(gameOptions);
        EndScreen.gamePane.getChildren().add(stack);

    }
}
