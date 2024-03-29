import Game.CollisionHandler;
import Game.GameController;
import Highscores.FileHandler;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    FileHandler fileHandler = FileHandler.getInstance();

    /**
     * Starts the application
     * @param primaryStage the stage the application is being run on
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Red Runner");
        primaryStage.getIcons().add(new Image("/Resources/icon/RedRunnerIcon.png"));

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu/MainMenu.fxml"));
        root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(GameController.addKillCoins)fileHandler.addSurvivalInfo(CollisionHandler.killCoins);
            }
        });
    }


    /**
     * Starts the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
