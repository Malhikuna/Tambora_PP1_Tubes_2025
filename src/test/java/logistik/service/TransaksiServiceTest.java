package logistik.service;

import logistik.Model.Barang;
import logistik.Model.Transaksi;
import logistik.Service.AuthService;
import logistik.Service.BarangService;
import logistik.Service.TransaksiService;
import logistik.Service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransaksiServiceTest {
    private static Connection conn;

    @BeforeAll
    static void setupDatabase() {
        conn = logistik.Config.DatabaseConnection.getConnection();
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
    public void testTambahTransaksi() {
        Barang barang = new Barang("B001","Indomie" , 1, "Pcs", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        Transaksi transaksi = new Transaksi(barang.getKode(), "Masuk", 5);
        TransaksiService transaksiService = new TransaksiService();
        transaksiService.tambahTransaksi(transaksi);
        assertEquals("B001", transaksi.getKodeBarang());
    }
}