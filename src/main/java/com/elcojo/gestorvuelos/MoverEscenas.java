package com.elcojo.gestorvuelos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MoverEscenas {


    private static Stage stage;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void cambiarEscena(String nombreFXML, String titulo) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(
                    MoverEscenas.class.getResource(nombreFXML + ".fxml")
            );

            Scene scene = new Scene(fxmlLoader.load());

            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void abrirPopup(String nombreFXML, String titulo) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    MoverEscenas.class.getResource(nombreFXML + ".fxml")
            );

            Parent root = loader.load();

            Stage popup = new Stage();

            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initStyle(StageStyle.DECORATED);

            popup.setTitle(titulo);
            popup.setScene(new Scene(root));

            popup.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}