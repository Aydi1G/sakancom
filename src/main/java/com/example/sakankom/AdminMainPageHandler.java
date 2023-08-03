package com.example.sakankom;
import com.example.sakankom.dataStructures.Apartment;
import com.example.sakankom.dataStructures.Neigbour;
import com.example.sakankom.dataStructures.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminMainPageHandler implements Initializable {
        @FXML
        private MFXButton apartmentsBtn;
        @FXML
        private AnchorPane bigPane;
        @FXML
        private MFXButton btnLogout;
        @FXML
        private AnchorPane mainPane;
        @FXML
        private Label name;
        @FXML
        private MFXButton reservationsBtn;
        @FXML
        private VBox container;
        @FXML
        private MFXScrollPane page1;
        private MFXScrollPane page2;
        User user;
        ArrayList<Apartment> apartments ;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                apartments = new ArrayList<Apartment>();
                ResultSet rst,rst2;
                try{

                        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "sakankom", "12345678");
                        Statement st = con.createStatement();
                        rst = st.executeQuery("select * from house,owner,residence where house.residence_id = residence.residence_id and residence.owner_id = owner.owner_id and house.isvalid = '1' and house.isaccepted = '0'");

                        Apartment apt;
                        while(rst.next()) {
                                apt = new Apartment();
                                apt.setOwnerEmail(rst.getString("email"));
                                apt.setOwnerPhone(rst.getString("phone_number"));
                                apt.setAddress(rst.getString("location"));
                                apt.setOwnerName(rst.getString("fname") + " " + rst.getString("lname"));
                                apt.setAptName(rst.getString("residence_name"));
                                apt.setHouseId(rst.getInt("house_id"));
                                apt.setResidenceId(rst.getInt("residence_id"));
                                apt.setOwnerId(rst.getInt("owner_id"));
                                apt.setBathsN(rst.getInt("bathrooms_number"));
                                apt.setBedsN(rst.getInt("bedrooms_number"));
                                apt.setServices(rst.getString("services"));
                                apt.setPrice(rst.getDouble("price"));
                                apt.setFloor(rst.getInt("floor_number"));
                                apt.setAptNumber(rst.getInt("flat_number"));
                                apt.setCapacity(rst.getInt("capacity"));
                                apt.setResCapacity(rst.getInt("reserved_capacity"));
                                apt.setGender(rst.getString("genders"));
                                apt.setBalcony(rst.getString("balcony"));
                                apt.setIsValid(rst.getString("isvalid"));
                                apt.setIsAccepted(rst.getString("isaccepted"));
                                apt.setIsReserved(rst.getString("isreserved"));
                                apartments.add(apt);
                        }


                        con.close();
                }
                catch (SQLException e){
                        e.printStackTrace();
                }

                generateGUI();
        }
        @FXML
        void logoutBtnHandler(ActionEvent event) {
                Stage stage = (Stage) mainPane.getScene().getWindow();
                stage.close();

                Stage newStage = new Stage();
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Sign-In.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        newStage.setScene(scene);
                        newStage.show();

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void generateGUI(){
                //Declaring elements --------------------------------------------------------------------
                container.setMaxWidth(920);
                VBox card ;
                Label l1, l2;
                HBox cont , cont2;
                Label l3,l4;
                Label l5; MFXButton reserveBtn , detailsBtn;

                //prepare strings and data.
                String name = "";
                String owner = "";
                String type = "";
                String address = "";
                String price = "";


                for(int i= 0; i<apartments.size();i++) {

                        if(apartments.get(i).getIsValid().equals("1") && apartments.get(i).getIsAccepted().equals("0") && apartments.get(i).getIsReserved().equalsIgnoreCase("0")){
                                //data
                                name = apartments.get(i).getAptName();
                                owner = apartments.get(i).getOwnerName();
                                type = (apartments.get(i).getCapacity() > 1) ? "shared" : "solo";
                                address = apartments.get(i).getAddress();
                                price = Double.toString(apartments.get(i).getPrice());

                                //generating elements
                                //where it starts

                                card = new VBox();
                                card.setMaxWidth(950);
                                card.getStylesheets().add("TenantHouses.css");
                                card.getStyleClass().add("vbox");

                                l1 = new Label(name);
                                l2 = new Label("By " + owner);
                                l3 = new Label("Type: " + type + " room   ,");
                                l4 = new Label("   Address: " + address);
                                cont = new HBox();
                                cont2 = new HBox();

                                cont.getChildren().add(l3);
                                cont.getChildren().add(l4);

                                l5 = new Label("Price: " + price + "$" + "\t\t");
                                reserveBtn = new MFXButton("accept");
                                detailsBtn = new MFXButton("reject");


                                //setting the styles
                                reserveBtn.getStylesheets().add("TenantHouses.css");
                                reserveBtn.getStyleClass().add("myBtn");
                                detailsBtn.getStylesheets().add("TenantHouses.css");
                                detailsBtn.getStyleClass().add("myBtn");
                                l1.getStylesheets().add("TenantHouses.css");
                                l1.getStyleClass().add("l1");

                                cont2.getStylesheets().add("TenantHouses.css");
                                cont2.getStyleClass().add("cont2");

                                cont.getStylesheets().add("TenantHouses.css");
                                cont.getStyleClass().add("cont");


                                DoubleProperty fontSize = new SimpleDoubleProperty(18);
                                l1.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;", fontSize));
                                DoubleProperty fontSize2 = new SimpleDoubleProperty(12);
                                l2.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;", fontSize2));
                                l3.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;", fontSize2));
                                l4.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;", fontSize2));
                                l5.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;", fontSize2));


                                //adding elements to card
                                card.getChildren().add(l1);
                                card.getChildren().add(l2);
                                card.getChildren().add(cont);

                                cont2.getChildren().add(l5);
                                cont2.getChildren().add(detailsBtn);
                                cont2.getChildren().add(new Label("   "));
                                cont2.getChildren().add(reserveBtn);
                                card.getChildren().add(cont2);

                                container.getChildren().add(card);

                                reserveBtn.setId(Integer.toString(apartments.get(i).getHouseId()));
                                detailsBtn.setId(Integer.toString(apartments.get(i).getHouseId()));

                                int finalI = i;
                                detailsBtn.setOnAction(handler);
                                reserveBtn.setOnAction(handler2);


                                //where it ends

                        }

                }
        }
        EventHandler<ActionEvent> handler = event -> {
                MFXButton btn = (MFXButton) event.getSource();
                int i = Integer.parseInt(btn.getId());



        };
        EventHandler<ActionEvent> handler2 = event -> {
                MFXButton btn = (MFXButton) event.getSource();
                String id = btn.getId();

        };

        @FXML
        void viewApartments(ActionEvent event) {
                if(! bigPane.getChildren().isEmpty()){
                        bigPane.getChildren().remove(0);
                }
                bigPane.getChildren().add(page1);
        }

        @FXML
        void viewReservations(ActionEvent event) {
//                if(! bigPane.getChildren().isEmpty()){
//                        bigPane.getChildren().remove(0);
//                }
//                bigPane.getChildren().add(page2);
        }
        public void setUser(User user) {
                this.user = user;
                name.setText(user.getUsername());
        }
}