package logistik.view;

import logistik.util.InputUtil;

public class MenuView {
    public static int displayLogin() {
        System.out.println("\n=== Sistem Logistik Pencatatan Keluar Masuk Barang ===");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        return InputUtil.inputInt("Pilih Opsi");
    }

    public static int displayMenuUtama() {
        System.out.println("\n=== Menu Utama ===");
        System.out.println("1. Barang");
        System.out.println("2. Stok");
        System.out.println("3. Transaksi");
        System.out.println("4. Manajemen Akun");
        System.out.println("5. Logout");
        return InputUtil.inputInt("Pilih Opsi");
    }

    public static int displayBarang(String role) {
        System.out.println("\n=== Menu Barang ===");

        System.out.println("1. Lihat Barang");
        System.out.println("2. Cari Barang");

        if ("Admin".equalsIgnoreCase(role)) {
            System.out.println("3. Tambah Barang Baru");
            System.out.println("4. Edit Data Barang");
            System.out.println("5. Hapus Barang");
            System.out.println("6. Kembali");
        } else {
            System.out.println("3. Kembali");
        }

        return InputUtil.inputInt("Pilih Opsi");
    }

    public static int displayStok() {
        System.out.println("\n=== Menu Stok ===");
        System.out.println("1. Catat Barang Masuk");
        System.out.println("2. Catat Barang Keluar");
        System.out.println("3. Stok Saat Ini");
        System.out.println("4. Kembali");
        return InputUtil.inputInt("Pilih Opsi");
    }

    public static int displayTransaksi() {
        System.out.println("\n=== Menu Transaksi ===");
        System.out.println("1. Lihat Transaksi");
        System.out.println("2. Kembali");
        return InputUtil.inputInt("Pilih Opsi");
    }

    public static int displayPengguna(String role) {
        System.out.println("\n=== Menu Manajemen Akun ===");

        System.out.println("1. Ubah Username Saya");
        System.out.println("2. Ubah Password Saya");
        System.out.println("3. Ubah Nama Akun Saya");

        if ("Admin".equalsIgnoreCase(role)) {
            System.out.println("---------------------------------");
            System.out.println("4. Tambah Pengguna Baru");
            System.out.println("5. Lihat/Edit/Hapus Pengguna");
            System.out.println("6. Kembali");
            return InputUtil.inputInt("Pilih Opsi");
        } else {
            System.out.println("4. Kembali");
            return InputUtil.inputInt("Pilih Opsi");
        }
    }

    public static int displayManajemenPengguna() {
        System.out.println("\n=== Menu Pengguna ===");
        System.out.println("1. Tambah Pengguna Baru");
        System.out.println("2. Kembali");
        return InputUtil.inputInt("Pilih Opsi");
    }
}
