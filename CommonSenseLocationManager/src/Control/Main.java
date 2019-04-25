package Control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {

    Configuration config = Configuration.getInstance();
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root;
        if (config.readConfig()) {
        } else {
            root = FXMLLoader.load(getClass().getResource("../gui/CommonSenseConfigSetup.fxml"));
            Stage configStage = new Stage();
            configStage.setTitle("Smart Common Sense Location Management Setup");
            configStage.setScene(new Scene(root, 471, 284));
            configStage.showAndWait();
            config.readConfig();
        }

        try {
            root = FXMLLoader.load(getClass().getResource("../gui/CommonSenseLocationManager.fxml"));
            Stage manager = new Stage();
            manager.setTitle("Smart Common Sense Location Management");
            manager.setScene(new Scene(root));
            manager.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            manager.showAndWait();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override public void stop(){

    }


    public static void main(String[] args) {
        launch(args);
    }
}
