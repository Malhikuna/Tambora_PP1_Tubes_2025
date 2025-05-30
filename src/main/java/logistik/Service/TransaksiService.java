package logistik.Service;

import logistik.Config.DatabaseConnection;
import logistik.Model.Barang;
import logistik.Model.StokBarang;
import logistik.Model.Transaksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiService {
    /* Atribut */
    private Connection conn;
    private StokQueue stokQueue;
    private StokBarang stokBarang;

    // Constructor
    public TransaksiService() {
        conn = DatabaseConnection.getConnection();
        stokQueue = new StokQueue(100);
    }

    // Prosedur untuk menambahkan transaksi baru ke queue
    public void tambahTransaksi(Transaksi transaksi) {
        String sql = "INSERT INTO transaksi (id, kode_barang, jenis, jumlah, tanggal) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transaksi.getId().toString());
            stmt.setString(2, transaksi.getKodeBarang());
            stmt.setString(3, transaksi.getJenis());
            stmt.setInt(4, transaksi.getJumlah());
            stmt.setTimestamp(5, Timestamp.valueOf(transaksi.getTanggal()));
            stmt.executeUpdate();
            stokBarang = new StokBarang(transaksi.getKodeBarang(), transaksi.getJumlah(), transaksi.getTanggal());

            if(transaksi.getJenis().equalsIgnoreCase("Masuk")) {
                stokQueue.enqueue(stokBarang);
            } else {
                stokQueue.dequeue(transaksi.getKodeBarang(), transaksi.getJumlah());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Prosedur untuk menampilkan semua transaksi dalam queue
        public void tampilkanSemuaTransakasi () {
            String sql = "SELECT * FROM Transaksi";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    System.out.println("Kode Barang" + rs.getString("kode_barang"));
                    System.out.println("Jenis" + rs.getString("jenis"));
                    System.out.println("Jumlah" + rs.getInt("jumlah"));
                    System.out.println("Tanggal" + rs.getString("tanggal"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}
