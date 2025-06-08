package logistik.service;

import logistik.config.DatabaseConnection;
import logistik.model.Barang;
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
    public void enqueue(Connection conn, StokBarang stok) throws SQLException {
        String sql = "INSERT INTO stok_barang (kode_barang, jumlah, tanggal_masuk) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stok.getKodeBarang());
            stmt.setInt(2, stok.getJumlah());
            stmt.setTimestamp(3, Timestamp.valueOf(stok.getTanggalMasuk()));
            stmt.executeUpdate();
        }
    }

    // Dequeue: Mengeluarkan sejumlah stok barang berdasarkan prinsip FIFO (First-In, First-Out).
    public void dequeue(Connection conn, String kodeBarang, int jumlahKeluar) throws SQLException {

        String selectSql = "SELECT * FROM stok_barang WHERE kode_barang = ? AND jumlah > 0 ORDER BY tanggal_masuk ASC";

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
                throw new SQLException("Error internal: Stok tidak mencukupi saat proses dequeue, rollback dilakukan. Sisa: " + remainingToDequeue);
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

    public List<StokBarang> dapatkanSemuaStok() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        List<StokBarang> stok = new ArrayList<>();
        String sql = "SELECT s.*, b.nama_barang " +
                "FROM stok_barang s\n" + "JOIN barang b ON s.kode_barang = b.kode_barang\n";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                StokBarang s = new StokBarang(
                        rs.getString("kode_barang"),
                        rs.getInt("jumlah"),
                        rs.getTimestamp("tanggal_masuk").toLocalDateTime());
                s.setNamaBarang(rs.getString("nama_barang"));
                stok.add(s);
            }
        }
        return stok;
    }
}
