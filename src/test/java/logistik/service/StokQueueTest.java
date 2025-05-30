package logistik.service;

import logistik.Model.Barang;
import logistik.Model.StokBarang;
import logistik.Service.AuthService;
import logistik.Service.BarangService;
import logistik.Service.StokQueue;
import logistik.Service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StokQueueTest {
    private static Connection conn;
    private static StokQueue stokQueue;

    @BeforeAll
    static void setupDatabase() {
        conn = logistik.Config.DatabaseConnection.getConnection();
    }

    @BeforeEach
    void setUp() {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM stok_barang");
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
    public void testEnqueue() {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang = new StokBarang(barang.getKode(), 10, LocalDateTime.now());

        try {
            stokQueue = new StokQueue(200);
            stokQueue.enqueue(stokBarang);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDequeue() {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));
        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        try {
            stokQueue = new StokQueue(200);
            stokQueue.enqueue(stokBarang1);
            stokQueue.enqueue(stokBarang2);
            stokQueue.enqueue(stokBarang3);

            stokQueue.dequeue(barang.getKode(), 11);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPeek() {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));

        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        try {
            stokQueue = new StokQueue(200);
            stokQueue.enqueue(stokBarang1);
            stokQueue.enqueue(stokBarang2);
            stokQueue.enqueue(stokBarang3);

            stokQueue.peek(barang.getKode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllStok() {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));

        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        try {
            stokQueue = new StokQueue(200);
            stokQueue.enqueue(stokBarang1);
            stokQueue.enqueue(stokBarang2);
            stokQueue.enqueue(stokBarang3);

            stokQueue.getAllStok(barang.getKode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void size() {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));

        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        try {
            stokQueue = new StokQueue(200);
            stokQueue.enqueue(stokBarang1);
            stokQueue.enqueue(stokBarang2);
            stokQueue.enqueue(stokBarang3);

            stokQueue.size(barang.getKode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void clear() {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));

        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        try {
            stokQueue = new StokQueue(200);
            stokQueue.enqueue(stokBarang1);
            stokQueue.enqueue(stokBarang2);
            stokQueue.enqueue(stokBarang3);

            stokQueue.clear(barang.getKode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
