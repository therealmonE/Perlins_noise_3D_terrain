package ru.therealmone.PerlinsNoise;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Window window = new Window();
        primaryStage.setTitle("Perlin's noise terrain");
        primaryStage.setScene(window.getScene());
        primaryStage.show();
    }
}
