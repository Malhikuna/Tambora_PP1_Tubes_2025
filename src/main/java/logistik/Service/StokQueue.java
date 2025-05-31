package logistik.service;

import logistik.config.DatabaseConnection;
import logistik.model.StokBarang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StokQueue {
    private int capacity, front, rear, size;
    private Connection conn;

    public StokQueue(int capacity) {
        this.capacity = capacity;
        front = 0;
        rear = -1;
        size = 0;
        conn = DatabaseConnection.getConnection();
    }

    public boolean isEmpty(String kodeBarang) {
        String sql = "SELECT 1 FROM stok_barang WHERE kode_barang = ? AND jumlah > 0 LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);
            ResultSet rs = stmt.executeQuery();
            return !rs.next();  // TRUE jika tidak ada stok aktif
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // anggap kosong jika error
        }
    }

    public boolean isFull(String kodeBarang, int kapasitasMaksimum) {
        int totalStok = size(kodeBarang);
        return totalStok >= kapasitasMaksimum;
    }

    public void enqueue(StokBarang stok) throws SQLException {
        String sql = "INSERT INTO stok_barang (kode_barang, jumlah, tanggal_masuk) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stok.getKodeBarang());
            stmt.setInt(2, stok.getJumlah());
            stmt.setTimestamp(3, Timestamp.valueOf(stok.getTanggalMasuk()));
            stmt.executeUpdate();
        }
    }

    // Dequeue: ambil stok tertua untuk pengeluaran (FIFO)
    public boolean dequeue(String kodeBarang, int jumlahKeluar) throws SQLException {
        if(isEmpty(kodeBarang)) {
            System.out.println("Stok barang kosong");
            return false;
        }

        String selectSql = "SELECT * FROM stok_barang WHERE kode_barang = ? AND jumlah > 0 ORDER BY tanggal_masuk ASC";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            selectStmt.setString(1, kodeBarang);
            ResultSet rs = selectStmt.executeQuery();

            int remaining = jumlahKeluar;

            while (rs.next() && remaining > 0) {
                int jumlahTersedia = rs.getInt("jumlah");

                int dikurangkan = Math.min(jumlahTersedia, remaining);
                int sisa = jumlahTersedia - dikurangkan;

                if (sisa > 0) {
                    rs.updateInt("jumlah", sisa);
                    rs.updateRow(); // update jumlah di DB
                } else {
                    rs.deleteRow(); // hapus jika stok habis
                }

                remaining -= dikurangkan;
            }

            return remaining == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Menampilkan semua stok
    public List<StokBarang> getAllStok(String kodeBarang) throws SQLException {
        String sql = "SELECT * FROM stok_barang WHERE kode_barang = ? ORDER BY tanggal_masuk ASC";
        List<StokBarang> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StokBarang sb = new StokBarang(
                        rs.getString("kode_barang"),
                        rs.getInt("jumlah"),
                        rs.getTimestamp("tanggal_masuk").toLocalDateTime()
                );
                list.add(sb);
            }
        }
        return list;
    }

    public StokBarang peek(String kodeBarang) {
        String sql = "SELECT * FROM stok_barang WHERE kode_barang = ? AND jumlah > 0 ORDER BY tanggal_masuk ASC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);
            ResultSet rs = stmt.executeQuery();

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
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int size(String kodeBarang) {
        String sql = "SELECT SUM(jumlah) AS total FROM stok_barang WHERE kode_barang = ? AND jumlah > 0";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void clear(String kodeBarang) {
        String sql = "DELETE FROM stok_barang WHERE kode_barang = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeBarang);
            int rows = stmt.executeUpdate();
            System.out.println("Berhasil menghapus " + rows + " baris stok untuk " + kodeBarang);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
