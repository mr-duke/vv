package fahrtenbuch;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Nachricht implements Serializable {

    private long telematikId;
    private String breitengrad;
    private String laengengrad;
    private int streckeGefahren;
    // Datum und Uhrzeit der Erzeugung der telematik.NachrichtDTO
    private LocalDateTime uhrzeit;

    public Nachricht() {}

    public Nachricht(long telematikId, String breitengrad, String laengengrad, int streckeGefahren, LocalDateTime uhrzeit) {
        this.telematikId = telematikId;
        this.breitengrad = breitengrad;
        this.laengengrad = laengengrad;
        this.streckeGefahren = streckeGefahren;
        this.uhrzeit = uhrzeit;
    }

    public long getTelematikId() {
        return telematikId;
    }

    public void setTelematikId(long telematikId) {
        this.telematikId = telematikId;
    }

    public String getBreitengrad() {
        return breitengrad;
    }

    public void setBreitengrad(String breitengrad) {
        this.breitengrad = breitengrad;
    }

    public String getLaengengrad() {
        return laengengrad;
    }

    public void setLaengengrad(String laengengrad) {
        this.laengengrad = laengengrad;
    }

    public int getStreckeGefahren() {
        return streckeGefahren;
    }

    public void setStreckeGefahren(int streckeGefahren) {
        this.streckeGefahren = streckeGefahren;
    }

    public LocalDateTime getUhrzeit() {
        return uhrzeit;
    }

    public void setUhrzeit(LocalDateTime uhrzeit) {
        this.uhrzeit = uhrzeit;
    }

    @Override
    public String toString() {
        return "fahrtenbuch.Nachricht{" +
                "telematikId=" + telematikId +
                ", breitengrad='" + breitengrad + '\'' +
                ", laengengrad='" + laengengrad + '\'' +
                ", streckeGefahren=" + streckeGefahren +
                ", uhrzeit=" + uhrzeit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nachricht nachricht = (Nachricht) o;
        return telematikId == nachricht.telematikId &&
                Objects.equals(uhrzeit, nachricht.uhrzeit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telematikId, uhrzeit);
    }
}
