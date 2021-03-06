package Control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateConfigController {

    @FXML TextField configHost;
    @FXML TextField configUser;
    @FXML TextField configDBUser;
    @FXML TextField configPass;
    @FXML TextField configDB;
    @FXML private javafx.scene.control.Button configAcceptBtn;

    @FXML private void configAcceptBtnAction()
    {
        try {
            Stage stage = (Stage) configAcceptBtn.getScene().getWindow();

            //Opens a JFileChooser to select the SSH key file for the remote server
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Please select the SSH key for the remote server");
            jfc.showOpenDialog(null);

            //Creates the correct path for the selected file
            String keyUrl = jfc.getSelectedFile().getCanonicalPath().replace("\\", "\\\\");

            //Establishes a file output for the values entered into the gui that creates .CommonSenseDB/config.ini
            PrintWriter out = new PrintWriter(System.getProperty("user.home") + File.separator + ".CommonSenseDB" + File.separator + "config.ini");
            out.println("host = " + configHost.getText());
            out.println("remoteUser = " + configUser.getText());
            out.println("dbUser = " + configDBUser.getText());
            out.println("dbPass = " + configPass.getText());
            out.println("database = " + configDB.getText());
            out.println(("lport = 5656"));
            out.println("rport = 3306");
            out.println("key = " + keyUrl);
            stage.close();
            out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //Cancels creating the configuration
    @FXML private void configCancelBtnAction(){
        Platform.exit();
        System.exit(0);
    }
}
