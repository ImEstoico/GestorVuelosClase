package POJO;

import java.time.LocalDate;

public class Flight {

    private int idFlight;
    private String numFlight;
    private String destination;
    private LocalDate departure;
    private int duration;

    public Flight() {
    }

    public Flight(int idFlight, String numFlight, String destination, LocalDate departure, int duration) {
        this.idFlight = idFlight;
        this.numFlight = numFlight;
        this.destination = destination;
        this.departure = departure;
        this.duration = duration;
    }


    public int getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
    }

    public String getNumFlight() {
        return numFlight;
    }

    public void setNumFlight(String numFlight) {
        this.numFlight = numFlight;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "idFlight=" + idFlight +
                ", numFlight='" + numFlight + '\'' +
                ", destination='" + destination + '\'' +
                ", departure=" + departure +
                ", duration=" + duration +
                '}';
    }
}