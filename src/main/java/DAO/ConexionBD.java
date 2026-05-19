package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Datos de conexión
    private static final String HOST = "localhost:3306";
    private static final String DB_NAME = "app_flights";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "mentefria11";

    // URL completa
    private static final String URL = "jdbc:mysql://" + HOST + "/" + DB_NAME;

    private static ConexionBD instancia;
    private Connection conexion;

    // Singleton
    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    private ConexionBD() {
    }

    /**
     * Devuelve una conexión a la BD
     */
    public Connection getConexion() {

        try {

            if (conexion == null || conexion.isClosed()) {

                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);

                System.out.println(" Conexion hecha correctamente GOTTTTY");
            }

        } catch (SQLException e) {

            System.out.println(" Conexion fallada -XXXXXXXXXX- ");
            System.out.println("Error: " + e.getMessage());
        }

        return conexion;
    }
}