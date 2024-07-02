package app.root.model;

import jakarta.persistence.*;

/**
 * class for data definition
 */
@Entity
@Table
public class CastObce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nazev;
    private String kodCastObce;
    private String kodObce;

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getKodCastObce() {
        return kodCastObce;
    }

    public void setKodCastObce(String kodCastObce) {
        this.kodCastObce = kodCastObce;
    }

    public String getKodObce() {
        return kodObce;
    }

    public void setKodObce(String kodObce) {
        this.kodObce = kodObce;
    }

    public CastObce() {

    }
}

