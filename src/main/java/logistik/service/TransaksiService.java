package logistik.service;

import logistik.config.DatabaseConnection;
import logistik.model.StokBarang;
import logistik.model.Transaksi;
import logistik.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransaksiService {
    /* Atribut */
    private Connection conn;
    private StokQueue stokQueue;
    private StokBarang stokBarang;

    // Constructor
    public TransaksiService() throws SQLException {
        conn = DatabaseConnection.getConnection();
        stokQueue = new StokQueue();
    }

    // Prosedur untuk menambahkan transaksi baru ke queue
    public void tambahTransaksi(Connection conn, Transaksi transaksi, String userId) throws SQLException {
        String sql = "INSERT INTO transaksi (id, kode_barang, jenis, jumlah, tanggal, user_id) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transaksi.getId().toString());
            stmt.setString(2, transaksi.getKodeBarang());
            stmt.setString(3, transaksi.getJenis());
            stmt.setInt(4, transaksi.getJumlah());
            stmt.setTimestamp(5, Timestamp.valueOf(transaksi.getTanggal()));
            stmt.setString(6, userId);
            stmt.executeUpdate();
        }
    }

    // Prosedur untuk menampilkan semua transaksi dalam queue
    public List<Transaksi> tampilkanSemuaTransakasi() throws SQLException {
        List<Transaksi> transaksi = new ArrayList<>();
        String sql = "SELECT t.*, b.nama_barang, u.name " +
                "FROM transaksi t\n" + "JOIN barang b ON t.kode_barang = b.kode_barang\n"
                + "JOIN users u ON t.user_id = u.id\n";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                LocalDateTime rawTanggal = rs.getTimestamp("tanggal").toLocalDateTime();
                Transaksi t = new Transaksi(
                        rs.getString("kode_barang"),
                        rs.getString("jenis"),
                        rs.getInt("jumlah"),
                        rawTanggal);
                t.setNamaBarang(rs.getString("nama_barang"));
                t.setNamaUser(rs.getString("name"));
                        transaksi.add(t);
            }
        }
        return transaksi;
    }
}
