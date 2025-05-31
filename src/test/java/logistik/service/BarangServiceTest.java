package logistik.service;

import logistik.model.Barang;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarangServiceTest {
    private static Connection conn;

    @BeforeAll
    static void setupDatabase() {
        conn = logistik.config.DatabaseConnection.getConnection();
    }

    @BeforeEach
    void setUp() {
        try {
            Statement stmt = conn.createStatement();
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
    public void testTambahBarang() {
        Barang barang = new Barang("B001","Indomie" , 1, "Pcs", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        System.out.println("Nama Barang: " + barang.getNama());

        Barang result = barangService.cariBarang(barang.getKode());

        assertEquals("Indomie", result.getNama());
    }

    @Test
    public void testUpdateBarang() {
        Barang barang = new Barang("B001","Beras" , 1, "Kg", 10000, 12000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        System.out.println("Sebelum Update");
        System.out.println("Nama Barang: " + barang.getNama());

        barang.setNama("Minyak");
        barangService.updateBarang(barang);

        System.out.println("Setelah Update");
        System.out.println("Nama Barang: " + barang.getNama());

        Barang result = barangService.cariBarang(barang.getKode());

        assertEquals("Minyak", result.getNama());
    }

    @Test
    public void testHapusBarang() {
        Barang barang = new Barang("B001","Beras" , 1, "Kg", 10000, 12000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        String kodeBarang = barang.getKode();

        barangService.hapusBarang(kodeBarang);

        List<Barang> barangList = barangService.tampilkanSemuaBarang();

        assertEquals(0, barangList.size());
    }

    @Test
    public void testCariBarang() {
        Barang barang = new Barang("B001","Beras" , 1, "Kg", 10000, 12000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        Barang result = barangService.cariBarang(barang.getKode());

        assertEquals("Beras", result.getNama());
    }

    @Test
    public void testTampilkanSemuaBarang() {
        Barang barang1 = new Barang("B001","Beras" , 1, "Kg", 10000, 12000);
        Barang barang2 = new Barang("B002","Minyak" , 1, "Kg", 10000, 12000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang1);
        barangService.tambahBarang(barang2);

        List<Barang> barangList = barangService.tampilkanSemuaBarang();

        System.out.println("Nama Barang 1: " + barangList.get(0).getNama());
        System.out.println("Nama Barang 2: " + barangList.get(1).getNama());

        assertEquals("Beras", barangList.get(0).getNama());
        assertEquals("Minyak", barangList.get(1).getNama());
    }
}
