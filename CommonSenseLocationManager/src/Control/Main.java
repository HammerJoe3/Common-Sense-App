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
        //Reads the config values into the Configuration Singleton
        if (config.readConfig()) {
        } else {
            //If the config cannot be read, show the configuration creation GUI and then read the config
            root = FXMLLoader.load(getClass().getResource("../gui/CommonSenseConfigSetup.fxml"));
            Stage configStage = new Stage();
            configStage.setTitle("Smart Common Sense Location Management Setup");
            configStage.setScene(new Scene(root));
            configStage.showAndWait();
            config.readConfig();
        }

        //Display the CommonSenseLocationManager GUI
        try {
            root = FXMLLoader.load(getClass().getResource("../gui/CommonSenseLocationManager.fxml"));
            Stage manager = new Stage();
            manager.setTitle("Smart Common Sense Location Management");
            manager.setScene(new Scene(root));
            manager.showAndWait();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
