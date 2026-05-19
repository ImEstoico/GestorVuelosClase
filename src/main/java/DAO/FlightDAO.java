package DAO;

import POJO.Flight;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FlightDAO {

    private final Connection con;

    public FlightDAO() throws SQLException {
        this.con = ConexionBD.getInstancia().getConexion();
    }

    public ArrayList<Flight> obtenerTodos() {

        ArrayList<Flight> lista = new ArrayList<>();

        String sql = "SELECT * FROM flight";

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                Flight f = new Flight(
                        rs.getInt("id_flight"),
                        rs.getString("num_flight"),
                        rs.getString("destination"),
                        rs.getDate("departure").toLocalDate(),
                        rs.getInt("duration")
                );

                lista.add(f);
            }

        } catch (SQLException e) {
            System.out.println("Error obtener todos flights: " + e.getMessage());
        }

        return lista;
    }


    public Flight obtenerPorId(int id) {

        Flight f = null;

        String sql = "SELECT * FROM flight WHERE id_flight=?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    f = new Flight(
                            rs.getInt("id_flight"),
                            rs.getString("num_flight"),
                            rs.getString("destination"),
                            rs.getDate("departure").toLocalDate(),
                            rs.getInt("duration")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error obtener por id: " + e.getMessage());
        }

        return f;
    }

    public boolean insertar(Flight f) {

        String sql = """
            INSERT INTO flight(num_flight, destination, departure, duration)
            VALUES (?,?,?,?)
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, f.getNumFlight());
            pstmt.setString(2, f.getDestination());
            pstmt.setDate(3, Date.valueOf(f.getDeparture()));
            pstmt.setInt(4, f.getDuration());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error insertar flight: " + e.getMessage());
            return false;
        }
    }
    public boolean actualizar(Flight f) {

        String sql = """
                UPDATE flight
                SET num_flight=?, destination=?, departure=?, duration=?
                WHERE id_flight=?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, f.getNumFlight());
            pstmt.setString(2, f.getDestination());
            pstmt.setDate(3, Date.valueOf(f.getDeparture()));
            pstmt.setInt(4, f.getDuration());
            pstmt.setInt(5, f.getIdFlight());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error actualizar flight: " + e.getMessage());
            return false;
        }
    }


    public boolean eliminar(int id) {

        String sql = "DELETE FROM flight WHERE id_flight=?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminar flight: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Flight> vuelosLargos() {

        ArrayList<Flight> lista = new ArrayList<>();

        String sql = "SELECT * FROM flight WHERE duration > 180";

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                Flight f = new Flight(
                        rs.getInt("id_flight"),
                        rs.getString("num_flight"),
                        rs.getString("destination"),
                        rs.getDate("departure").toLocalDate(),
                        rs.getInt("duration")
                );

                lista.add(f);
            }

        } catch (SQLException e) {
            System.out.println("Error vuelos largos: " + e.getMessage());
        }

        return lista;
    }


    public ArrayList<Flight> porDestino(String destino) {

        ArrayList<Flight> lista = new ArrayList<>();

        String sql = "SELECT * FROM flight WHERE destination = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, destino);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {

                    Flight f = new Flight(
                            rs.getInt("id_flight"),
                            rs.getString("num_flight"),
                            rs.getString("destination"),
                            rs.getDate("departure").toLocalDate(),
                            rs.getInt("duration")
                    );

                    lista.add(f);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error filtrar por destino: " + e.getMessage());
        }

        return lista;
    }
}