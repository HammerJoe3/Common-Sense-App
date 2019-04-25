package Control;

import Connection.DbConnection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;


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
    @FXML private javafx.scene.control.Button removeBtn;
    @FXML private javafx.scene.control.Button saveLocationBtn;
    @FXML private javafx.scene.control.Button cancelAddBtn;

    private DbConnection conn;

    private ChangeListener typeListener = new ChangeListener<String>() {
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (typeBox.getSelectionModel().getSelectedIndex() < 0){
                return;
            }
            switch (newValue) {
                case "Sales":
                    treatedDate.setDisable(true);
                    treatedDate.setText("");
                    break;
                case "Treated":
                    if (locationList.getSelectionModel().getSelectedIndex() >= 0){
                        treatedDate.setDisable(false);
                        if ((places.get(locationList.getSelectionModel().getSelectedIndex()).getTreatedDate() == null)) {
                            treatedDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else {
                            treatedDate.setText(places.get(locationList.getSelectionModel().getSelectedIndex()).getTreatedDate().toString());
                        }
                        break;
                    }
                    else {
                        treatedDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        treatedDate.setDisable(false);
                    }

            }
        }
    };

    private ChangeListener listListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (locList.indexOf(newValue) < 0){
                return;
            }
            enableFields();
            buildingName.setText(places.get(locList.indexOf(newValue)).getName());

            streetAddress.setText(places.get(locList.indexOf(newValue)).getAddress());

            city.setText(places.get(locList.indexOf(newValue)).getCity());

            state.setText(places.get(locList.indexOf(newValue)).getState());

            zip.setText(places.get(locList.indexOf(newValue)).getZip());

            switch (places.get(locList.indexOf(newValue)).getType()){
                case "Treated":
                    typeBox.setValue("Treated");
                    treatedDate.setText(places.get(locList.indexOf(newValue)).getTreatedDate().toString());
                    break;
                case "Sales":
                    typeBox.setValue("Sales");
                    treatedDate.setText(null);
                    treatedDate.setDisable(true);
                    break;
            }

            submitBtn.setDisable(false);
            removeBtn.setDisable(false);
        }
    };

        private ArrayList<Place> places = new ArrayList<>();

    ObservableList<String> locList = FXCollections.observableArrayList();

    @Override public void initialize(URL location, ResourceBundle resources){
        locationList.getSelectionModel().selectedItemProperty().addListener(listListener);
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
            Platform.exit();
            System.exit(0);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML private void addBtnAction(){
        locationList.getSelectionModel().clearSelection();
        submitBtn.setDisable(true);
        removeBtn.setDisable(true);
        locationList.setDisable(true);
        cancelAddBtn.setDisable(false);
        saveLocationBtn.setDisable(false);
        cancelAddBtn.setVisible(true);
        saveLocationBtn.setVisible(true);
        typeBox.setDisable(false);
        clearValues();
        enableFields();
        treatedDate.setDisable(true);
    }

    @FXML private void removeBtnAction(){
        if (locationList.getSelectionModel().getSelectedIndex() < 0){
            JOptionPane.showMessageDialog(null, "Please select a location from the list.", "Select a Location", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int accept = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this location?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (accept == 0){
            try {
                PreparedStatement removeLoc = conn.prepareStatement("Delete from locations where locID = ?");
                removeLoc.setInt(1, places.get(locationList.getSelectionModel().getSelectedIndex()).getLocId());
                removeLoc.execute();
                removeLoc.close();
                conn.commit();
                removeLoc.close();
                getLocations();
                clearValues();
                disableFields();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @FXML private void submitBtnAction(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int selection = locationList.getSelectionModel().getSelectedIndex();
        if (selection < 0){
            JOptionPane.showMessageDialog(null, "Please select a location from the list.", "Select a Location", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try{
            PreparedStatement updateLocation = conn.prepareStatement("call update_locations(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            updateLocation.setInt(1, places.get(selection).getLocId());
            updateLocation.setString(2, buildingName.getText());
            updateLocation.setString(3, typeBox.getValue().toString());
            if (treatedDate.getText() == null || treatedDate.getText().trim().isEmpty()){
                if (typeBox.getValue().toString() == "Sales") {
                    updateLocation.setDate(4, null);
                }
                else throw new NullPointerException();
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
            updateLocation.close();
            clearValues();
            disableFields();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            JOptionPane.showMessageDialog(null, "Please be sure to fill all values", "Warning", JOptionPane.WARNING_MESSAGE);
            locationList.getSelectionModel().select(selection);
        }

        getLocations();
    }

    private void getLocations(){
        try {
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

            locationList.setItems(locList);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML private void saveLocationBtnAction(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            PreparedStatement addAddress = conn.prepareStatement("Insert into address (StreetAddress, City, State, Zip, Latitude, Longitude) values (?, ?, ?, ?, ?, ?) on duplicate key update AddressID=AddressID;");
            addAddress.setString(1, streetAddress.getText());
            addAddress.setString(2, city.getText());
            addAddress.setString(3, state.getText());
            addAddress.setString(4, zip.getText());
            //TODO Add Geolocation
            addAddress.setFloat(5, 0);
            addAddress.setFloat(6, 0);
            addAddress.execute();
            conn.commit();
            addAddress.close();

            PreparedStatement getAddressId = conn.prepareStatement("select AddressID from address where StreetAddress = ?;");
            getAddressId.setString(1, streetAddress.getText());
            ResultSet rs = getAddressId.executeQuery();

            int addressID = 0;
            while (rs.next()){
                addressID = rs.getInt(1);
            }
            getAddressId.close();

            PreparedStatement addLocation = conn.prepareStatement("insert into locations (Building_Name, AddressID, Type, Date) values (?, ?, (Select typeID from location_type where Type = ?), ?);");
            addLocation.setString(1, buildingName.getText());
            addLocation.setInt(2, addressID);
            addLocation.setString(3, typeBox.getValue().toString());
            if (treatedDate.getText() == null || treatedDate.getText().trim().isEmpty()){
                if (typeBox.getValue().toString() == "Sales") {
                    addLocation.setDate(4, null);
                }
                else throw new NullPointerException();
            }
            else {
                addLocation.setDate(4, Date.valueOf(LocalDate.parse(treatedDate.getText(), format)));
            }
            addLocation.execute();
            conn.commit();
            addLocation.close();
            getLocations();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Please be sure to fill all values", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    @FXML private void cancelAddBtnAction(){
        locationList.setDisable(false);
        cancelAddBtn.setDisable(true);
        saveLocationBtn.setDisable(true);
        cancelAddBtn.setVisible(false);
        saveLocationBtn.setVisible(false);
        clearValues();
        disableFields();
    }

    private void clearValues(){
        buildingName.setText(null);
        streetAddress.setText(null);
        city.setText(null);
        zip.setText(null);
        treatedDate.setText(null);
        state.setText(null);
        typeBox.getSelectionModel().clearSelection();
    }

    private void disableFields(){
        buildingName.setDisable(true);
        streetAddress.setDisable(true);
        city.setDisable(true);
        zip.setDisable(true);
        treatedDate.setDisable(true);
        state.setDisable(true);
        zip.setDisable(true);
        typeBox.setDisable(true);
    }

    private void enableFields(){
        buildingName.setDisable(false);
        streetAddress.setDisable(false);
        city.setDisable(false);
        zip.setDisable(false);
        treatedDate.setDisable(false);
        state.setDisable(false);
        zip.setDisable(false);
        typeBox.setDisable(false);
    }

}
