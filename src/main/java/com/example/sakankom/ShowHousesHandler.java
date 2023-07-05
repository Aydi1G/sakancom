package com.example.sakankom;

import com.example.sakankom.OwnerFiles.House;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class ShowHousesHandler implements Initializable {

    @FXML
    private VBox floor;

    @FXML
    private GridPane floorContainer;

    @FXML
    private Label floorLabel;

    @FXML
    private HBox residenceLayout;

    private String residenceName;
    private List<House> houses;

    public void setDate(List<House> houses, String residenceName) {
        this.residenceName = residenceName;
        this.houses = new ArrayList<>(houses);

        floorLabel.setText("Floor " + houses.get(0).getFloor());
        Collections.reverse(houses);
        int column = 1;
        int row = 1;
        for (House house : houses) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("house.fxml"));
                VBox houseBox = fxmlLoader.load();

                houseHandler houseHandler = fxmlLoader.getController();
                houseHandler.setDate(house);

                if (column == 6) {
                    column = 0;
                    ++row;
                }
                floorContainer.add(houseBox, column++, row);
                GridPane.setMargin(houseBox, new Insets(10));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
