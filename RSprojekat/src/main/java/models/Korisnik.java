package models;

public class Korisnik {
    private int id;
    private String ime;
    private String prezime;
    private String username;
    private String passwordHash;

    public String getIme() {
        return this.ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return this.prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Korisnik(int id, String ime, String prezime, String username, String passwordHash) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public Korisnik() {
    }
}
