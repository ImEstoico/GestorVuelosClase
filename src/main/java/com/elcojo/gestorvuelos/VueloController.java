package com.elcojo.gestorvuelos;

import DAO.FlightDAO;
import POJO.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class VueloController {



    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNum;

    @FXML
    private TextField txtDestino;

    @FXML
    private DatePicker dateSalida;

    @FXML
    private TextField txtDuracion;


    @FXML
    private ComboBox<String> cmbFiltro;

    @FXML
    private TextField txtFiltroDestino;



    @FXML
    private TableView<Flight> tabla;

    @FXML
    private TableColumn<Flight, Integer> colId;

    @FXML
    private TableColumn<Flight, String> colNum;

    @FXML
    private TableColumn<Flight, String> colDestino;

    @FXML
    private TableColumn<Flight, LocalDate> colFecha;

    @FXML
    private TableColumn<Flight, Integer> colDuracion;


    private FlightDAO dao;


    @FXML
    public void initialize() {


            try {

                dao = new FlightDAO();

                System.out.println("DAO creado correctamente");

            } catch (SQLException e) {

                e.printStackTrace();
            }

            cargarTabla();

        // CONFIGURAR COLUMNAS

        colId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getIdFlight()
                ).asObject()
        );

        colNum.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getNumFlight()
                )
        );

        colDestino.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDestination()
                )
        );

        colFecha.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        cellData.getValue().getDeparture()
                )
        );

        colDuracion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getDuration()
                ).asObject()
        );

        // FILTROS

        cmbFiltro.setItems(FXCollections.observableArrayList(
                "Todos",
                "Más de 3 horas",
                "Por destino"
        ));

        // CARGAR TABLA

        cargarTabla();

        // EVENTO SELECCION TABLA

        tabla.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> cargarFormulario(newValue)
        );
    }


    //CARGAR TABLA


    private void cargarTabla() {

        ObservableList<Flight> lista =
                FXCollections.observableArrayList(dao.obtenerTodos());

        tabla.setItems(lista);
    }

    // CARGAR FORMULARIO

    private void cargarFormulario(Flight f) {

        if (f == null) return;

        txtId.setText(String.valueOf(f.getIdFlight()));
        txtNum.setText(f.getNumFlight());
        txtDestino.setText(f.getDestination());
        dateSalida.setValue(f.getDeparture());
        txtDuracion.setText(String.valueOf(f.getDuration()));
    }

    // AÑADIR

    @FXML
    public void onAdd() {

        try {

            if (!validarCampos()) return;

            Flight f = new Flight(
                    0,
                    txtNum.getText(),
                    txtDestino.getText(),
                    dateSalida.getValue(),
                    Integer.parseInt(txtDuracion.getText())
            );

            boolean insertado = dao.insertar(f);

            if (insertado) {

                mostrarInfo("Vuelo añadido correctamente");
                cargarTabla();
                limpiarFormulario();

            } else {

                mostrarError("Error", "No se pudo insertar el vuelo");
            }

        } catch (Exception e) {

            mostrarError("Error", e.getMessage());
        }
    }

    // ELIMINAR

    @FXML
    public void onDelete() {

        Flight seleccionado = tabla.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            mostrarError("Error", "Selecciona un vuelo");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmación");
        alert.setHeaderText("Eliminar vuelo");
        alert.setContentText(
                "¿Seguro que quieres eliminar el vuelo "
                        + seleccionado.getNumFlight() + "?"
        );

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            dao.eliminar(seleccionado.getIdFlight());

            cargarTabla();
            limpiarFormulario();

            mostrarInfo("Vuelo eliminado");
        }
    }

    // ACTUALIZAR

    @FXML
    public void onActualizar() {

        try {

            if (!validarCampos()) return;

            Flight f = new Flight(
                    Integer.parseInt(txtId.getText()),
                    txtNum.getText(),
                    txtDestino.getText(),
                    dateSalida.getValue(),
                    Integer.parseInt(txtDuracion.getText())
            );

            boolean actualizado = dao.actualizar(f);

            if (actualizado) {

                mostrarInfo("Vuelo actualizado");
                cargarTabla();
                limpiarFormulario();

            } else {

                mostrarError("Error", "No se pudo actualizar");
            }

        } catch (Exception e) {

            mostrarError("Error", e.getMessage());
        }
    }

    // FILTRAR

    @FXML
    public void onFilter() {

        String filtro = cmbFiltro.getValue();

        if (filtro == null) return;

        switch (filtro) {

            case "Todos" -> cargarTabla();

            case "Más de 3 horas" -> {

                tabla.setItems(
                        FXCollections.observableArrayList(
                                dao.vuelosLargos()
                        )
                );
            }

            case "Por destino" -> {

                tabla.setItems(
                        FXCollections.observableArrayList(
                                dao.porDestino(txtFiltroDestino.getText())
                        )
                );
            }
        }
    }


    // VALIDAR


    private boolean validarCampos() {

        if (txtNum.getText().isEmpty()
                || txtDestino.getText().isEmpty()
                || txtDuracion.getText().isEmpty()
                || dateSalida.getValue() == null) {

            mostrarError("Campos vacíos",
                    "Debes rellenar todos los campos");

            return false;
        }

        try {

            Integer.parseInt(txtDuracion.getText());

        } catch (NumberFormatException e) {

            mostrarError("Error",
                    "La duración debe ser numérica");

            return false;
        }

        return true;
    }


    // LIMPIAR


    private void limpiarFormulario() {

        txtId.clear();
        txtNum.clear();
        txtDestino.clear();
        txtDuracion.clear();
        dateSalida.setValue(null);
    }


    // ALERT INFO


    private void mostrarInfo(String mensaje) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }


    // ALERT ERROR


    //esto lo hice para probarlo, no es necesario al 100% para el programa se puede hacer de otra forma,
    // solo queria experimentar con los alerts

    private void mostrarError(String titulo, String mensaje) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}