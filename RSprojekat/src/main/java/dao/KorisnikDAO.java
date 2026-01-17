package dao;

import models.Korisnik;
import utils.BazaUtil;
import utils.PassUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KorisnikDAO {
    // Create
    public void create(Korisnik k) {
        String sql = "INSERT INTO korisnik(ime, prezime, username, password_hash) VALUES(?,?,?,?)";
        try (Connection c = BazaUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, k.getIme());
            ps.setString(2, k.getPrezime());
            ps.setString(3, k.getUsername());
            ps.setString(4, k.getPasswordHash());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Login
    public static Korisnik login(String username, String rawPassword) {
        String sql = "SELECT * FROM korisnik WHERE username=?";

        try (Connection c = BazaUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("password_hash");

                if (PassUtil.checkPassword(rawPassword, hash)) {
                    Korisnik k = new Korisnik();
                    k.setId(rs.getInt("id"));
                    k.setIme(rs.getString("ime"));
                    k.setPrezime(rs.getString("prezime"));
                    k.setUsername(rs.getString("username"));
                    k.setPasswordHash(hash);
                    return k;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Provjera da li postoji korisnik sa username-om
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM korisnik WHERE username = ?";
        try (Connection conn = BazaUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
