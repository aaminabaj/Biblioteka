package dao;

import models.Clan;
import models.Knjiga;
import models.Posudba;
import utils.BazaUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PosudbaDAO {

    // Posudi knjigu
    public void posudi(Posudba p) {
        String sqlInsert = "INSERT INTO posudba(clan_id, knjiga_id, datum_posudbe, datum_vracanja) VALUES(?,?,?,?)";
        String sqlUpdateKnjiga = "UPDATE knjiga SET status=FALSE WHERE id=?";

        try (Connection conn = BazaUtil.getConnection()) {
            // Cuvanje u bazu
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, p.getClan().getId());
                ps.setInt(2, p.getKnjiga().getId());
                ps.setDate(3, Date.valueOf(p.getDatumPosudbe()));
                ps.setDate(4, null);
                ps.executeUpdate();
            }

            // Update statusa knjige
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateKnjiga)) {
                ps.setInt(1, p.getKnjiga().getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Vrati knjigu
    public void vrati(Posudba p) {
        String sqlUpdatePosudba = "UPDATE posudba SET datum_vracanja=? WHERE id=?";
        String sqlUpdateKnjiga = "UPDATE knjiga SET status=TRUE WHERE id=?";

        try (Connection conn = BazaUtil.getConnection()) {
            // Update posudbe
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdatePosudba)) {
                ps.setDate(1, Date.valueOf(p.getDatumVracanja()));
                ps.setInt(2, p.getId());
                ps.executeUpdate();
            }

            // Update statusa knjige
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateKnjiga)) {
                ps.setInt(1, p.getKnjiga().getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Dohvacanje svih posudbi
    public List<Posudba> dohvatiSvePosudbe() {
        List<Posudba> list = new ArrayList<>();
        String sql = """
            SELECT p.id AS posudba_id, p.datum_posudbe, p.datum_vracanja,
                   k.id AS knjiga_id, k.naziv, k.autor, k.zanr, k.izdavacka_kuca, k.godina_izdavanja, k.status,
                   c.id AS clan_id, c.broj_clanske, c.ime, c.prezime, c.mail, c.datum_pridruzivanja
            FROM posudba p
            JOIN knjiga k ON p.knjiga_id = k.id
            JOIN clan c ON p.clan_id = c.id
        """;

        try (Connection conn = BazaUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Knjiga k = new Knjiga();
                k.setId(rs.getInt("knjiga_id"));
                k.setNaziv(rs.getString("naziv"));
                k.setAutor(rs.getString("autor"));
                k.setZanr(Knjiga.Zanr.valueOf(rs.getString("zanr")));
                k.setIzdavackaKuca(rs.getString("izdavacka_kuca"));
                k.setGodinaIzdavanja(rs.getInt("godina_izdavanja"));
                k.setStatus(rs.getBoolean("status"));

                Clan c = new Clan();
                c.setId(rs.getInt("clan_id"));
                c.setBrojClanske(rs.getString("broj_clanske"));
                c.setIme(rs.getString("ime"));
                c.setPrezime(rs.getString("prezime"));
                c.setMail(rs.getString("mail"));
                c.setDatumPridruzivanja(rs.getDate("datum_pridruzivanja").toLocalDate());

                Posudba p = new Posudba();
                p.setId(rs.getInt("posudba_id"));
                p.setKnjiga(k);
                p.setClan(c);
                p.setDatumPosudbe(rs.getDate("datum_posudbe").toLocalDate());
                Date vracanje = rs.getDate("datum_vracanja");
                p.setDatumVracanja(vracanje != null ? vracanje.toLocalDate() : null);

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
