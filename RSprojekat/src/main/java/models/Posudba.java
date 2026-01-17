package models;

import java.time.LocalDate;

public class Posudba {
    private int id;
    private Clan clan;
    private Knjiga knjiga;
    private LocalDate datumPosudbe;
    private LocalDate datumVracanja;

    public Posudba() {
    }

    public Posudba(int id, Clan clan, Knjiga knjiga, LocalDate datumPosudbe) {
        this.id = id;
        this.clan = clan;
        this.knjiga = knjiga;
        this.datumPosudbe = datumPosudbe;
        this.datumVracanja = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public Knjiga getKnjiga() {
        return knjiga;
    }

    public void setKnjiga(Knjiga knjiga) {
        this.knjiga = knjiga;
    }

    public LocalDate getDatumPosudbe() {
        return datumPosudbe;
    }

    public void setDatumPosudbe(LocalDate datumPosudbe) {
        this.datumPosudbe = datumPosudbe;
    }

    public LocalDate getDatumVracanja() {
        return datumVracanja;
    }

    public void setDatumVracanja(LocalDate datumVracanja) {
        this.datumVracanja = datumVracanja;
    }
}
