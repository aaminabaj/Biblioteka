package dao;

import models.Knjiga;
import utils.BazaUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KnjigaDAO implements CRUDOperacije<Knjiga>{
    // Create
    @Override
    public void kreiraj(Knjiga obj) {
        String sql = "INSERT INTO knjiga(naziv, autor, zanr, izdavacka_kuca, godina_izdavanja, status) VALUES(?,?,?,?,?,?)";

        try (Connection c = BazaUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, obj.getNaziv());
            ps.setString(2, obj.getAutor());
            ps.setString(3, obj.getZanr().name());
            ps.setString(4, obj.getIzdavackaKuca());
            ps.setInt(5, obj.getGodinaIzdavanja());
            ps.setBoolean(6, obj.isStatus());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read
    @Override
    public List<Knjiga> dohvatiSve() {
        List<Knjiga> list = new ArrayList<>();
        String sql = "SELECT * FROM knjiga";

        try (Connection c = BazaUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Knjiga k = new Knjiga();
                k.setId(rs.getInt("id"));
                k.setNaziv(rs.getString("naziv"));
                k.setAutor(rs.getString("autor"));
                k.setZanr(Knjiga.Zanr.valueOf(rs.getString("zanr")));
                k.setIzdavackaKuca(rs.getString("izdavacka_kuca"));
                k.setGodinaIzdavanja(rs.getInt("godina_izdavanja"));
                k.setStatus(rs.getBoolean("status"));
                list.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Update
    @Override
    public void azuriraj(Knjiga obj) {
        String sql = "UPDATE knjiga SET naziv=?, autor=?, zanr=?, izdavacka_kuca=?, godina_izdavanja=?, status=? WHERE id=?";

        try (Connection c = BazaUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, obj.getNaziv());
            ps.setString(2, obj.getAutor());
            ps.setString(3, obj.getZanr().name());
            ps.setString(4, obj.getIzdavackaKuca());
            ps.setInt(5, obj.getGodinaIzdavanja());
            ps.setBoolean(6, obj.isStatus());
            ps.setInt(7, obj.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete
    @Override
    public void brisi(int id) {
        String sql = "DELETE FROM knjiga WHERE id=?";

        try (Connection c = BazaUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Provjera da li knjiga ima posudbu
    public boolean imaPosudbe(int knjigaId) {
        String sql = "SELECT COUNT(*) FROM posudba WHERE knjiga_id = ?";
        try (Connection conn = BazaUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, knjigaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
