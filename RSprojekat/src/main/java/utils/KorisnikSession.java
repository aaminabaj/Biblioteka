package utils;

import models.Korisnik;

public class KorisnikSession {
    // Cuvanje korisnicke sesije
    private static Korisnik trenutniKorisnik;

    public static void setKorisnik(Korisnik korisnik) {
        trenutniKorisnik = korisnik;
    }

    public static Korisnik getKorisnik() {
        return trenutniKorisnik;
    }

    public static void clear() {
        trenutniKorisnik = null;
    }
}
