package logistik.service;

import logistik.model.Barang;
import logistik.model.StokBarang;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StokQueueTest {
    private static Connection conn;
    private static StokQueue stokQueue;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        conn = logistik.config.DatabaseConnection.getConnection();
    }

    @BeforeEach
    void setUp() {
        try {
            Statement stmt = conn.createStatement();
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
    public void testEnqueue() throws SQLException {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", "Makanan", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang = new StokBarang(barang.getKode(), 10, LocalDateTime.now());

        try {
            stokQueue = new StokQueue();
            stokQueue.enqueue(conn, stokBarang);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDequeue() throws SQLException {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", "Makanan", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));
        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        stokQueue = new StokQueue();
        stokQueue.enqueue(conn, stokBarang1);
        stokQueue.enqueue(conn, stokBarang2);
        stokQueue.enqueue(conn, stokBarang3);

        stokQueue.dequeue(conn, barang.getKode(), 11);
    }

    @Test
    public void testPeek() throws SQLException {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", "Makanan", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));

        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        try {
            stokQueue = new StokQueue();
            stokQueue.enqueue(conn, stokBarang1);
            stokQueue.enqueue(conn, stokBarang2);
            stokQueue.enqueue(conn, stokBarang3);

            stokQueue.peek(barang.getKode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllStok() throws SQLException {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", "Makanan", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));

        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        try {
            stokQueue = new StokQueue();
            stokQueue.enqueue(conn, stokBarang1);
            stokQueue.enqueue(conn, stokBarang2);
            stokQueue.enqueue(conn, stokBarang3);

            stokQueue.getAllStok(barang.getKode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void size() throws SQLException {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", "Makanan", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));

        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        try {
            stokQueue = new StokQueue();
            stokQueue.enqueue(conn, stokBarang1);
            stokQueue.enqueue(conn, stokBarang2);
            stokQueue.enqueue(conn, stokBarang3);

            stokQueue.getStokCount(barang.getKode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void clear() throws SQLException {
        Barang barang = new Barang("B001", "Indomie", 1, "Pcs", "Makanan", 1000, 2000);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);

        StokBarang stokBarang1 = new StokBarang(barang.getKode(), 10, LocalDateTime.now());
        StokBarang stokBarang2 = new StokBarang(barang.getKode(), 20, LocalDateTime.now().plusHours(1));

        StokBarang stokBarang3 = new StokBarang(barang.getKode(), 30, LocalDateTime.now().plusHours(2));

        try {
            stokQueue = new StokQueue();
            stokQueue.enqueue(conn, stokBarang1);
            stokQueue.enqueue(conn, stokBarang2);
            stokQueue.enqueue(conn, stokBarang3);

            stokQueue.clear(barang.getKode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
