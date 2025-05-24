package logistik.service;

import logistik.Model.Barang;
import logistik.Service.BarangService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarangServiceTest {
    @Test
    public void testTambahBarang() {
        Barang barang = new Barang("101", "Beras", 10000, 5);
        BarangService barangService = new BarangService();
        barangService.tambahBarang(barang);
        Barang result = barangService.cariBarang(barang.getKode());
        assertEquals("101", result.getKode());
    }
}
