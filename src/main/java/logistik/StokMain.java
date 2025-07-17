package logistik;

import logistik.model.Barang;
import logistik.model.StokBarang;
import logistik.model.Transaksi;
import logistik.model.User;
import logistik.service.*;
import logistik.util.InputUtil;
import logistik.util.ManajemenSesi;
import logistik.view.MenuView;
import logistik.util.HashUtil;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            User user = new User();

            User userYangLogin = null;
            while (userYangLogin == null) {
                int pilihan = MenuView.displayLogin();

                switch (pilihan) {
                    case 1:
                        String username = InputUtil.inputString("Masukkan Username");
                        String password = InputUtil.inputString("Masukkan Password");

                        userYangLogin = authService.login(username, password);

                        if (userYangLogin == null) {
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

            boolean isOwner = "owner".equalsIgnoreCase(userYangLogin.getRole());

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
                                    List<Barang> barang = barangService.tampilkanSemuaBarang();

                                    int count = 1;
                                    boolean addData = false;
                                    for (Barang b : barang) {
                                        addData = true;
                                        System.out.println((count++) + ". " + b.getKode() + " - " + b.getNama() + " - "
                                                + b.getKategoriId() + " - " + b.getSatuan() + " - "
                                                + b.getNamaKategori() + " - " + b.getHargaBeli() + " - "
                                                + b.getHargaJual());
                                    }
                                    if (!addData) {
                                        System.out.println("Tidak ada data barang yang ditemukan.");
                                    }
                                    break;
                                case 2:
                                    System.out.println(">>> Aksi: Cari Barang");
                                    // TODO: Implementasi Cari Barang | Murod
                                    String kodeBarang = InputUtil.inputString("Masukkan Kode Barang");
                                    Barang barangYangDicari = barangService.cariBarang(kodeBarang);
                                    if (barangYangDicari != null) {
                                        System.out.println(barangYangDicari.getKode() + " - "
                                                + barangYangDicari.getNama() + " - " + barangYangDicari.getKategoriId()
                                                + " - " + barangYangDicari.getSatuan() + " - "
                                                + barangYangDicari.getNamaKategori() + " - "
                                                + barangYangDicari.getHargaBeli() + " - "
                                                + barangYangDicari.getHargaJual());
                                    } else {
                                        System.out.println("Tidak ada data barang yang ditemukan.");
                                    }
                                    break;
                                case 3:
                                    if (isOwner) {
                                        System.out.println(">>> Aksi: Tambah Barang");
                                        // TODO: Implementasi Tambah Barang | Murod
                                        String kode = InputUtil.inputString("Masukkan Kode Barang");
                                        String nama = InputUtil.inputString("Masukkan Nama Barang");
                                        String namaKategori = "";
                                        int kategoriId = -1;

                                        System.out.println("1. Makanan");
                                        System.out.println("2. Minuman");
                                        System.out.println("0. Batal");

                                        int aksi = InputUtil.inputInt("Pilih aksi");
                                        switch (aksi) {
                                            case 1:
                                                kategoriId = 1;
                                                namaKategori = "Makanan";
                                                break;
                                            case 2:
                                                kategoriId = 2;
                                                namaKategori = "Minuman";
                                                break;
                                            case 0:
                                                System.out.println("Aksi dibatalkan.");
                                                return; // keluar dari method
                                            default:
                                                System.out.println("Pilihan aksi tidak valid.");
                                                return; // keluar dari method
                                        }

                                        String satuan = InputUtil.inputString("Masukkan Satuan Barang");
                                        Double hargaBeli = InputUtil.inputDouble("Masukkan Harga beli Barang");
                                        Double hargaJual = InputUtil.inputDouble("Masukkan Harga Jual Barang");

                                        Barang b = new Barang(kode, nama, kategoriId, satuan, namaKategori, hargaBeli,
                                                hargaJual);
                                        barangService.tambahBarang(b);
                                        System.out.println("Barang berhasil ditambahkan: " +
                                                b.getKode() + " - " + b.getNama() + " - " + b.getKategoriId() + " - " +
                                                b.getSatuan() + " - " + b.getNamaKategori() + " - " + b.getHargaBeli()
                                                + " - " +
                                                b.getHargaJual());
                                    } else {
                                        // Kembali ke Menu Utama
                                        menuBarangAktif = false;
                                    }
                                    break;
                                case 4:
                                    if (isOwner) {
                                        System.out.println(">>> Aksi: Edit Barang");
                                        // TODO: Implementasi Edit Barang | Dzaki
                                        String kode = InputUtil.inputString("Masukkan Kode Barang");

                                        Barang kodeDiDB = barangService.cariBarang(kode);

                                        if (kode.equals(kodeDiDB.getKode())) {
                                            System.out.println(kodeDiDB.getKode() + " - " + kodeDiDB.getNama() + " - "
                                                    + kodeDiDB.getKategoriId() + " - " + kodeDiDB.getSatuan() + " - "
                                                    + kodeDiDB.getNamaKategori() + " - " + kodeDiDB.getHargaBeli()
                                                    + " - " + kodeDiDB.getHargaJual());
                                            System.out.println("Silakan pilih data yang ingin anda ubah");
                                            System.out.println("1. Nama Barang");
                                            System.out.println("2. Kategori Barang");
                                            System.out.println("3. Satuan Barang");
                                            System.out.println("4. Harga Beli");
                                            System.out.println("5. Harga Jual");
                                            System.out.println("0. Batal");

                                            int aksi = InputUtil.inputInt("Pilih data yang ingin diubah");
                                            switch (aksi) {
                                                case 1:
                                                    String namaBarang = InputUtil
                                                            .inputString("Masukkan nama barang yang baru");
                                                    kodeDiDB.setNama(namaBarang);
                                                    barangService.updateBarang(kodeDiDB);
                                                    if (namaBarang.equals(kodeDiDB.getNama())) {
                                                        System.out.println("Nama barang berhasil diubah");
                                                    } else {
                                                        System.out.println("Nama barang gagal diubah");
                                                    }
                                                    break;
                                                case 2:
                                                    String kategoriBarang = InputUtil
                                                            .inputString("Masukkan Kategori Barang : ");
                                                    kodeDiDB.setNamaKategori(kategoriBarang);
                                                    barangService.updateBarang(kodeDiDB);
                                                    if (kode.equals(kodeDiDB.getNamaKategori())) {
                                                        System.out.println("Kategori barang berhasil diubah");
                                                    } else {
                                                        System.out.println("Kategori barang gagal diubah");
                                                    }
                                                    break;
                                                case 3:
                                                    String satuanBarang = InputUtil
                                                            .inputString("Masukkan Satuan Barang : ");
                                                    kodeDiDB.setSatuan(satuanBarang);
                                                    barangService.updateBarang(kodeDiDB);
                                                    if (kode.equals(kodeDiDB.getSatuan())) {
                                                        System.out.println("Satuan barang berhasil diubah");
                                                    } else {
                                                        System.out.println("Satuan barang gagal diubah");
                                                    }
                                                    break;
                                                case 4:
                                                    double hargaJual = InputUtil
                                                            .inputDouble("Masukkan Harga Jual Barang : ");
                                                    kodeDiDB.setHargaJual(hargaJual);
                                                    barangService.updateBarang(kodeDiDB);
                                                    if (kode.equals(kodeDiDB.getHargaJual())) {
                                                        System.out.println("Harga Jual barang berhasil diubah");
                                                    } else {
                                                        System.out.println("Harga Jual barang gagal diubah");
                                                    }
                                                    break;
                                                case 5:
                                                    double hargaBeli = InputUtil
                                                            .inputDouble("Masukkan Harga Beli Barang : ");
                                                    kodeDiDB.setHargaBeli(hargaBeli);
                                                    barangService.updateBarang(kodeDiDB);
                                                    if (kode.equals(kodeDiDB.getHargaBeli())) {
                                                        System.out.println("Harga Beli barang berhasil diubah");
                                                    } else {
                                                        System.out.println("Harga Beli barang gagal diubah");
                                                    }
                                                    break;
                                                default:
                                                    System.out.println("Pilihan aksi tidak valid.");
                                            }
                                        } else {
                                            System.out.println("Tidak ada data barang yang ditemukan.");
                                        }
                                    } else {
                                        System.out.println("Pilihan tidak valid, coba lagi bro.");
                                    }
                                    break;
                                case 5:
                                    if (isOwner) {
                                        System.out.println(">>> Aksi: Hapus Barang");
                                        // TODO: Implementasi Hapus Barang | Dzaki
                                        String kode = InputUtil.inputString("Masukkan Kode Barang Yang Ingin Dihapus");

                                        try {
                                            barangService.hapusBarang(kode);
                                            System.out.println("Barang Dengan Kode " + kode + " berhasil dihapus");
                                            String confirm = InputUtil
                                                    .inputString("Yakin ingin hapus barang ini ? (y/n)");
                                            if (confirm.equalsIgnoreCase("y")) {
                                                barangService.hapusBarang(kode);
                                            } else {
                                                System.out.println("Hapus barang gagal dihapus");
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Tejadi Kesalahan Saat Hapus Barang: " + e.getMessage());
                                        }
                                    } else {
                                        System.out.println("Pilihan tidak valid, coba lagi bro.");
                                    }
                                    break;
                                case 6:
                                    if (isOwner) {
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
                                        LocalDateTime tanggalMasuk = InputUtil
                                                .inputLocalDateTime("Masukkan Tanggal & Waktu");

                                        stokService.catatBarang(kodeBarang, jumlah, "Masuk", tanggalMasuk, userYangLogin.getId().toString());

                                        // 3. Beri feedback sukses ke user
                                        System.out.println("\n[SUKSES] Barang masuk untuk kode " + kodeBarang
                                                + " berhasil dicatat.");

                                    } catch (IllegalArgumentException | SQLException e) {
                                        // 4. Beri feedback gagal ke user jika ada error
                                        System.out.println("\n[GAGAL] " + e.getMessage());
                                    }
                                    break;
                                case 2:
                                    System.out.println(">>> Aksi: Catat Barang Keluar");
                                    // TODO: Implementasi Catat Barang Keluar | Dzaki
                                    try {
                                        // 1. Ambil input dari user
                                        String kodeBarang = InputUtil.inputString("Masukkan Kode Barang");
                                        int jumlah = InputUtil.inputInt("Masukkan Jumlah");
                                        LocalDateTime tanggalKeluar = InputUtil
                                                .inputLocalDateTime("Masukkan Tanggal & Waktu");

                                        stokService.catatBarang(kodeBarang, jumlah, "Keluar", tanggalKeluar, userYangLogin.getId().toString());

                                        // 3. Beri feedback sukses ke user
                                        System.out.println("\n[SUKSES] Barang keluar untuk kode " + kodeBarang
                                                + " berhasil dicatat.");

                                    } catch (IllegalArgumentException | SQLException e) {
                                        // 4. Beri feedback gagal ke user jika ada error
                                        System.out.println("\n[GAGAL] " + e.getMessage());
                                    }
                                    break;
                                case 3:
                                    System.out.println(">>> Aksi: Stok Saat Ini");
                                    // TODO: Implementasi Stok Saat Ini | Hikmal

                                    String kodeBarang = InputUtil.inputString("Masukkan Kode Barang");

                                    List<StokBarang> list = stokQueue.dapatkanSemuaStok().stream()
                                            .filter(stokBarang -> stokBarang.getKodeBarang().equals(kodeBarang))
                                            .collect(Collectors.toList());

                                    int count = 1;
                                    boolean addData = false;
                                    for (StokBarang stokBarang : list) {
                                        addData = true;
                                        System.out.println((count++) + ". " + stokBarang.getKodeBarang() + " - "
                                                + stokBarang.getNamaBarang() + " - " + stokBarang.getJumlah() + " - "
                                                + stokBarang.getTanggalMasuk().toLocalDate());
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
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                    List<Transaksi> transaksi = transaksiService.tampilkanSemuaTransakasi();

                                    int count = 1;
                                    boolean addData = false;
                                    for (Transaksi t : transaksi) {
                                        addData = true;
                                        java.time.LocalDateTime tanggalRaw = t.getTanggal();
                                        String tanggalFormatted = tanggalRaw.format(formatter);
                                        System.out.println((count++) + ". " + t.getKodeBarang() +
                                                " - " + t.getNamaBarang()
                                                + " - " + t.getJenis()
                                                + " - " + t.getJumlah()
                                                + " - " + tanggalFormatted);
                                    }
                                    if (!addData) {
                                        System.out.println("Tidak ada data transaksi yang ditemukan.");
                                    }

                                    System.out.println("===================");
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

                                    String usernameLama = userYangLogin.getUsername();
                                    String usernameBaru;

                                    while (true) {
                                        usernameBaru = InputUtil.inputString("Masukkan username baru: ");

                                        if (usernameBaru.equals(usernameLama)) {
                                            System.out.println(
                                                    "Username baru tidak boleh sama dengan username lama. Silakan coba lagi.");
                                        } else {
                                            break; // keluar loop kalau username baru valid
                                        }
                                    }

                                    userYangLogin.setUsername(usernameBaru);
                                    try {
                                        userService.updateUserProfile(userYangLogin);
                                        System.out.println("Username berhasil diupdate.");
                                    } catch (SQLException e) {
                                        System.out.println("Gagal mengupdate username: " + e.getMessage());
                                    }
                                    break;
                                case 2:
                                    System.out.println(">>> Aksi: Ubah Password Saya");
                                    // TODO: Implementasi Ubah Password Saya | Ikhsan
                                    String passwordBaru = InputUtil.inputString("Masukkan Password Baru");
                                    String konformasiPsBaru = InputUtil.inputString("Masukkan Ulang Password Baru");

                                    String hashedPassword = HashUtil.hashPassword(passwordBaru);

                                    if (!passwordBaru.equals(konformasiPsBaru)) {
                                        System.out.println("Password baru dan konfirmasi password tidak cocok!");
                                        break;
                                    }

                                    // Validasi sederhana: password tidak boleh kosong
                                    if (passwordBaru.trim().isEmpty()) {
                                        System.out.println("Password baru tidak boleh kosong!");
                                        break;
                                    }

                                    // Memanggil method updatePassword dari UserService
                                    if (userYangLogin == null || userYangLogin.getId() == null) {
                                        System.out.println(
                                                "Kesalahan: Data pengguna yang login tidak ditemukan atau ID tidak valid. Tidak dapat mengubah password.");
                                        break;
                                    }

                                    UUID userSaatIni = userYangLogin.getId();

                                    try {
                                        userService.updatePassword(userSaatIni, hashedPassword);
                                        System.out.println("Proses ubah password selesai.");
                                    } catch (SQLException e) {
                                        System.err.println(
                                                "Terjadi kesalahan database saat mengubah password: " + e.getMessage());
                                        e.printStackTrace();
                                    }

                                    break;
                                case 3:
                                    System.out.println(">>> Aksi: Ubah Nama Akun Saya");
                                    // TODO: Implementasi Ubah Nama Akun Saya | Haidar

                                    String namaAkunLama = userYangLogin.getName();
                                    String namaAkunBaru;

                                    while (true) {
                                        namaAkunBaru = InputUtil.inputString("Masukkan nama baru: ");

                                        if (namaAkunBaru.equals(namaAkunLama)) {
                                            System.out.println(
                                                    "Nama akun baru tidak boleh sama dengan nama akun lama. Silakan coba lagi.");
                                        } else {
                                            break; // keluar loop kalau username baru valid
                                        }
                                    }

                                    userYangLogin.setName(namaAkunBaru);
                                    try {
                                        userService.updateUserProfile(userYangLogin);
                                        System.out.println("Nama akun berhasil diupdate.");
                                    } catch (SQLException e) {
                                        System.out.println("Gagal mengupdate nama akun: " + e.getMessage());
                                    }
                                    break;

                                case 4:
                                    if (isOwner) {
                                        System.out.println(">>> Aksi: Tambah Pengguna Baru");
                                        // TODO: Implementasi Tambah Pengguna Baru | Ikhsan

                                        String username = "";
                                        while (true) {
                                            username = InputUtil.inputString("Masukkan Username");
                                            try {
                                                if (userService.findUserByUsername(username) != null) {
                                                    System.out.println(
                                                            "Username sudah digunakan. Silakan pilih username lain.");
                                                } else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Terjadi kesalahan saat mengecek username.");
                                                e.printStackTrace();
                                            }
                                        }

                                        String password = InputUtil.inputString("Masukkan Password");
                                        String namaLengkap = InputUtil.inputString("Masukkan Nama Lengkap");

                                        String role = "";
                                        while (true) {
                                            role = InputUtil.inputString("Masukkan Role (owner/staf)").trim()
                                                    .toLowerCase();
                                            if (role.equals("owner") || role.equals("staf")) {
                                                break;
                                            } else {
                                                System.out
                                                        .println("Role tidak valid. Hanya boleh 'owner' atau 'staf'.");
                                            }
                                        }
                                        User newUser = new User(username, password, namaLengkap, role);
                                        userService.createUser(newUser);

                                        System.out.println("User Berhasil Ditambahkan: " + newUser.getUsername() + " - "
                                                + newUser.getPassword() + " - " + newUser.getName() + " - "
                                                + newUser.getRole());

                                    } else {
                                        // // Kembali ke Menu Utama
                                        menuPenggunaAktif = false;
                                    }
                                    break;
                                case 5:
                                    if (isOwner) {
                                        System.out.println(">>> Aksi: Lihat/Edit/Hapus Pengguna");
                                        // TODO: Implementasi Lihat/Edit/Hapus Pengguna | Ikhsan
                                        List<User> users = userService.getAllUser();

                                        if (users.isEmpty()) {
                                            System.out.println("Tidak ada pengguna yang terdaftar.");
                                            break;
                                        }

                                        // Tampilkan semua user
                                        System.out.println("Daftar Pengguna:");
                                        for (int i = 0; i < users.size(); i++) {
                                            User us = users.get(i);
                                            System.out.println((i + 1) + ". " + us.getUsername() + " - " + us.getName()
                                                    + " - " + us.getRole());
                                        }

                                        int pilihUser = InputUtil.inputInt(
                                                "Pilih nomor user untuk Edit/Hapus Pengguna (0 untuk batal:) ");

                                        if (pilihUser < 1 || pilihUser > users.size()) {
                                            System.out.println("Pilihan tidak valid.");
                                            break;
                                        }

                                        User selectedUser = users.get(pilihUser - 1);

                                        System.out.println("1. Ubah Password");
                                        System.out.println("2. Hapus User");
                                        System.out.println("0. Batal");

                                        int aksi = InputUtil.inputInt("Pilih Aksi: ");
                                        switch (aksi) {
                                            case 1:
                                                String newPassword = InputUtil.inputString("Masukkan Password Baru");
                                                String hashed = HashUtil.hashPassword(newPassword);
                                                userService.updatePassword(selectedUser.getId(), hashed);
                                                break;
                                            case 2:
                                                String konfirmasi = InputUtil
                                                        .inputString("Yakin ingin hapus user ini? (y/n)");
                                                if (konfirmasi.equalsIgnoreCase("y")) {
                                                    userService.hapusUser(selectedUser.getId());
                                                } else {
                                                    System.out.println("Penghapusan dibatalkan.");
                                                }
                                                break;
                                            case 0:
                                                System.out.println("Aksi dibatalkan.");
                                                break;
                                            default:
                                                System.out.println("Pilihan aksi tidak valid.");
                                        }
                                    } else {
                                        System.out.println("Pilihan tidak valid, coba lagi");
                                    }
                                    break;
                                case 6:
                                    if (isOwner) {
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
                        sesiAktif = false;
                        System.out.println("Logout berhasil. Sampai jumpa!");
                        break;
                    default:
                        System.out.println("Pilihan menu utama tidak valid. Silakan coba lagi.");
                }
            }
        }
    }
}
