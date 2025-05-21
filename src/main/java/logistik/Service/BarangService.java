package logistik.Service;

import logistik.Model.Barang;

import java.util.ArrayList;

public class BarangService {
    /* Atribut */
    private ArrayList<Barang> daftarBarang = new ArrayList<>();

    // Prosedur untuk menambahkan barang
    public void tambahBarang(Barang barang) {
        daftarBarang.add(barang);
        System.out.println("Barang berhasil ditambahkan");
    }

    // Fungsi untuk mencari barang berdasarkan kode
    public Barang cariBarang(String kode) {
        for (Barang b : daftarBarang) {
            if (b.getKode().equalsIgnoreCase(kode)) {
                return b;
            }
        }
        return null;
    }

    // Prosedur untuk menampilkan semua barang
    public void tampilkanSemuaBarang() {
        if (daftarBarang.isEmpty()) {
            System.out.println("Tidak ada barang yang tersedia.");
        } else {
            System.out.println("Daftar Semua Barang:");
            for (Barang barang : daftarBarang) {
                System.out.println("- " + barang.getNama());
            }
        }
    }

    // Prosedur untuk menghapus barang
    public void hapusBarang(String kode) {
        Barang barang = cariBarang(kode);
        if (barang != null) {
            daftarBarang.remove(barang);
            System.out.println("Barang berhasil dihapus");
        }else {
            System.out.println("Barang dengan kode " + kode + " tidak ditemukan.");
        }
    }
}
