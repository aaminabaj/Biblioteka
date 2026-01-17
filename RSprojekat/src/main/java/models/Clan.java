package models;

import java.time.LocalDate;

public class Clan {
    private int id;
    private String brojClanske;
    private String ime;
    private String prezime;
    private String mail;
    private LocalDate datumPridruzivanja;

    public Clan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrojClanske() {
        return brojClanske;
    }

    public void setBrojClanske(String brojClanske) {
        this.brojClanske = brojClanske;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public LocalDate getDatumPridruzivanja() {
        return datumPridruzivanja;
    }

    public void setDatumPridruzivanja(LocalDate datumPridruzivanja) {
        this.datumPridruzivanja = datumPridruzivanja;
    }

    public Clan(int id, String brojClanske, String ime, String prezime, String mail, LocalDate datumPridruzivanja) {
        this.id = id;
        this.brojClanske = brojClanske;
        this.ime = ime;
        this.prezime = prezime;
        this.mail = mail;
        this.datumPridruzivanja = datumPridruzivanja;
    }

    @Override
    public String toString() {
        return this.brojClanske + " - " + this.ime + " " + this.prezime;
    }
}
