package com.elcojo.gestorvuelos;

import DAO.FlightDAO;
import POJO.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;

public class VueloController {



        @FXML
        private TextField txtNum;
        @FXML private TextField txtDestino;
        @FXML private DatePicker dateSalida;
        @FXML private TextField txtDuracion;

        @FXML private ComboBox<String> cmbFiltro;
        @FXML private TextField txtFiltroDestino;

        @FXML private TableView<Flight> tabla;

        @FXML private TableColumn<Flight, String> colNum;
        @FXML private TableColumn<Flight, String> colDestino;
        @FXML private TableColumn<Flight, LocalDate> colFecha;
        @FXML private TableColumn<Flight, Integer> colDuracion;

        private FlightDAO dao;

        @FXML
        public void initialize() {

            try {
                dao = new FlightDAO();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // COLUMNAS
            colNum.setCellValueFactory(new PropertyValueFactory<>("numFlight"));
            colDestino.setCellValueFactory(new PropertyValueFactory<>("destination"));
            colFecha.setCellValueFactory(new PropertyValueFactory<>("departure"));
            colDuracion.setCellValueFactory(new PropertyValueFactory<>("duration"));

            // FILTROS
            cmbFiltro.setItems(FXCollections.observableArrayList(
                    "Todos",
                    "> 3 horas",
                    "Por destino"
            ));

            cargarTabla();
        }

        // =========================
        // CARGAR TABLA
        // =========================
        private void cargarTabla() {
            ObservableList<Flight> data =
                    FXCollections.observableArrayList(dao.obtenerTodos());

            tabla.setItems(data);
        }

        // =========================
        // AÑADIR
        // =========================
        @FXML
        public void onAdd() {

            Flight f = new Flight(
                    0,
                    txtNum.getText(),
                    txtDestino.getText(),
                    dateSalida.getValue(),
                    Integer.parseInt(txtDuracion.getText())
            );

            dao.insertar(f);
            cargarTabla();
            limpiar();
        }

        // =========================
        // ELIMINAR
        // =========================
        @FXML
        public void onDelete() {

            Flight f = tabla.getSelectionModel().getSelectedItem();

            if (f == null) return;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("Eliminar vuelo " + f.getNumFlight());

            if (alert.showAndWait().get() == ButtonType.OK) {
                dao.eliminar(f.getIdFlight());
                cargarTabla();
            }
        }

        // =========================
        // FILTROS
        // =========================
        @FXML
        public void onFilter() {

            String opcion = cmbFiltro.getValue();

            if (opcion == null) return;

            switch (opcion) {

                case "Todos" -> tabla.setItems(
                        FXCollections.observableArrayList(dao.obtenerTodos())
                );

                case "> 3 horas" -> tabla.setItems(
                        FXCollections.observableArrayList(dao.vuelosLargos())
                );

                case "Por destino" -> tabla.setItems(
                        FXCollections.observableArrayList(
                                dao.porDestino(txtFiltroDestino.getText())
                        )
                );
            }
        }

        // =========================
        // LIMPIAR FORM
        // =========================
        private void limpiar() {
            txtNum.clear();
            txtDestino.clear();
            txtDuracion.clear();
            dateSalida.setValue(null);
        }

    public void onActualizar(ActionEvent actionEvent) {
    }
}

