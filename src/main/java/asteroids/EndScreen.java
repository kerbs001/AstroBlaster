package asteroids;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class EndScreen{

    private static Pane gamePane;
    private TitleScreen titleScreen;

    public EndScreen(Pane gamePane, int score, TitleScreen titleScreen) {
        this.gamePane = gamePane;
        this.titleScreen = titleScreen;

        //Overlay
        Rectangle rectangle = new Rectangle(TitleScreen.WIDTH, TitleScreen.HEIGHT);
        StackPane stack = new StackPane(); // main layout
        stack.setOpacity(1);
        stack.getChildren().add(rectangle);
        stack.setPrefSize(TitleScreen.WIDTH, TitleScreen.HEIGHT);

        // game options after end screen
        VBox gameOptions = new VBox();

        Font textFont = Font.loadFont(AsteroidsApplication.class.getResourceAsStream("/fonts/Death Star.otf"), 25);

        Label gameOver = new Label("Game over!");
        Label totalScore = new Label(String.valueOf(AsteroidsApplication.getScore()));
        gameOver.setFont(textFont);
        totalScore.setFont(textFont);
        gameOver.setTextFill(Color.WHITE);
        totalScore.setTextFill(Color.WHITE);

        Button restart = new Button("Play again!");
        Button exit = new Button("Exit");

        restart.setOnAction(event -> titleScreen.restartGame());

        exit.setOnAction(event -> Platform.exit());

        gameOptions.setAlignment(Pos.CENTER);
        gameOptions.setPadding(new Insets(20, 20, 20, 20));
        gameOptions.setSpacing(10);

        gameOptions.getChildren().addAll(gameOver, totalScore, restart, exit);

        stack.getChildren().add(gameOptions);
        this.gamePane.getChildren().add(stack);

    }
}
