package fr.FDR.controller;

import fr.FDR.manager.WeatherManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import lombok.Data;

import java.awt.*;


@Data
public class WeatherController {

    private WeatherManager wm;

    @FXML
    public TextField cityName;

    @FXML
    public Label error;

    public void searchWeather(ActionEvent actionEvent) {
        if(cityName.getText() != null) {
            String city = cityName.getText();
            if (!city.isEmpty()) {
                city = city.toLowerCase();
                wm = new WeatherManager(city);
                System.out.println(wm);
            }else{
                this.error.setText("Le champ ne peut pas être vide !");
                //this.error.setTextFill(Color.TOMATO);
                this.error.setVisible(true);
            }
        }

    }

}
