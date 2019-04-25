package sample;

import Connection.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.control.ComboBox;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.ObservableList;


public class CommonSenseLocationManagerController implements Initializable {

    private Configuration config;

    @FXML
    ListView<String> locationList;
    @FXML TextField buildingName;
    @FXML TextField streetAddress;
    @FXML TextField city;
    @FXML TextField state;
    @FXML TextField zip;
    @FXML ComboBox typeBox;
    @FXML TextField treatedDate;
    @FXML private javafx.scene.control.Button submitBtn;
    @FXML private javafx.scene.control.Button exitBtn;

    private DbConnection conn;

    int i = 0;

    private ChangeListener typeListener = new ChangeListener<String>() {
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            switch (newValue){
                case "Sales":
                    treatedDate.setDisable(true);
                    treatedDate.setText("");
                    break;
                case "Treated":
                    treatedDate.setDisable(false);
                    if((places.get(locationList.getSelectionModel().getSelectedIndex()).getTreatedDate() == null)){
                        treatedDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                    else{
                        treatedDate.setText(places.get(locationList.getSelectionModel().getSelectedIndex()).getTreatedDate().toString());
                    }
                    break;
            }
        }
    };

    private ChangeListener listListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            buildingName.setText(places.get(locList.indexOf(newValue)).getName());
            buildingName.setDisable(false);

            streetAddress.setText(places.get(locList.indexOf(newValue)).getAddress());
            streetAddress.setDisable(false);

            city.setText(places.get(locList.indexOf(newValue)).getCity());
            city.setDisable(false);

            state.setText(places.get(locList.indexOf(newValue)).getState());
            state.setDisable(false);

            zip.setText(places.get(locList.indexOf(newValue)).getZip());
            zip.setDisable(false);

            typeBox.setDisable(false);
            switch (places.get(locList.indexOf(newValue)).getType()){
                case "Treated":
                    typeBox.setValue("Treated");
                    treatedDate.setText(places.get(locList.indexOf(newValue)).getTreatedDate().toString());
                    treatedDate.setDisable(false);
                    break;
                case "Sales":
                    typeBox.setValue("Sales");
                    treatedDate.setText(null);
                    treatedDate.setDisable(true);
                    break;
            }

            submitBtn.setDisable(false);
        }
    };

        private ArrayList<Place> places = new ArrayList<>();

    ObservableList<String> locList = FXCollections.observableArrayList();

    @Override public void initialize(URL location, ResourceBundle resources){
        config = Configuration.getInstance();
        typeBox.valueProperty().addListener(typeListener);
        conn = new DbConnection(config);
        try {
            conn.connect();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        typeBox.getItems().addAll("Sales", "Treated");
        getLocations();
    }

    @FXML private void exitBtnAction() {
        try {
            conn.disconnect();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML private void addBtnAction(){

    }

    @FXML private void submitBtnAction(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int selection = locationList.getSelectionModel().getSelectedIndex();
        try{
            PreparedStatement updateLocation = conn.prepareStatement("call update_locations(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            updateLocation.setInt(1, places.get(selection).getLocId());
            updateLocation.setString(2, buildingName.getText());
            updateLocation.setString(3, typeBox.getValue().toString());
            if (treatedDate.getText().equals("") || treatedDate.getText() == null){
                updateLocation.setDate(4, null);
            }
            else {
                updateLocation.setDate(4, Date.valueOf(LocalDate.parse(treatedDate.getText(), format)));
            }
            updateLocation.setInt(5,places.get(locationList.getSelectionModel().getSelectedIndex()).getAddressId());
            updateLocation.setString(6, streetAddress.getText());
            updateLocation.setString(7, city.getText());
            updateLocation.setString(8, state.getText());
            updateLocation.setString(9, zip.getText());

            //TODO Get Geolocate Functioning
            updateLocation.setFloat(10, 0);
            updateLocation.setFloat(11, 0);

            updateLocation.execute();
            conn.commit();
            locationList.getSelectionModel().select(selection);
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        getLocations();
    }

    private void getLocations(){
        try {
            locationList.getSelectionModel().selectedItemProperty().removeListener(listListener);
            locList.remove(0, locList.size());
            places.clear();
            PreparedStatement getLocationList = conn.prepareStatement("Select * from Location_List;");
            ResultSet rs = getLocationList.executeQuery();
            while (rs.next()) {
                places.add(new Place(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getDate(7), rs.getDate(8),
                        rs.getInt(9), rs.getInt(10)));
            }

            getLocationList.close();


            for (Place place : places) {
                locList.add(place.getName());
            }
            locationList.setItems(locList);
            i++;

            locationList.setItems(locList);
            locationList.getSelectionModel().selectedItemProperty().addListener(listListener);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
