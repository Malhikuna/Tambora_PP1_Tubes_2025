package logistik.service;

import logistik.config.DatabaseConnection;
import logistik.model.StokBarang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StokQueue {
    public StokQueue() {}

    public boolean isEmpty(String kodeBarang) throws SQLException {
        String sql = "SELECT 1 FROM stok_barang WHERE kode_barang = ? AND jumlah > 0 LIMIT 1";
        try (   Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);
            try (ResultSet rs = stmt.executeQuery()) {
                return !rs.next();
            }
        }
    }

    public boolean isFull(String kodeBarang, int kapasitasMaksimum) throws SQLException {
        int totalStok = getStokCount(kodeBarang);
        return totalStok >= kapasitasMaksimum;
    }

    // Enqueue: Menambahkan batch stok baru ke database.
    public void enqueue(StokBarang stok) throws SQLException {
        if (stok == null || stok.getKodeBarang() == null || stok.getKodeBarang().trim().isEmpty() || stok.getJumlah() <= 0) {
            throw new IllegalArgumentException("Data stok tidak valid atau jumlah tidak positif.");
        }
        String sql = "INSERT INTO stok_barang (kode_barang, jumlah, tanggal_masuk) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stok.getKodeBarang());
            stmt.setInt(2, stok.getJumlah());
            stmt.setTimestamp(3, Timestamp.valueOf(stok.getTanggalMasuk()));
            stmt.executeUpdate();
        }
    }

    // Dequeue: Mengeluarkan sejumlah stok barang berdasarkan prinsip FIFO (First-In, First-Out).
    public boolean dequeue(String kodeBarang, int jumlahKeluar) throws SQLException {
        if(isEmpty(kodeBarang)) {
            System.out.println("Stok barang kosong");
            return false;
        }

        if (jumlahKeluar <= 0) {
            throw new IllegalArgumentException("Jumlah keluar harus lebih besar dari 0.");
        }

        int totalStokSaatIni = getStokCount(kodeBarang);
        if (totalStokSaatIni < jumlahKeluar) {
            System.err.println("Stok barang " + kodeBarang + " tidak mencukupi. Diminta: " + jumlahKeluar + ", Tersedia: " + totalStokSaatIni);
            return false;
        }

        Connection conn = null;
        boolean originalAutoCommit = true;

        String selectSql = "SELECT * FROM stok_barang WHERE kode_barang = ? AND jumlah > 0 ORDER BY tanggal_masuk ASC";
        try {
            conn = DatabaseConnection.getConnection();
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                selectStmt.setString(1, kodeBarang);
                ResultSet rs = selectStmt.executeQuery();

                int remainingToDequeue  = jumlahKeluar;

                while (rs.next() && remainingToDequeue  > 0) {
                    int jumlahTersediaDiBatch = rs.getInt("jumlah");;
                    int akanDikeluarkanDariBatch = Math.min(jumlahTersediaDiBatch, remainingToDequeue );

                    if (jumlahTersediaDiBatch - akanDikeluarkanDariBatch > 0) {
                        rs.updateInt("jumlah", jumlahTersediaDiBatch - akanDikeluarkanDariBatch);
                        rs.updateRow(); // update jumlah di DB
                    } else {
                        rs.deleteRow(); // hapus jika stok habis
                    }
                    remainingToDequeue -= akanDikeluarkanDariBatch;
                }

                if (remainingToDequeue > 0) {
                    // Jika ini terjadi, berarti pengecekan stok awal gagal atau ada kondisi balapan.
                    // Rollback untuk menjaga konsistensi.
                    conn.rollback();
                    System.err.println("Error internal: Stok tidak mencukupi saat proses dequeue, rollback dilakukan. Sisa: " + remainingToDequeue);
                    return false; // Gagal dequeue sepenuhnya
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException exRollback) {
                    System.err.println("Error saat melakukan rollback: " + exRollback.getMessage());
                }
            }
            throw new SQLException("Gagal melakukan dequeue untuk barang " + kodeBarang + ": " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommit); // Kembalikan status autocommit
                    conn.close(); // Tutup koneksi
                } catch (SQLException exClose) {
                    System.err.println("Error saat menutup koneksi atau mengembalikan autocommit: " + exClose.getMessage());
                }
            }
        }
    }

    // Mengambil semua batch stok untuk kode barang tertentu, diurutkan berdasarkan tanggal masuk (FIFO).
    public List<StokBarang> getAllStok(String kodeBarang) throws SQLException {
        String sql = "SELECT * FROM stok_barang WHERE kode_barang = ? ORDER BY tanggal_masuk ASC";
        List<StokBarang> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StokBarang sb = new StokBarang(
                            rs.getString("kode_barang"),
                            rs.getInt("jumlah"),
                            rs.getTimestamp("tanggal_masuk").toLocalDateTime()
                    );
                    list.add(sb);
                }
            }
        }
        return list;
    }

    // Peek: Melihat (tanpa mengeluarkan) batch stok tertua untuk kode barang tertentu.
    public StokBarang peek(String kodeBarang) throws SQLException {
        String sql = "SELECT * FROM stok_barang WHERE kode_barang = ? AND jumlah > 0 ORDER BY tanggal_masuk ASC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);

            try (ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    return new StokBarang(
                            rs.getString("kode_barang"),
                            rs.getInt("jumlah"),
                            rs.getTimestamp("tanggal_masuk").toLocalDateTime()
                    );
                } else {
                    System.out.println("Queue Kosong di Database");
                    return null;
                }
            }
        }
    }

    // Size: Mendapatkan total jumlah stok untuk kode barang tertentu.
    public int getStokCount(String kodeBarang) throws SQLException {
        String sql = "SELECT SUM(jumlah) AS total FROM stok_barang WHERE kode_barang = ? AND jumlah > 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);

            try (ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    // Clear: Menghapus semua batch stok untuk kode barang tertentu.
    public int clear(String kodeBarang) throws SQLException {
        String sql = "DELETE FROM stok_barang WHERE kode_barang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);
            return stmt.executeUpdate();
            //System.out.println("Berhasil menghapus " + rows + " baris stok untuk " + kodeBarang);
        }
    }
}
