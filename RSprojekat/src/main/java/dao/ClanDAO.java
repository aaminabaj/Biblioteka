package dao;

import models.Clan;
import utils.BazaUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClanDAO implements CRUDOperacije<Clan>{
    // Create
    @Override
    public void kreiraj(Clan obj) {
        String sql = "INSERT INTO clan(broj_clanske, ime, prezime, mail, datum_pridruzivanja) VALUES(?,?,?,?,?)";

        try (Connection conn = BazaUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,obj.getBrojClanske());
            ps.setString(2, obj.getIme());
            ps.setString(3, obj.getPrezime());
            ps.setString(4, obj.getMail());
            ps.setDate(5, Date.valueOf(obj.getDatumPridruzivanja()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read
    @Override
    public List<Clan> dohvatiSve() {
        List<Clan> list = new ArrayList<>();
        String sql = "SELECT * FROM clan";

        try (Connection conn = BazaUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Clan c = new Clan();
                c.setId(rs.getInt("id"));
                c.setBrojClanske(rs.getString("broj_clanske"));
                c.setIme(rs.getString("ime"));
                c.setPrezime(rs.getString("prezime"));
                c.setMail(rs.getString("mail"));
                c.setDatumPridruzivanja(rs.getDate("datum_pridruzivanja").toLocalDate());

                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Update
    @Override
    public void azuriraj(Clan obj) {
        String sql = "UPDATE clan SET broj_clanske=?, ime=?, prezime=?, mail=?, datum_pridruzivanja=? WHERE id=?";

        try (Connection conn = BazaUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getBrojClanske());
            ps.setString(2, obj.getIme());
            ps.setString(3, obj.getPrezime());
            ps.setString(4, obj.getMail());
            ps.setDate(5, Date.valueOf(obj.getDatumPridruzivanja()));
            ps.setInt(6, obj.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete
    @Override
    public void brisi(int id) {
        String sql = "DELETE FROM clan WHERE id=?";

        try (Connection conn = BazaUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Provjera da li clan ima posudbe
    public boolean imaPosudbe(int clanId) {
        String sql = "SELECT COUNT(*) FROM posudba WHERE clan_id = ?";
        try (Connection conn = BazaUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clanId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Provjera da li postoji clan sa brojem clanske karte
    public boolean existsByBrojClanske(String brojClanske) {
        String sql = "SELECT COUNT(*) FROM clan WHERE broj_clanske = ?";
        try (Connection conn = BazaUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, brojClanske);
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
