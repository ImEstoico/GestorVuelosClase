module com.elcojo.gestorvuelos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;



    opens com.elcojo.gestorvuelos to javafx.fxml;
    exports com.elcojo.gestorvuelos;
}