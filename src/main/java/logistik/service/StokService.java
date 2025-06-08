package logistik.service;

import logistik.config.DatabaseConnection;
import logistik.model.Barang;
import logistik.model.StokBarang;
import logistik.model.Transaksi;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class StokService {
    private Connection conn;
    private StokQueue stokQueue;
    private BarangService barangService;
    private TransaksiService transaksiService;

    // Constructor
    public StokService() throws SQLException {
        conn = DatabaseConnection.getConnection();
        stokQueue = new StokQueue();
        barangService = new BarangService();
        transaksiService = new TransaksiService();
    }

    public void catatBarang(String kodeBarang, int jumlah, String jenis, LocalDateTime tanggal)
            throws SQLException, IllegalArgumentException {

        // 1. Validasi Awal yang Berlaku untuk Semua Jenis
        // Cek dulu apakah barangnya ada di database
        if (barangService.cariBarang(kodeBarang) == null) {
            throw new IllegalArgumentException("Barang dengan kode '" + kodeBarang + "' tidak ditemukan.");
        }

        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah barang harus positif.");
        }

        // 2. Routing berdasarkan jenis transaksi
        if ("Masuk".equalsIgnoreCase(jenis)) {
            prosesBarangMasuk(kodeBarang, jumlah, tanggal);
        } else if ("Keluar".equalsIgnoreCase(jenis)) {
            prosesBarangKeluar(kodeBarang, jumlah, tanggal);
        } else {
            throw new IllegalArgumentException("Jenis transaksi tidak valid: '" + jenis + "'. Harap gunakan 'Masuk' atau 'Keluar'.");
        }
    }

    /**
     * Metode privat untuk menangani logika dan transaksi BARANG MASUK.
     */
    private void prosesBarangMasuk(String kodeBarang, int jumlah, LocalDateTime tanggal) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi

            // Operasi 1: Tambah ke stok (Enqueue)
            StokBarang batchBaru = new StokBarang(kodeBarang, jumlah, tanggal);
            stokQueue.enqueue(conn, batchBaru); // Asumsi metode ini sudah dimodifikasi

            // Operasi 2: Catat log transaksi
            Transaksi transaksi = new Transaksi(kodeBarang, "Masuk", jumlah, tanggal);
            transaksiService.tambahTransaksi(conn, transaksi); // Asumsi metode ini sudah dimodifikasi

            conn.commit(); // Jika semua berhasil, commit

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Gagal mencatat barang masuk: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Metode privat untuk menangani logika dan transaksi BARANG KELUAR.
     */
    private void prosesBarangKeluar(String kodeBarang, int jumlah, LocalDateTime tanggal) throws SQLException, IllegalArgumentException {
        // Validasi KHUSUS untuk barang keluar: Cek apakah stoknya cukup!
        int stokTersedia = stokQueue.getStokCount(kodeBarang); // Asumsi ada metode ini
        if (stokTersedia < jumlah) {
            throw new IllegalArgumentException("Stok tidak mencukupi. Tersedia: " + stokTersedia + ", Diminta: " + jumlah);
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi

            // Operasi 1: Kurangi dari stok (Dequeue)
            stokQueue.dequeue(conn, kodeBarang, jumlah);

            // Operasi 2: Catat log transaksi
            Transaksi transaksi = new Transaksi(kodeBarang, "Keluar", jumlah, tanggal);
            transaksiService.tambahTransaksi(conn, transaksi);

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Gagal mencatat barang keluar: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException exClose) {
                    // Sebaiknya di-log, tapi jangan lempar lagi agar tidak menimpa exception utama
                    exClose.printStackTrace();
                }
            }
        }
    }
}
