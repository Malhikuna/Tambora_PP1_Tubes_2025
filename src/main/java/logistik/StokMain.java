package logistik;

import logistik.model.StokBarang;
import logistik.model.Transaksi;
import logistik.model.User;
import logistik.service.*;
import logistik.util.InputUtil;
import logistik.view.MenuView;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StokMain {
    public static void main(String[] args) throws SQLException {
        System.out.println("================================================");
        System.out.println(" SISTEM LOGISTIK PENCATATAN KELUAR MASUK BARANG ");
        System.out.println(" Implementasi Queue Structure ");
        System.out.println("================================================");

        while (true) {
            TransaksiService transaksiService = new TransaksiService();
            StokQueue stokQueue = new StokQueue();
            AuthService authService = new AuthService();
            BarangService barangService = new BarangService();
            UserService userService = new UserService();
            StokService stokService = new StokService();

            User userYangLogin = null;
            while (userYangLogin == null) {
                int pilihan = MenuView.displayLogin();

                switch (pilihan) {
                    case 1:
                        String username = InputUtil.inputString("Masukkan Username");
                        String password = InputUtil.inputString("Masukkan Password");

                        userYangLogin = authService.login(username, password);

                        if (userYangLogin != null) {
                            System.out.println("\nLogin berhasil!");
                        } else {
                            System.out.println("\nUsername atau password salah. Silakan coba lagi.");
                        }
                        break;
                    case 2:
                        System.out.println("\nTerima kasih telah menggunakan aplikasi ini.");
                        return; // Keluar dari method main() dan menghentikan program
                    default:
                        System.out.println("Pilihan menu utama tidak valid. Silakan coba lagi.");
                }
            }
            boolean sesiAktif = true;

            System.out.println("================================================");
            System.out.println(" Selamat Datang " + userYangLogin.getName());
            System.out.println(" Peran Anda Adalah " + userYangLogin.getRole());
            System.out.println("================================================");

            boolean isAdmin = "Admin".equalsIgnoreCase(userYangLogin.getRole());

            while (sesiAktif) {
                int pilihanUtama = MenuView.displayMenuUtama();

                switch (pilihanUtama) {
                    case 1: // Menu Barang
                        boolean menuBarangAktif = true;
                        while (menuBarangAktif) {
                            int pilihanBarang = MenuView.displayBarang(userYangLogin.getRole());

                            switch (pilihanBarang) {
                                case 1:
                                    System.out.println(">>> Aksi: Lihat Semua Barang");
                                    // TODO: Implementasi Lihat Semua Barang | Murod
                                    break;
                                case 2:
                                    System.out.println(">>> Aksi: Cari Barang");
                                    // TODO: Implementasi Cari Barang | Murod
                                    break;
                                case 3:
                                    if (isAdmin) {
                                        System.out.println(">>> Aksi: Tambah Barang");
                                        // TODO: Implementasi Tambah Barang | Murod
                                    } else {
                                        // Kembali ke Menu Utama
                                        menuBarangAktif = false;
                                    }
                                    break;
                                case 4:
                                    if (isAdmin) {
                                        System.out.println(">>> Aksi: Edit Barang");
                                        // TODO: Implementasi Edit Barang | Dzaki
                                    } else {
                                        System.out.println("Pilihan tidak valid, coba lagi bro.");
                                    }
                                    break;
                                case 5:
                                    if (isAdmin) {
                                        System.out.println(">>> Aksi: Hapus Barang");
                                        // TODO: Implementasi Hapus Barang | Dzaki
                                    } else {
                                        System.out.println("Pilihan tidak valid, coba lagi bro.");
                                    }
                                    break;
                                case 6:
                                    if (isAdmin) {
                                        // Kembali ke Menu Utama
                                        menuBarangAktif = false;
                                    } else {
                                        System.out.println("Pilihan tidak valid, coba lagi bro.");
                                    }
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
                                    // TODO: Implementasi Catat Barang Masuk | Hikmal

                                    try {
                                        // 1. Ambil input dari user
                                        String kodeBarang = InputUtil.inputString("Masukkan Kode Barang");
                                        int jumlah = InputUtil.inputInt("Masukkan Jumlah");
                                        LocalDateTime tanggalMasuk = InputUtil.inputLocalDateTime("Masukkan Tanggal & Waktu");

                                        stokService.catatBarang(kodeBarang, jumlah, "Masuk", tanggalMasuk);

                                        // 3. Beri feedback sukses ke user
                                        System.out.println("\n[SUKSES] Barang masuk untuk kode " + kodeBarang + " berhasil dicatat.");

                                    } catch (IllegalArgumentException | SQLException e) {
                                        // 4. Beri feedback gagal ke user jika ada error
                                        System.out.println("\n[GAGAL] " + e.getMessage());
                                    }

                                    break;
                                case 2:
                                    System.out.println(">>> Aksi: Catat Barang Keluar");
                                    // TODO: Implementasi Catat Barang Keluar | Dzaki

                                    break;
                                case 3:
                                    System.out.println(">>> Aksi: Stok Saat Ini");
                                    // TODO: Implementasi Stok Saat Ini | Hikmal

                                    String kodeBarang = InputUtil.inputString("Masukkan Kode Barang");

                                    List<StokBarang> list = stokQueue.getAllStok(kodeBarang);

                                    int count = 1;
                                    boolean addData = false;
                                    for (StokBarang stokBarang : list) {
                                        addData = true;
                                        System.out.println((count++) + ". " + stokBarang.getKodeBarang() + " - " + stokBarang.getJumlah() + " - " + stokBarang.getTanggalMasuk().toLocalDate());
                                    }
                                    if (!addData) {
                                        System.out.println("Tidak ada data stok yang ditemukan.");
                                    }

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
                                    // TODO: Implementasi Lihat Transaksi | Ikhsan
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
                            int pilihanPengguna = MenuView.displayPengguna(userYangLogin.getRole());

                            switch (pilihanPengguna) {
                                case 1:
                                    System.out.println(">>> Aksi: Ubah Username Saya");
                                    // TODO: Implementasi Ubah Username Saya | Haidar
                                    break;
                                case 2:
                                    System.out.println(">>> Aksi: Ubah Password Saya");
                                    // TODO: Implementasi Ubah Password Saya | Haidar
                                    break;
                                case 3:
                                    System.out.println(">>> Aksi: Ubah Nama Akun Saya");
                                    // TODO: Implementasi Ubah Nama Akun Saya | Haidar
                                    break;
                                case 4:
                                    if (isAdmin) {
                                        System.out.println(">>> Aksi: Tambah Pengguna Baru");
                                        // TODO: Implementasi Tambah Pengguna Baru | Ikhsan
                                    } else {
                                        // // Kembali ke Menu Utama
                                        menuPenggunaAktif = false;
                                    }
                                    break;
                                case 5:
                                    if (isAdmin) {
                                        System.out.println(">>> Aksi: Lihat/Edit/Hapus Pengguna");
                                        // TODO: Implementasi Lihat/Edit/Hapus Pengguna | Ikhsan
                                    } else {
                                        System.out.println("Pilihan tidak valid, coba lagi bro.");
                                    }
                                    break;
                                case 6:
                                    if (isAdmin) {
                                        // // Kembali ke Menu Utama
                                        menuPenggunaAktif = false;
                                    } else {
                                        System.out.println("Pilihan tidak valid, coba lagi bro.");
                                    }
                                    break;
                                default:
                                    System.out.println("Pilihan menu pengguna tidak valid.");
                            }
                        }
                        break;
                    case 5: // Logout
                        sesiAktif  = false;
                        System.out.println("Logout berhasil. Sampai jumpa!");
                        break;
                    default:
                        System.out.println("Pilihan menu utama tidak valid. Silakan coba lagi.");
                }
            }
        }
    }
}
