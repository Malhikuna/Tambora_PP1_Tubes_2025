package logistik.view;

import logistik.util.InputUtil;

public class MenuView {
    public static int displayMenuUtama() {
        System.out.println("\n=== Sistem Logistik Pencatatan Keluar Masuk Barang ===");
        System.out.println("1. Barang");
        System.out.println("2. Stok");
        System.out.println("3. Transaksi");
        System.out.println("4. Pengguna");
        System.out.println("5. Logout");
        return InputUtil.inputInt("Pilih Menu");
    }

    public static int displayBarang() {
        System.out.println("\n=== Menu Barang ===");
        System.out.println("1. Lihat Barang");
        System.out.println("2. Cari Barang");
        System.out.println("3. Tambah barang");
        System.out.println("4. Edit Barang");
        System.out.println("5. Hapus Barang");
        System.out.println("6. Kembali");
        return InputUtil.inputInt("Pilih Menu");
    }

    public static int displayStok() {
        System.out.println("\n=== Menu Stok ===");
        System.out.println("1. Catat Barang Masuk");
        System.out.println("2. Catat Barang Keluar");
        System.out.println("3. Stok Saat Ini");
        System.out.println("4. Kembali");
        return InputUtil.inputInt("Pilih Menu");
    }

    public static int displayTransaksi() {
        System.out.println("\n=== Menu Transaksi ===");
        System.out.println("1. Lihat Transaksi");
        System.out.println("2. Kembali");
        return InputUtil.inputInt("Pilih Menu");
    }

    public static int displayPengguna() {
        System.out.println("\n=== Menu Pengguna ===");
        System.out.println("1. Ubah Username");
        System.out.println("2. Ubah Password");
        System.out.println("3. Kembali");
        return InputUtil.inputInt("Pilih Menu");
    }
}
