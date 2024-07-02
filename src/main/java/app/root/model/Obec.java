package app.root.model;

import jakarta.persistence.*;

/**
 * class for data definition
 */
@Entity
@Table
public class Obec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String nazev;
    private String kod;

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public Obec() {

    }
}
