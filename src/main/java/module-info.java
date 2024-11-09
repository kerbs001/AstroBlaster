module asteroids {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    exports asteroids to javafx.graphics;
    exports asteroids.package1.models to javafx.graphics;
    exports asteroids.package1.generators to javafx.graphics;
    exports asteroids.package1.screens to javafx.graphics;

}