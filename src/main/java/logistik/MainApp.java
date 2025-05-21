package logistik;

import logistik.Model.Barang;
import logistik.Service.BarangService;

public class MainApp {
    public static void main(String[] args) {
        Barang barang = new Barang("101", "Beras", 10000, 5);
        BarangService barangService = new BarangService();

        barangService.tampilkanSemuaBarang();
        barangService.tambahBarang(barang);
        barangService.tampilkanSemuaBarang();

        barangService.cariBarang(barang.getKode());

        barangService.hapusBarang(barang.getKode());
    }
}