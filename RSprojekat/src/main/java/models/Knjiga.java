package models;


public class Knjiga {
    public enum Zanr{
        Drama, SciFi, Romantika, Akcija, Krimi, Biografija, Dječija, Psihologija, Filozofija
    }

    private int id;
    private String naziv;
    private String autor;
    private Zanr zanr;
    private String izdavackaKuca;
    private int godinaIzdavanja;
    private boolean status;

    public Knjiga() {
    }

    public Knjiga(int id, String naziv, String autor, Zanr zanr, String izdavackaKuca, int godinaIzdavanja) {
        this.id = id;
        this.naziv = naziv;
        this.autor = autor;
        this.zanr = zanr;
        this.izdavackaKuca = izdavackaKuca;
        this.godinaIzdavanja = godinaIzdavanja;
        this.status = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Zanr getZanr() {
        return zanr;
    }

    public void setZanr(Zanr zanr) {
        this.zanr = zanr;
    }

    public String getIzdavackaKuca() {
        return izdavackaKuca;
    }

    public void setIzdavackaKuca(String izdavackaKuca) {
        this.izdavackaKuca = izdavackaKuca;
    }

    public int getGodinaIzdavanja() {
        return godinaIzdavanja;
    }

    public void setGodinaIzdavanja(int godinaIzdavanja) {
        this.godinaIzdavanja = godinaIzdavanja;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.naziv + " - " + this.autor;
    }
}
