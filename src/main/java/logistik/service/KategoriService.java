package logistik.service;

import logistik.config.DatabaseConnection;
import logistik.model.Barang;
import logistik.model.Kategori;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class KategoriService {
    private Connection conn;

    public KategoriService() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    public List<Kategori> tampilkanSemuaKategori() throws SQLException {
        List<Kategori> kategori = new ArrayList<>();
        String sql = "SELECT * FROM kategori_barang";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Kategori k = new Kategori(
                        rs.getInt("id"),
                        rs.getString("nama_kategori"));
                kategori.add(k);
            }
        }
        return kategori;
    }
}
