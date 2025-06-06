package logistik.service;

import logistik.model.Barang;
import logistik.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangService {
    /* Atribut */
    private Connection conn;

    public BarangService() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    // Prosedur untuk menambahkan barang
    public void tambahBarang(Barang barang) throws SQLException {
       String sql = "INSERT INTO barang (kode_barang, nama_barang, kategori_id, satuan, harga_beli, harga_jual) VALUES (?,?,?,?,?,?)";
       try (PreparedStatement stmt = conn.prepareStatement(sql)) {
           stmt.setString(1, barang.getKode());
           stmt.setString(2, barang.getNama());
           stmt.setInt(3, barang.getKategoriId());
           stmt.setString(4, barang.getSatuan());
           stmt.setDouble(5, barang.getHargaBeli());
           stmt.setDouble(6, barang.getHargaBeli());
           stmt.executeUpdate();
       }
    }

    public void updateBarang(Barang barang) throws SQLException {
        String sql = "UPDATE barang SET nama_barang = ?, kategori_id = ?, satuan = ?, harga_beli = ? WHERE kode_barang = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, barang.getNama());
            stmt.setInt(2, barang.getKategoriId());
            stmt.setString(3, barang.getSatuan());
            stmt.setDouble(4, barang.getHargaBeli());
            stmt.setString(5, barang.getKode());
            stmt.executeUpdate();
        }
    }

    // Fungsi untuk mencari barang berdasarkan kode
    public Barang cariBarang(String kode) throws SQLException {
        String sql = "SELECT b.*, k.nama_kategori " +
                "FROM barang b\n" + "JOIN kategori_barang k ON b.kategori_id = k.id\n" + " WHERE kode_barang = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Barang barang = new Barang(
                    rs.getString("kode_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("kategori_id"),
                    rs.getString("satuan"),
                    rs.getString("nama_kategori"),
                    rs.getDouble("harga_beli"),
                    rs.getDouble("harga_jual"));
                return barang;
            }
        }
        return null;
    }

    // Prosedur untuk menampilkan semua barang
    public List<Barang> tampilkanSemuaBarang () throws SQLException {
        List<Barang> barang = new ArrayList<>();
        String sql = "SELECT b.*, k.nama_kategori " +
                "FROM barang b\n" + "JOIN kategori_barang k ON b.kategori_id = k.id\n";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Barang b = new Barang(
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("kategori_id"),
                        rs.getString("satuan"),
                        rs.getString("nama_kategori"),
                        rs.getDouble("harga_beli"),
                        rs.getDouble("harga_jual"));
                barang.add(b);
            }
        }
        return barang;
    }

    // Prosedur untuk menghapus barang
    public void hapusBarang(String kode) throws SQLException {
        String sql = "DELETE FROM barang WHERE kode_barang = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kode);
            stmt.executeUpdate();
        }
    }
}
