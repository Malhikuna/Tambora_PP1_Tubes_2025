package logistik;

import logistik.service.*;
import logistik.util.InputUtil;
import logistik.view.MenuView;
import java.sql.SQLException;

public class StokMain {
    public static void main(String[] args) throws SQLException {
        System.out.println("================================================");
        System.out.println(" SISTEM LOGISTIK PENCATATAN KELUAR MASUK BARANG ");
        System.out.println(" Implementasi Queue Structure ");
        System.out.println("================================================");

        boolean berjalan = true;
        while (berjalan) {
            int pilihanUtama = MenuView.displayMenuUtama();

            switch (pilihanUtama) {
                case 1: // Menu Barang
                    boolean menuBarangAktif = true;
                    while (menuBarangAktif) {
                        int pilihanBarang = MenuView.displayBarang();
                        switch (pilihanBarang) {
                            case 1:
                                System.out.println(">>> Aksi: Lihat Barang");
                                // TODO: Implementasi Lihat Barang

                                break;
                            case 2:
                                System.out.println(">>> Aksi: Cari Barang");
                                // TODO: Implementasi Cari Barang
                                break;
                            case 3:
                                System.out.println(">>> Aksi: Tambah Barang");
                                // TODO: Implementasi Tambah Barang
                                break;
                            case 4:
                                System.out.println(">>> Aksi: Edit Barang");
                                // TODO: Implementasi Edit Barang
                                break;
                            case 5: // Kembali ke Menu Utama
                                menuBarangAktif = false;
                                break;
                            default:
                                System.out.println("Pilihan menu barang tidak valid.");
                        }
                    }
                    break;
                case 2: // Menu Stok
                    boolean menuStokAktif = true;
                    while (menuStokAktif) {
                        int pilihanStok = MenuView.displayStok();
                        switch (pilihanStok) {
                            case 1:
                                System.out.println(">>> Aksi: Catat Barang Masuk");
                                // TODO: Implementasi Catat Barang Masuk
                                break;
                            case 2:
                                System.out.println(">>> Aksi: Catat Barang Keluar");
                                // TODO: Implementasi Catat Barang Keluar
                                break;
                            case 3:
                                System.out.println(">>> Aksi: Stok Saat Ini");
                                // TODO: Implementasi Stok Saat Ini
                                break;
                            case 4: // Kembali ke Menu Utama
                                menuStokAktif = false;
                                break;
                            default:
                                System.out.println("Pilihan menu stok tidak valid.");
                        }
                    }
                    break;
                case 3: // Menu Transaksi
                    boolean menuTransaksiAktif = true;
                    while (menuTransaksiAktif) {
                        int pilihanTransaksi = MenuView.displayTransaksi();
                        switch (pilihanTransaksi) {
                            case 1:
                                System.out.println(">>> Aksi: Lihat Transaksi");
                                // TODO: Implementasi Lihat Transaksi
                                TransaksiService trn = new TransaksiService();
                                trn.tampilkanSemuaTransakasi();
                                break;
                            case 2: // Kembali ke Menu Utama
                                menuTransaksiAktif = false;
                                break;
                            default:
                                System.out.println("Pilihan menu transaksi tidak valid.");
                        }
                    }
                    break;
                case 4: // Menu Pengguna
                    boolean menuPenggunaAktif = true;
                    while (menuPenggunaAktif) {
                        int pilihanPengguna = MenuView.displayPengguna();
                        switch (pilihanPengguna) {
                            case 1:
                                System.out.println(">>> Aksi: Ubah Username");
                                // TODO: Implementasi Ubah Username
                                break;
                            case 2:
                                System.out.println(">>> Aksi: Ubah Password");
                                // TODO: Implementasi Ubah Password
                                break;
                            case 3: // Kembali ke Menu Utama
                                menuPenggunaAktif = false;
                                break;
                            default:
                                System.out.println("Pilihan menu pengguna tidak valid.");
                        }
                    }
                    break;
                case 5: // Logout
                    berjalan = false;
                    System.out.println("Logout berhasil. Sampai jumpa!");
                    break;
                default:
                    System.out.println("Pilihan menu utama tidak valid. Silakan coba lagi.");
       }
}

    }
}
