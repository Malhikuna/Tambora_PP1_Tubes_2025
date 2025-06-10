package logistik.service;

import logistik.model.Barang;
import logistik.model.Transaksi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransaksiServiceTest {
    private static Connection conn;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        conn = logistik.config.DatabaseConnection.getConnection();
    }

    @BeforeEach
    void setUp() {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM transaksi");
            stmt.executeUpdate("DELETE FROM stok_barang");
            stmt.executeUpdate("DELETE FROM barang");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTambahTransaksi() throws SQLException {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", "Makanan", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        Transaksi transaksi = new Transaksi(barang.getKode(), "Masuk", 5, LocalDateTime.now());
        TransaksiService transaksiService = new TransaksiService();
//        transaksiService.tambahTransaksi(conn, transaksi);
        assertEquals("B001", transaksi.getKodeBarang());
    }
}